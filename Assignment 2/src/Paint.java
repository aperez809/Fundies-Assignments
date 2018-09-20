import tester.Tester;

import java.awt.Color;

interface IPaint {
  // return color of paint after any operations (if applicable)
  Color getFinalColor();

  // count the number of paints involved in final
  int countPaints();

  // count the total number of operations were involved in making the color
  int countMixes();

  // count the number of levels of nesting in the formula
  int formulaDepth();

  int formulaDepthHelp();

  String mixingFormula(int depth);

  String mixingFormulaHelp();
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

  public int countPaints() {
    return 1;
  }

  public int countMixes() {
    return 0;
  }

  public int formulaDepth() {
    return 0;
  }

  public int formulaDepthHelp() {
    return 0;
  }

  public String mixingFormula(int depth) {
    return this.name;
  }

  public String mixingFormulaHelp() {
    return this.name;
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

  public int countPaints() {
    return this.operation.countPaints();
  }

  public int countMixes() {
    return this.operation.countMixes();
  }

  public int formulaDepth() {
    return this.operation.formulaDepth();
  }

  public int formulaDepthHelp() {
    return this.operation.formulaDepthHelp();
  }

  public String mixingFormula(int depth) {
    return this.operation.mixingFormula(depth - 1);
  }

  public String mixingFormulaHelp() {
    return this.name;
  }
}

interface IMixture {
  // return final color of paint
  Color getFinalColor();

  // return number of paints in mixture
  int countPaints();

  // how many mixtures were involved in this color
  int countMixes();

  // count how many levels of nesting there are in the formula
  // ravioli ravioli, give me the formuoli
  int formulaDepth();

  int formulaDepthHelp();

  String mixingFormula(int depth);
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

  public int countPaints() {
    return 1 + this.color.countPaints();
  }

  public int countMixes() {
    return 1 + this.color.countMixes();
  }

  public int formulaDepth() {
    return 1 + this.color.formulaDepth();
  }

  public int formulaDepthHelp() {
    return this.color.formulaDepthHelp();
  }

  public String mixingFormula(int depth) {
    if (depth - 1 > 0) {
      return "darken(" + this.color.mixingFormula(depth) + ")";
    }
    else {
      return "darken(" + this.color.mixingFormulaHelp() + ")";
    }
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

  public int countPaints() {
    return 1 + this.color.countPaints();
  }

  public int countMixes() {
    return 1 + this.color.countMixes();
  }

  public int formulaDepth() {
    return 1 + this.color.formulaDepth();
  }

  public int formulaDepthHelp() {
    return this.color.formulaDepthHelp();
  }

  public String mixingFormula(int depth) {
    if (depth - 1 > 0) {
      return "brighten(" + this.color.mixingFormula(depth) + ")";
    }
    else {
      return "brighten(" + this.color.mixingFormulaHelp() + ")";
    }
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
    return getFinalColorHelp(this.top.getFinalColor(), this.bottom.getFinalColor());
  }

  // takes in 2 Color objects and returns a new color object
  Color getFinalColorHelp(Color top, Color bottom) {
    return new Color(top.getRed()/2 + bottom.getRed()/2,
            top.getGreen()/2 + bottom.getGreen()/2,
            top.getBlue()/2 + bottom.getBlue()/2);

  }

  public int countPaints() {
    return this.top.countPaints() + this.bottom.countPaints();
  }

  public int countMixes() {
    return 1 + this.top.countMixes() + this.bottom.countMixes();
  }

  public int formulaDepth() {
    return this.top.formulaDepthHelp() + this.bottom.formulaDepthHelp();
  }

  public int formulaDepthHelp() {
    return 1 + this.top.formulaDepthHelp() + this.bottom.formulaDepthHelp();
  }

  public String mixingFormula(int depth) {
    if (depth > 0) {
      return "blend(" + this.top.mixingFormula(depth) + ", " + this.bottom.mixingFormula(depth) + ")";
    }
    else {
      return "blend(" + this.top.mixingFormulaHelp() + ", " + this.bottom.mixingFormulaHelp() + ")";
    }
  }
}

class ExamplesPaint {
  IPaint red = new Solid("red", new Color(255, 0, 0));
  IPaint green = new Solid("green", new Color(0, 255, 0));
  IPaint blue = new Solid("blue", new Color(0, 0, 255));

  IPaint brightRed = new Combo("bright red", new Brighten(red));
  IPaint purple = new Combo("purple", new Blend(red, blue));
  IPaint darkPurple = new Combo("dark purple", new Darken(purple));
  IPaint khaki = new Combo("khaki", new Blend(red, green));
  IPaint yellow = new Combo("yellow", new Brighten(khaki));
  IPaint mauve = new Combo("mauve", new Blend(purple, khaki));
  IPaint pink = new Combo("pink", new Brighten(mauve));
  IPaint coral = new Combo("coral", new Blend(pink, khaki));

  IPaint ridiculous = new Combo("ridiculous", new Blend(coral, pink));


  boolean testGetFinalColor(Tester t) {
    return t.checkExpect(red.getFinalColor(), new Color(255, 0, 0)) &&
            t.checkExpect(purple.getFinalColor(), new Color(255 / 2, 0, 255 / 2)) &&
            t.checkExpect(mauve.getFinalColor(), new Color(255 / 2 / 2 + 255 / 2 / 2, 63, 63));
  }

  boolean testCountPaints(Tester t) {
    return t.checkExpect(red.countPaints(), 1) &&
            t.checkExpect(coral.countPaints(), 7) &&
            t.checkExpect(brightRed.countPaints(), 2) &&
            t.checkExpect(ridiculous.countPaints(), 12);
  }

  boolean testCountMixes(Tester t) {
    return t.checkExpect(red.countMixes(), 0) &&
            t.checkExpect(brightRed.countMixes(), 1) &&
            t.checkExpect(coral.countMixes(), 6) &&
            t.checkExpect(ridiculous.countMixes(), 11);
  }

  boolean testFormulaDepth(Tester t) {
    return t.checkExpect(red.formulaDepth(), 0) &&
            t.checkExpect(brightRed.formulaDepth(), 1) &&
            t.checkExpect(coral.formulaDepth(), 4) &&
            t.checkExpect(ridiculous.formulaDepth(), 8);
  }

  boolean testMixingFormula(Tester t) {
    return t.checkExpect(red.mixingFormula(0), "red") &&
            t.checkExpect(brightRed.mixingFormula(1), "brighten(red)") &&
            t.checkExpect(coral.mixingFormula(4),
                    "blend(brighten(blend(blend(red, blue), blend(red, green))), blend(red, green))") &&
            t.checkExpect(coral.mixingFormula(3), "blend(brighten(blend(purple, khaki)), blend(red, green))");
  }
}