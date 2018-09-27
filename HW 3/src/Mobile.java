import javalib.worldcanvas.WorldCanvas;
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
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

  //gets the true width of the mobile
  int curWidthHelp();

  //combine 2 balanced mobiles together to create a new balanced one
  IMobile buildMobile(IMobile that, int length, int strut);

  //combine 2 balanced mobiles together to create a new balanced one
  IMobile buildMobileHelp(IMobile that, int length, int strutOne, int strutTwo);

  //draw the image of the mobile
  WorldImage drawMobile();

  WorldImage drawMobileHelp(int leftPinCur, int rightPinCur);
}

class Simple implements IMobile {
  int length;

  @Override
  public WorldImage drawMobileHelp(int leftPinCur, int rightPinCur) {
    return this.drawMobile();
  }

  int weight;
  Color color;


  Simple(int length, int weight, Color color) {
    this.length = length;
    this.weight = weight;
    this.color = color;
  }

  /*

  FIELDS:

  ... this.length ... int
  ... this.weight ... int
  ... this.color  ... Color

  METHODS:

  ... this.totalWeight()         ...      -- int
  ... this.totalHeight()            ...      -- int
  ... this.isBalanced()          ...      -- boolean
  ... this.curWidth()        ...      -- int
  ... this.buildMobile()      ...      -- IMobile
  ... this.drawMobile()           ...      -- WorldImage

  METHODS FOR FIELDS:
  N/A
  */

  //gets total weight of mobile
  public int totalWeight() {
    return this.weight;
  }


  //gets total height of mobile
  public int totalHeight() {
    return this.length + this.weight / 10;
  }

  //is this mobile balanced?
  public boolean isBalanced() {
    return true;
  }

  //what is the true width of this mobile
  public int curWidth() {
    if (this.weight % 10 == 0) {
      return (this.weight / 10) / 2;
    }
    else {
      return ((this.weight / 10) + 1) / 2;
    }
  }

  //what is the true width of this mobile
  public int curWidthHelp() {
    return 0;
  }

  //combine two balanced mobiles to create a new balanced mobile
  public IMobile buildMobile(IMobile that, int length, int strut) {
    return this.buildMobileHelp(that, length, strut - 1, strut - (strut - 1));
  }

  //combine two balanced mobiles to create a new balanced mobile
  public IMobile buildMobileHelp(IMobile that, int length, int strutOne, int strutTwo) {
    IMobile answer = new Complex(length, strutOne, strutTwo, this, that);
    if (answer.isBalanced()) {
      return new Complex(length, strutOne, strutTwo, this, that);
    }
    else {
      return this.buildMobileHelp(that, length, strutOne - 1, strutTwo + 1);
    }
  }

