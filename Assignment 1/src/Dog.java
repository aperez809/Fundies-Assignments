class Dog {
  String name;
  String breed;
  int yob;
  String state;
  boolean hypoallergenic;

  /*
  Fields:

  this.name           ... String
  this.breed          ... String
  this.yob            ... int
  this.state          ... String
  this.hypoallergenic ... boolean

  Methods:

  N/A

   */
  Dog(String name, String breed, int yob, String state, boolean hypoallergenic) {
    this.name = name;
    this.breed = breed;
    this.yob = yob;
    this.state = state;
    this.hypoallergenic = hypoallergenic;
  }
}

class ExamplesDog {
  Dog huffle = new Dog("Hufflepuff","Wheaten Terrier", 2012,"TX", true);
  Dog pearl = new Dog("Pearl", "Labrador Retriever", 2016, "MA", false);
  Dog rikki = new Dog("Rikki", "Puggle", 2008, "MD", true);
}


