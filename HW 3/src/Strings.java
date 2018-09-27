// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {

  // combine all Strings in this list into one
  String combine();

  // produces a new list sorted in alphabetical order
  ILoString sort();

  //inserts an item into the list if it comes before
  // the first of the list alphabetically
  ILoString insert(String word);

  //is the list sorted?
  boolean isSorted(ILoString acc);

  //is the list sorted?
  boolean isSortedHelp(String first);

  //reverses the list
  ILoString reverse();


  ILoString append(String word);

  //get the rest of the list, depending on type
  ILoString getRest();

  String getFirst();

  //given two lists, odd indices are filled with items from the first list
  //and even indices are filled with items from second list
  ILoString interleave(ILoString that);

  //add two lists together and return the sorted version of it
  ILoString merge(ILoString that);

  //does this list consist of pairs of the same value?
  boolean doubleList();

  boolean doubleListHelper(ILoString that);

  boolean isPalindromeList(ILoString rest);

  boolean isPalHelp(String first);
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString(){}

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  public boolean isPalHelp(String first) {
    return true;
  }

  public boolean isPalindromeList(ILoString that) {
    return true;
  }

  public ILoString sort() {
    return this;
  }

  public ILoString insert(String name) {
    return new ConsLoString(name, this);
  }

  public boolean isSorted(ILoString acc) {
    return true;
  }

  public boolean isSortedHelp(String that) {
    return false;
  }

  public ILoString getRest() {
    return this;
  }

  public String getFirst() {
    return "";
  }

  public ILoString interleave(ILoString that) {
    return that;
  }

  public ILoString merge(ILoString that) {
    return this;
  }

  public boolean doubleList() {
    return true;
  }

  public boolean doubleListHelper(ILoString that) {
    return true;
  }


  public ILoString reverse() {
    return this;
  }

  public ILoString append(String word) {
    return new ConsLoString(word, this);
  }

}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;

  public String getFirst() {
    return this.first;
  }

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
  public ILoString insert(String word) {
    if (this.first.toLowerCase().compareTo(word.toLowerCase()) <= 0) {
      return new ConsLoString(this.first, this.rest.insert(word));
    }
    else {
      return new ConsLoString(word, this);
    }
  }

  public boolean isSorted(ILoString that) {
    return that.isSortedHelp(this.first) && this.rest.isSorted(that.getRest());
  }

  public boolean isSortedHelp(String that) {
    return this.first.equals(that);
  }

  public ILoString getRest() {
    return this.rest;
  }

  public ILoString interleave(ILoString that) {
    return new ConsLoString(this.first, that.interleave(this.rest));
  }

  public ILoString merge(ILoString that) {
    return this.interleave(that).sort();
  }

  public boolean doubleList() {
    return this.doubleListHelper(this.rest);
  }

  public boolean doubleListHelper(ILoString that) {
    return this.first.equals(that.getFirst())
            && this.rest.doubleListHelper(that.getRest());
  }

  public boolean isPalindromeList(ILoString that) {
    return that.isPalHelp(this.first) && this.rest.isPalindromeList(that.getRest());
  }


  public boolean isPalHelp(String that) {
    return this.first.equals(that);
  }

  public ILoString reverse() {
    return this.rest.reverse().append(this.first);
  }

  public ILoString append(String first) {
    return new ConsLoString(this.first, this.rest.append(first));
  }

  public boolean listsEqual(ILoString that) {
    return this.first.equals(that);
  }

}

// to represent examples for lists of strings
class ExamplesStrings{

  ILoString mary = new ConsLoString("Mary ",
          new ConsLoString("had ",
                  new ConsLoString("a ",
                          new ConsLoString("little ",
                                  new ConsLoString("lamb.", new MtLoString())))));

  ILoString marySort = mary.sort();

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

  ILoString str2Sort = str2.sort();

  ILoString doubles = new ConsLoString("beep ",
          new ConsLoString("beep ",
                  new ConsLoString("boop ",
                          new ConsLoString("boop ",
                                  new MtLoString()))));

