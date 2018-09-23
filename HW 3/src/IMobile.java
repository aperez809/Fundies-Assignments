import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import tester.TestResults.TestEquality;

import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)

interface IMobile {

  //calculates total weight of the given IMobile
  int totalWeight();

  //calculates total height of the given IMobile
  int totalHeight();

  //is the mobile balanced?
  //i.e. no net torque on node (weight of node * length of strut)
  boolean isBalanced();
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
    // TODO Auto-generated method stub
    return this.length + this.weight/10;
  }

  public boolean isBalanced() {
    return true;
  }

  int isBalancedHelp(int strutLength) {
    return 0;
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

}