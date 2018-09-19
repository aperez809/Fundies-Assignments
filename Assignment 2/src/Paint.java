import tester.Tester;

import java.awt.Color;

interface IPaint {
  // return color of paint after any operations (if applicable)
  Color getFinalColor();
}

class Solid implements IPaint {
  String name;
  Color color;

  Solid(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  public Color getFinalColor() {
    return this.color;
  }
}

class Combo implements IPaint {
  String name;
  IMixture operation;

  Combo(String name, IMixture operation) {
    this.name = name;
    this.operation = operation;
  }


  // Get color of paint
  // calls getFinalColor() on operation (an IMixture), which can be 1 of 3 things
  public Color getFinalColor() {
    return this.operation.getFinalColor();
  }
}

interface IMixture {
  Color getFinalColor();
}

class Darken implements IMixture {
  IPaint color;

  Darken(IPaint color) {
    this.color = color;
  }
  // return final color of paint with getFinalColor then apply darker()
  // and return that result
  // recursively calls getFinalColor to check if there are underlying Combos or just a solid
  public Color getFinalColor() {
    return this.color.getFinalColor().darker();
  }
}

class Brighten implements IMixture {
  IPaint color;

  Brighten(IPaint color) {
    this.color = color;
  }

  // return final color of paint with getFinalColor then apply brighter()
  // and return that result
  // recursively calls getFinalColor to check if there are underlying Combos or just a solid
  public Color getFinalColor() {
    return this.color.getFinalColor().brighter();
  }
}

class Blend implements IMixture {
  IPaint top;
  IPaint bottom;

  Blend(IPaint top, IPaint bottom) {
    this.top = top;
    this.bottom = bottom;
  }

  // return final color of top and bottom with getFinalColor
  public Color getFinalColor() {
    return this.top.getFinalColor();
  }

  Color getFinalColorHelp() {
    return null;

  }
}

class ExamplesPaint {
  IPaint red = new Solid("red", new Color(255,0,0));
  IPaint green = new Solid("green", new Color(0,255,0));
  IPaint blue = new Solid("blue", new Color(0,0,255));

  IPaint brightRed = new Combo("bright red", new Brighten(red));
  IPaint purple = new Combo("purple", new Blend(red, blue));
  IPaint darkPurple = new Combo("dark purple", new Darken(purple));
  IPaint khaki = new Combo("khaki", new Blend(red, green));
  IPaint yellow = new Combo("yellow", new Brighten(khaki));
  IPaint mauve = new Combo("mauve", new Blend(purple, khaki));
  IPaint pink = new Combo("pink", new Brighten(mauve));
  IPaint coral = new Combo("coral", new Blend(pink, khaki));


  boolean testGetFinalColor(Tester t) {
    return t.checkExpect(red.getFinalColor(), new Color(255,0,0)) &&
            t.checkExpect(brightRed.getFinalColor(), new Color(255,0,0));
  }

}