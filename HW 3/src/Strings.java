// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {

  // combine all Strings in this list into one
  String combine();

  // produces a new list sorted in alphabetical order
  ILoString sort();

  ILoString insert(String name);
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString(){}

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  public ILoString sort() {
    return this;
  }

  public ILoString insert(String name) {
    return new ConsLoString(name, this);
  }

}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest){
    this.first = first;
    this.rest = rest;
  }

    /*
     TEMPLATE
     FIELDS:
     ... this.first ...         -- String
     ... this.rest ...          -- ILoString

     METHODS
     ... this.combine() ...     -- String

     METHODS FOR FIELDS
     ... this.first.concat(String) ...        -- String
     ... this.first.compareTo(String) ...     -- int
     ... this.rest.combine() ...              -- String

     */

  // combine all Strings in this list into one
  public String combine(){
    return this.first.concat(this.rest.combine());
  }

  public ILoString sort() {
    return this.rest.sort().insert(this.first);
  }

  //compare first of list to the accumulated list
  public ILoString insert(String name) {
    if (this.first.toLowerCase().compareTo(name.toLowerCase()) <= 0) {
      return new ConsLoString(this.first, this.rest.insert(name));
    }
    else {
      return new ConsLoString(name, this);
    }
  }
}

// to represent examples for lists of strings
class ExamplesStrings{

  ILoString mary = new ConsLoString("Mary ",
          new ConsLoString("had ",
                  new ConsLoString("a ",
                          new ConsLoString("little ",
                                  new ConsLoString("lamb.", new MtLoString())))));

  ILoString str2 = new ConsLoString("My ", new ConsLoString("name ", new ConsLoString(
          "is ",
          new ConsLoString("Alex ",
                  new ConsLoString("Perez, ",
                          new ConsLoString("Alex ",
                                  new ConsLoString("Perez ",
                                          new ConsLoString("is ",
                                                  new ConsLoString("my ",
                                                          new ConsLoString("name.",
                                                                  new MtLoString()))))))))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t){
    return t.checkExpect(this.mary.combine(), "Mary had a little lamb.")
            && t.checkExpect(this.str2.combine(),
            "My name is Alex Perez, Alex Perez is my name.");
  }

  boolean testSort(Tester t) {
    return t.checkExpect(this.mary.sort(), new ConsLoString("a ",
            new ConsLoString("had ",
                    new ConsLoString("lamb.",
                            new ConsLoString("little ",
                                    new ConsLoString("Mary ", new MtLoString()))))))
            && t.checkExpect(this.str2.sort(), new ConsLoString("Alex ",
            new ConsLoString("Alex ",
                    new ConsLoString("is ",
                            new ConsLoString("is ",
                                    new ConsLoString("my ",
                                            new ConsLoString("My ",
                                                    new ConsLoString("name ",
                                                            new ConsLoString("name.",
                                                                    new ConsLoString("Perez ",
                                                                            new ConsLoString("Perez, ",
                                                                            new MtLoString())))))))))));
  }
}