  //draw image of the mobile
  public WorldImage drawMobile() {
    return new RectangleImage(this.weight, this.weight, "solid", this.color);
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

  /*
  TEMPLATE
  FIELDS:
  ... this.length ...         -- String
  ... this.leftside ...          -- int
  ... this.rightside ...         -- int
  ... this.left ...          -- IMobile
  ... this.right ...          -- IMobile

  METHODS
  ... this.totalWeight()         ...      -- int
  ... this.totalHeight()            ...      -- int
  ... this.isBalanced()          ...      -- boolean
  ... this.curWidth()        ...      -- int
  ... this.buildMobile()      ...      -- IMobile
  ... this.drawMobile()           ...      -- WorldImage

  METHODS FOR FIELDS
  ... this.totalWeight()         ...      -- int
  ... this.totalHeight()            ...      -- int
  ... this.isBalanced()          ...      -- boolean
  ... this.curWidth()        ...      -- int
  ... this.buildMobile()      ...      -- IMobile
  ... this.drawMobile()           ...      -- WorldImage
  */

  //gets total weight of mobile
  public int totalWeight() {
    return this.left.totalWeight() + this.right.totalWeight();
  }

  //gets total height of mobile
  public int totalHeight() {
    return this.length + Math.max(this.left.totalHeight(),this.right.totalHeight());
  }

  //is this mobile balanced?
  public boolean isBalanced() {
    return (this.left.totalWeight() * this.leftside) == (this.right.totalWeight() * this.rightside);
  }

  //gets the true width of the mobile
  public int curWidth() {
    return (this.leftside + this.left.curWidthHelp())
            + (this.rightside + this.right.curWidthHelp());
  }

  //gets the true width of the mobile
  public int curWidthHelp() {
    return Math.max(this.leftside + this.left.curWidth(),
            this.rightside + this.right.curWidth());
  }


  //build a new mobile such that the lengths of the left and right
  //struts create 0 net torque
  public IMobile buildMobile(IMobile that, int length, int strut) {
    return this.buildMobileHelp(that, length, strut - 1, strut - (strut - 1));
  }

  //build a new mobile such that the lengths of the left and right
  //struts create 0 net torque
  public IMobile buildMobileHelp(IMobile that, int length, int strutOne, int strutTwo) {
    IMobile answer = new Complex(length, strutOne, strutTwo, this, that);
    if (answer.isBalanced()) {
      return new Complex(length, strutOne, strutTwo, this, that);
    }
    else {
      return this.buildMobileHelp(that, length, strutOne - 1, strutTwo + 1);
    }
  }

  //draws image of the mobile
  public WorldImage drawMobile() {
    double leftMiddle = this.leftside / 2.0;
    double rightMiddle = (this.rightside / 2.0);
    WorldImage leftStem = new LineImage(new Posn(0, this.leftside),
            Color.BLACK).movePinhole(0 - this.length, 0 - leftMiddle);
    WorldImage rightStem = new LineImage(new Posn(0, this.rightside),
            Color.BLACK).movePinhole(0 - this.length, 0 - rightMiddle);

    return new OverlayImage(
            new RotateImage(new OverlayImage(new RotateImage(leftStem, 270),
                    this.left.drawMobileHelp(this.leftside, this.length)).movePinhole(this.leftside, this.length),
                    90),
            new RotateImage(new OverlayImage(new RotateImage(rightStem, 270),
                    this.right.drawMobileHelp(this.rightside, this.length)).movePinhole(this.rightside, this.length),
                    90));
  }

  public WorldImage drawMobileHelp(int leftPinCur, int rightPinCur) {
    double leftMiddle = this.leftside / 2.0;
    double rightMiddle = (this.rightside / 2.0);
    WorldImage leftStem = new LineImage(new Posn(leftPinCur, this.leftside),
            Color.BLACK).movePinhole(leftPinCur - this.length, leftPinCur - leftMiddle);
    WorldImage rightStem = new LineImage(new Posn(rightPinCur, this.rightside),
            Color.BLACK).movePinhole(rightPinCur - this.length, rightPinCur - rightMiddle);

    return new OverlayImage(
            new RotateImage(new OverlayImage(new RotateImage(leftStem, 270),
                    this.left.drawMobileHelp((this.leftside + leftPinCur),
                            (this.rightside + rightPinCur))
            ).movePinhole(this.leftside, this.length),
                    90),
            new RotateImage(new OverlayImage(new RotateImage(rightStem, 270),
                    this.right.drawMobileHelp((this.leftside + rightPinCur),
                            (this.rightside + rightPinCur))
            ).movePinhole(this.rightside, this.length),
                    90));
  }
}

class ExamplesMobiles {
  IMobile exampleSimple = new Simple(2, 20, new Color(0, 0, 255));
  IMobile exampleComplex = new Complex(1, 9, 3,
          new Simple(1, 36, new Color(0, 0, 255)),
          new Complex(2, 8, 1,
                  new Simple(1, 12, new Color(255, 0, 0)),
                  new Complex(2, 5, 3,
                          new Simple(2, 36, new Color(255, 0, 0)),
                          new Simple(1, 60, new Color(0, 255, 0)))));
  IMobile example3 = new Complex(1, 9, 3,
          new Simple(1, 38, new Color(0, 0, 255)),
          new Complex(2, 8, 1,
                  new Simple(1, 12, new Color(255, 0, 0)),
                  new Complex(
                          2, 5, 3,
                          new Simple(2, 36, new Color(255, 0, 0)),
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
            && t.checkExpect(exampleComplex.curWidth(), 21);
  }

  boolean testBuildMobile(Tester t) {
    return t.checkExpect(exampleSimple.buildMobile(exampleSimple, 2, 4),
            new Complex(2, 2, 2, exampleSimple, exampleSimple))
            && t.checkExpect(exampleComplex.buildMobile(exampleComplex, 1, 50),
            new Complex(1, 25, 25, exampleComplex, exampleComplex));
  }

  boolean testFailure(Tester t) {
    return t.checkExpect(
            new ScaleImageXY(new RectangleImage(
                    60, 40, OutlineMode.SOLID, Color.GRAY),0.5, 0.25),
            new RectangleImage(30, 15, OutlineMode.SOLID, Color.GRAY));
  }

  boolean testDrawMobile(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(exampleComplex.drawMobile(), 250, 250))
            && c.show();
  }
}

