interface IHousing {
}

interface ITransportation {
}

class Hut implements IHousing {
  int capacity;
  int population;

  /*
  Fields:

  this.capacity   ... int
  this.population ... int

  Methods:

  N/A

   */

  Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;
  }
}

class Inn implements IHousing {
  String name;
  int capacity;
  int population;
  int stalls;

  /*
  Fields:

  this.name       ... String
  this.capacity   ... int
  this.population ... int
  this.stalls     ... int

  Methods:

  N/A

   */

  Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.population = population;
    this.stalls = stalls;
  }
}

class Castle implements IHousing {
  String name;
  String familyName;
  int population;
  int carriageHouse;

  /*
  Fields:

  this.name          ... String
  this.familyName    ... String
  this.population    ... int
  this.carriageHouse ... int

  Methods:

  N/A

   */

  Castle(String name, String familyName, int population, int carriageHouse) {
    this.name = name;
    this.familyName = familyName;
    this.population = population;
    this.carriageHouse = carriageHouse;
  }
}

class Horse implements ITransportation {
  IHousing to;
  IHousing from;
  String name;
  String color;

  /*
  Fields:

  this.to    ... IHousing
  this.from  ... IHousing
  this.name  ... String
  this.color ... String

  Methods:

  N/A

   */

  Horse(IHousing to, IHousing from, String name, String color) {
    this.to = to;
    this.from = from;
    this.name = name;
    this.color = color;
  }
}

class Carriage implements ITransportation {
  IHousing to;
  IHousing from;
  int tonnage;

  /*
  Fields:

  this.to      ... IHousing
  this.from    ... IHousing
  this.tonnage ... int

  Methods:

  N/A

   */

  public Carriage(IHousing to, IHousing from, int tonnage) {
    this.to = to;
    this.from = from;
    this.tonnage = tonnage;
  }
}

class ExamplesTravel {

  IHousing hovel = new Hut(5, 1);
  IHousing teepee = new Hut(6, 5);

  IHousing winterfell = new Castle("Winterfell", "Stark", 500, 6);
  IHousing kingsLanding = new Castle("King's Landing", "Lannister", 2000, 12);

  IHousing crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  IHousing marriott = new Inn("Marriott", 150, 100, 3);

  ITransportation horse1 = new Horse(hovel, teepee, "Buttercup", "brown");
  ITransportation horse2 = new Horse(winterfell, kingsLanding, "Seabiscuit", "black");

  ITransportation carriage1 = new Carriage(marriott, kingsLanding, 3);
  ITransportation carriage2 = new Carriage(crossroads, winterfell, 3);

}
