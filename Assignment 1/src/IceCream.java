interface IIceCream {
}

class EmptyServing implements IIceCream {
  boolean cone;

  /*
  Fields:

  this.cone ... boolean

  Methods:

  N/A

   */

  EmptyServing(boolean cone) {
    this.cone = cone;
  }
}

class Scooped implements IIceCream {
  IIceCream more;
  String flavor;


  /*
  Fields:

  this.more   ... IIceCream
  this.flavor ... String

  Methods:

  N/A

   */

  Scooped(IIceCream more, String flavor) {
    this.more = more;
    this.flavor = flavor;
  }
}

class ExamplesIceCream {
  IIceCream order1 = new Scooped(new Scooped(new Scooped(new Scooped(new EmptyServing(false),
          "caramel swirl"),
          "black raspberry"),
          "coffee"),
          "mint chip");

  IIceCream order2 = new Scooped(new Scooped(new Scooped(new EmptyServing(true),
          "strawberry"),
          "vanilla"),
          "chocolate");
}