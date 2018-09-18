import java.awt.Color;

interface IPaint {
}

class Solid implements IPaint {
  String name;
  Color color;

  Solid(String name, Color color) {
    this.name = name;
    this.color = color;
  }
}

class Combo implements IPaint {
  String name;
  IMixture operation;

  Combo(String name, IMixture operation) {
    this.name = name;
    this.operation = operation;
  }
}

interface IMixture {
}

class Darken implements IMixture {

}

class Brighten implements IMixture {

}

class Blend implements IMixture {
  IPaint top;
  IPaint bottom;

  }

class ExamplesPaint {
  IPaint red = new Solid("red", new Color(255,0,0));
  IPaint green = new Solid("green", new Color(0,255,0));
  IPaint blue = new Solid("blue", new Color(0,0,255));

  IMixture purple = new Blend();

}