  //"palindrome this is a a is this palindrome"
  ILoString pal = new ConsLoString("palindrome ",
          new ConsLoString("this ",
          new ConsLoString("is ",
                  new ConsLoString("a ",
  new ConsLoString("a ",
          new ConsLoString("is ",
                  new ConsLoString("this ",
                          new ConsLoString("palindrome ", new MtLoString()))))))));


  // test the method combine for the lists of Strings
  boolean testCombine(Tester t){
    return t.checkExpect(mary.combine(), "Mary had a little lamb.")
            && t.checkExpect(str2.combine(),
            "My name is Alex Perez, Alex Perez is my name.");
  }

  boolean testSort(Tester t) {
    return t.checkExpect(mary.sort(), new ConsLoString("a ",
            new ConsLoString("had ",
                    new ConsLoString("lamb.",
                            new ConsLoString("little ",
                                    new ConsLoString("Mary ", new MtLoString()))))))
            && t.checkExpect(str2.sort(), new ConsLoString("Alex ",
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

  boolean testIsSorted(Tester t) {
    return t.checkExpect(mary.isSorted(marySort), false)
            && t.checkExpect(mary.sort().isSorted(marySort), true)
            && t.checkExpect(str2.isSorted(str2Sort), false)
            && t.checkExpect(str2.sort().isSorted(str2Sort), true);
  }

  boolean testInterleave(Tester t) {
    return t.checkExpect(marySort.interleave(marySort), new ConsLoString("a ",
            new ConsLoString("a ",
                    new ConsLoString("had ",
                            new ConsLoString("had ",
                                    new ConsLoString("lamb.",
                                            new ConsLoString("lamb.",
                                                    new ConsLoString("little ",
                                                            new ConsLoString("little ",
                                                                    new ConsLoString("Mary ",
                                                                            new ConsLoString("Mary ",
                                                                                    new MtLoString())))))))))));
}


  boolean testMerge(Tester t) {
    return t.checkExpect(str2Sort.merge(marySort), new ConsLoString("a ",
            new ConsLoString("Alex ",
                    new ConsLoString("Alex ",
                            new ConsLoString("had ",
                                    new ConsLoString("is ",
                                            new ConsLoString("is ",
                                                    new ConsLoString("lamb.",
                                                            new ConsLoString("little ",
                                                                    new ConsLoString("Mary ",
                                                                            new ConsLoString("My ",
                                                                                    new ConsLoString("my ",
                                                                                            new ConsLoString("name ",
                                                                                                    new ConsLoString("name.",
                                                                                                            new ConsLoString("Perez ",
                                                                                                                    new ConsLoString("Perez, ",
                                                                                                                            new MtLoString()))))))))))))))))
            && t.checkExpect(marySort.merge(marySort), new ConsLoString("a ",
            new ConsLoString("a ",
                    new ConsLoString("had ",
                            new ConsLoString("had ",
                                    new ConsLoString("lamb.",
                                            new ConsLoString("lamb.",
                                                    new ConsLoString("little ",
                                                            new ConsLoString("little ",
                                                                    new ConsLoString("Mary ",
                                                                            new ConsLoString("Mary ",
                                                                                    new MtLoString())))))))))));
  }

  boolean testDoubleList(Tester t) {
    return t.checkExpect(doubles.doubleList(), true);
  }

  boolean testReverse(Tester t) {
    return t.checkExpect(mary.reverse(), new ConsLoString("lamb.",
            new ConsLoString("little ",
                    new ConsLoString("a ",
                            new ConsLoString("had ",
                                    new ConsLoString("Mary ",
                                            new MtLoString()))))))
            && t.checkExpect(str2.reverse(), new ConsLoString("name.",
            new ConsLoString("my ",
                    new ConsLoString("is ",
                            new ConsLoString("Perez ",
                                    new ConsLoString("Alex ",
                                            new ConsLoString("Perez, ",
                                                    new ConsLoString("Alex ",
                                                            new ConsLoString("is ",
                                                                    new ConsLoString("name ",
                                                                            new ConsLoString("My ",
                                                                                    new MtLoString())))))))))));
  }

  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(pal.isPalindromeList(pal.reverse()), true)
            && t.checkExpect(mary.isPalindromeList(pal.reverse()), false);
  }
}