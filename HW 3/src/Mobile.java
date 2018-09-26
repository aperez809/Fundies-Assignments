import javalib.worldcanvas.WorldCanvas;
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import tester.TestResults.TestEquality;

import java.awt.Color;          // general colors (as triples of red,green,blue values)

import static javalib.worldimages.TextImage.c;
// and predefined colors (Red, Green, Yellow, Blue, Black, White)

interface IMobile {

  //calculates total weight of the given IMobile
  int totalWeight();

  //calculates total height of the given IMobile
  int totalHeight();

  //is the mobile balanced?
  //i.e. no net torque on node (weight of node * length of strut)
  boolean isBalanced();

  //gets the true width of the mobile
  int curWidth();

  //used to traverse only the left side of the mobile at every split
  int curWidthLeft();

  //used to traverse only the right side of the mobile at every split
  int curWidthRight();

  //combine 2 balanced mobiles together to create
  IMobile buildMobile(IMobile that, int length, int strut);

  WorldImage drawMobile();
}

class Simple implements IMobile {
  int length;
  int weight;
  Color color;

  Simple(int length, int weight, Color color) {
    this.length = length;
    this.weight = weight;
    this.color = color;
  }

  public int totalWeight() {
    return this.weight;
  }

  public int totalHeight() {
    return this.length + this.weight/10;
  }

  public boolean isBalanced() {
    return true;
  }

  public int curWidth() {
    if (this.weight % 10 == 0) {
      return this.weight / 10;
    } else {
      return this.weight / 10 + 1;
    }
  }

  public int curWidthLeft() {
    return this.curWidth();
  }

  public int curWidthRight() {
    return this.curWidth();
  }

  public IMobile buildMobile(IMobile that, int length, int strut) {
    return this;
  }

  public WorldImage drawMobile() {
    return new RectangleImage(this.weight/10 * 10, this.weight/10 * 10, "solid", this.color);
  }


}

class Complex implements IMobile {
  int length;
  int leftside;
  int rightside;
  IMobile left;
  IMobile right;

  Complex(int length, int leftside, int rightside, IMobile left, IMobile right) {
    this.length = length;
    this.leftside = leftside;
    this.rightside = rightside;
    this.left = left;
    this.right = right;
  }

  public int totalWeight() {
    return this.left.totalWeight() + this.right.totalWeight();
  }

  public int totalHeight() {
    return this.length + Math.max(this.left.totalHeight(),this.right.totalHeight());
  }

  public boolean isBalanced() {
    return (this.left.totalWeight() * this.leftside) == (this.right.totalWeight() * this.rightside);
  }

  public int curWidth() {
    return (this.leftside + this.left.curWidthLeft()) + (this.rightside + this.right.curWidthRight());
  }

  public int curWidthLeft() {
    return this.leftside + this.left.curWidthLeft();
  }

  public int curWidthRight() {
    return this.rightside + this.right.curWidthRight();
  }

  //build a new mobile such that the lengths of the left and right
  //struts create 0 net torque
  public IMobile buildMobile(IMobile that, int length, int strut) {
    return new Complex(length, 0, 0, this, that);
  }

  /*public WorldImage drawMobile() {
    double middle = (double)(length / 2);
    WorldImage stem = new LineImage(new Posn(0, this.length),
            Color.BLACK).movePinhole(0, 0 - middle);

    return new OverlayImage(this.left.drawMobile(), this.right.drawMobile());
  }
*/
  public WorldImage drawMobile() {
    double leftMiddle = (double)(this.leftside / 2);
    double rightMiddle = (double)(this.rightside / 2);
    WorldImage leftStem = new LineImage(new Posn(0, this.leftside),
            Color.BLACK).movePinhole(0, 0 - leftMiddle);
    WorldImage rightStem = new LineImage(new Posn(0, this.rightside),
            Color.BLACK).movePinhole(0, 0 - rightMiddle);

    return new OverlayImage(
            new RotateImage(new OverlayImage(new RotateImage(this.left.drawMobile(), 270),
                    leftStem).movePinhole(this.leftside, this.length),
                    90),
            new RotateImage(new OverlayImage(new RotateImage(this.right.drawMobile(), 270),
                    rightStem).movePinhole(this.rightside, this.length),
                    90));
  }

  public IMobile buildMobile(IMobile that) {
    return null;
  }


}

class ExamplesMobiles {
  IMobile exampleSimple = new Simple(2, 20, new Color(0, 0, 255));
  IMobile exampleComplex = new Complex(1, 9, 3, new Simple(1, 36, new Color(0, 0, 255)),
          new Complex(2, 8, 1, new Simple(1, 12, new Color(255, 0, 0)),
                  new Complex(
                          2, 5, 3, new Simple(2, 36, new Color(255, 0, 0)),
                          new Simple(1, 60, new Color(0, 255, 0)))));
  IMobile example3 = new Complex(1, 9, 3, new Simple(1, 38, new Color(0, 0, 255)),
          new Complex(2, 8, 1, new Simple(1, 12, new Color(255, 0, 0)),
                  new Complex(
                          2, 5, 3, new Simple(2, 36, new Color(255, 0, 0)),
                          new Simple(1, 60, new Color(0, 255, 0)))));

  boolean testTotalWeight(Tester t) {
    return t.checkExpect(exampleSimple.totalWeight(), 20)
            && t.checkExpect(exampleComplex.totalWeight(), 144);
  }

  boolean testTotalHeight(Tester t) {
    return t.checkExpect(exampleSimple.totalHeight(), 4)
            && t.checkExpect(exampleComplex.totalHeight(), 12);
  }

  boolean testIsBalanced(Tester t) {
    return t.checkExpect(exampleSimple.isBalanced(), true)
            && t.checkExpect(exampleComplex.isBalanced(), true)
            && t.checkExpect(example3.isBalanced(), false);
  }

  boolean testCurWidth(Tester t) {
    return t.checkExpect(exampleSimple.curWidth(), 2)
            && t.checkExpect(exampleComplex.curWidth(), 26);
  }

  //boolean test

  boolean testFailure(Tester t) {
    return t.checkExpect(
            new ScaleImageXY(new RectangleImage(60, 40, OutlineMode.SOLID, Color.GRAY), 0.5, 0.25),
            new RectangleImage(30, 15, OutlineMode.SOLID, Color.GRAY));
  }

  boolean testDrawMobile(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(exampleComplex.drawMobile(), 250, 250))
            && c.show();
  }
}

