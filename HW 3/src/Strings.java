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
  boolean isSorted();

  //is the list sorted?
  boolean isSortedHelp(String first);

  //reverses the list
  ILoString reverse();

  //add an item to the end of the list
  ILoString append(String word);

  //given two lists, odd indices are filled with items from the first list
  //and even indices are filled with items from second list
  ILoString interleave(ILoString that);

  //add two lists together and return the sorted version of it
  ILoString merge(ILoString that);

  //does this list consist of pairs of the same value?
  boolean isDoubledList();

  //does this list consist of pairs of the same value?
  boolean doubleListHelper(String that);

  //is the list equal when it's forward and backward?
  boolean isPalindromeList();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString(){}


  //is the list equal when it's forward and backward?
  public boolean isPalindromeList() {
    return true;
  }

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  //sort the values in alphabetical order
  public ILoString sort() {
    return this;
  }


  public ILoString insert(String name) {
    return new ConsLoString(name, this);
  }

  //is the list sorted alphabetically?
  public boolean isSorted() {
    return true;
  }

  //is the list sorted alphabetically?
  public boolean isSortedHelp(String that) {
    return true;
  }

  //given 2 lists, combine them with the first list in the odd indices and
  //the second list in the even indices
  public ILoString interleave(ILoString that) {
    return that;
  }

  //given 2 sorted lists, add them together and sort them
  public ILoString merge(ILoString that) {
    return this;
  }

  //does this list consist of pairs of the same value?
  public boolean isDoubledList() {
    return true;
  }

  //does this list consist of pairs of the same value?
  public boolean doubleListHelper(String that) {
    return false;
  }

  //produce the given list in reverse order
  public ILoString reverse() {
    return this;
  }

  //add an item to the end of the lsit
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

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
  TEMPLATE
  FIELDS:
  ... this.first ...         -- String
  ... this.rest ...          -- ILoString

  METHODS
  ... this.combine()         ...      -- String
  ... this.sort()            ...      -- ILoString
  ... this.insert()          ...      -- ILoString
  ... this.isSorted()        ...      -- boolean
  ... this.interleave()      ...      -- ILoString
  ... this.merge()           ...      -- ILoString
  ... this.isDoubledList()      ...      -- boolean
  ... this.isPalindromeList() ...      -- boolean

  METHODS FOR FIELDS
  ... this.first.concat(String) ...        -- String
  ... this.first.compareTo(String) ...     -- int
  ... this.rest.combine() ...              -- String
  ... this.sort()            ...      -- ILoString
  ... this.insert()          ...      -- ILoString
  ... this.isSorted()        ...      -- boolean
  ... this.interleave()      ...      -- ILoString
  ... this.merge()           ...      -- ILoString
  ... this.isDoubledList()      ...      -- boolean
  ... this.isPalindromeList() ...      -- boolean

  */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }


  //sort the list alphabetically
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

  //is the list sorted alphabetically?
  public boolean isSorted() {
    return this.rest.isSortedHelp(this.first);
  }

  //is the list sorted alphabetically?
  public boolean isSortedHelp(String that) {
    return this.first.toLowerCase().compareTo(that.toLowerCase()) >= 0
            && this.rest.isSortedHelp(this.first.toLowerCase());
  }

  //given 2 lists, combine them with the first list in the odd indices and
  //the second list in the even indices
  public ILoString interleave(ILoString that) {
    return new ConsLoString(this.first, that.interleave(this.rest));
  }

  //given 2 sorted lists, add them together and sort them
  public ILoString merge(ILoString that) {
    return this.interleave(that).sort();
  }

  //does this list consist of pairs of the same value?
  public boolean isDoubledList() {
    return this.rest.doubleListHelper(this.first);
  }

  //does this list consist of pairs of the same value?
  public boolean doubleListHelper(String that) {
    return this.first.equals(that)
            && this.rest.isDoubledList();
  }

  //is the list equal when it's forward and backward?
  public boolean isPalindromeList() {
    return this.combine().equals(this.reverse().combine());
  }

  //reverse the order of the list
  public ILoString reverse() {
    return this.rest.reverse().append(this.first);
  }

  //add an item to the end of the list
  public ILoString append(String first) {
    return new ConsLoString(this.first, this.rest.append(first));
  }
}

// to represent examples for lists of strings
class ExamplesStrings {

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
                                                  new ConsLoString("palindrome ",
                                                          new MtLoString()))))))));


  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return t.checkExpect(mary.combine(), "Mary had a little lamb.")
            && t.checkExpect(str2.combine(),
            "My name is Alex Perez, Alex Perez is my name.")
            && t.checkExpect(marySort.combine(), "a had lamb.little Mary ")
            && t.checkExpect(new MtLoString().combine(), "");
  }

  boolean testSort(Tester t) {
    return t.checkExpect(mary.sort(), new ConsLoString("a ", new ConsLoString("had ",
            new ConsLoString("lamb.", new ConsLoString("little ",
                    new ConsLoString("Mary ", new MtLoString()))))))
            && t.checkExpect(str2.sort(), new ConsLoString("Alex ",
            new ConsLoString("Alex ", new ConsLoString("is ",
                    new ConsLoString("is ", new ConsLoString("my ",
                            new ConsLoString("My ", new ConsLoString("name ",
                                    new ConsLoString("name.", new ConsLoString("Perez ",
                                            new ConsLoString("Perez, ",
                                                    new MtLoString())))))))))))
            && t.checkExpect(new MtLoString().sort(), new MtLoString());
  }

  boolean testIsSorted(Tester t) {
    return t.checkExpect(mary.isSorted(), false)
            && t.checkExpect(marySort.isSorted(), true)
            && t.checkExpect(str2.isSorted(), false)
            && t.checkExpect(str2Sort.isSorted(), true)
            && t.checkExpect(new MtLoString().isSorted(), true);
  }

  boolean testInterleave(Tester t) {
    return t.checkExpect(marySort.interleave(marySort), new ConsLoString("a ",
            new ConsLoString("a ", new ConsLoString("had ", new ConsLoString("had ",
                    new ConsLoString("lamb.", new ConsLoString("lamb.",
                            new ConsLoString("little ", new ConsLoString("little ",
                                    new ConsLoString("Mary ", new ConsLoString("Mary ",
                                            new MtLoString())))))))))))
            && t.checkExpect(new MtLoString().interleave(new MtLoString()), new MtLoString());
  }


  boolean testMerge(Tester t) {
    return t.checkExpect(str2Sort.merge(marySort), new ConsLoString("a ", new ConsLoString(
            "Alex ", new ConsLoString("Alex ", new ConsLoString("had ",
            new ConsLoString("is ", new ConsLoString("is ", new ConsLoString(
                    "lamb.", new ConsLoString("little ", new ConsLoString("Mary ",
                    new ConsLoString("My ", new ConsLoString("my ", new ConsLoString(
                            "name ", new ConsLoString("name.",
                            new ConsLoString("Perez ", new ConsLoString("Perez, ",
                                    new MtLoString()))))))))))))))))
            && t.checkExpect(marySort.merge(marySort), new ConsLoString("a ",
            new ConsLoString("a ", new ConsLoString("had ", new ConsLoString("had ",
                    new ConsLoString("lamb.", new ConsLoString("lamb.",
                            new ConsLoString("little ", new ConsLoString("little ",
                                    new ConsLoString("Mary ", new ConsLoString("Mary ",
                                            new MtLoString())))))))))))
            && t.checkExpect(new MtLoString().merge(new MtLoString()), new MtLoString());
  }

  boolean testDoubleList(Tester t) {
    return t.checkExpect(doubles.isDoubledList(), true)
            && t.checkExpect(mary.isDoubledList(), false)
            && t.checkExpect(new MtLoString().isDoubledList(), true);
  }

  boolean testReverse(Tester t) {
    return t.checkExpect(mary.reverse(), new ConsLoString("lamb.",
            new ConsLoString("little ", new ConsLoString("a ",
                    new ConsLoString("had ", new ConsLoString("Mary ",
                            new MtLoString()))))))
            && t.checkExpect(str2.reverse(), new ConsLoString("name.",
            new ConsLoString("my ", new ConsLoString("is ",
                    new ConsLoString("Perez ", new ConsLoString("Alex ",
                            new ConsLoString("Perez, ", new ConsLoString("Alex ",
                                    new ConsLoString("is ", new ConsLoString("name ",
                                            new ConsLoString("My ",
                                                    new MtLoString())))))))))))
            && t.checkExpect(pal.reverse(), pal)
            && t.checkExpect(new MtLoString().reverse(), new MtLoString());
  }

  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(pal.isPalindromeList(), true)
            && t.checkExpect(mary.isPalindromeList(), false)
            && t.checkExpect(str2.isPalindromeList(), false)
            && t.checkExpect(new MtLoString().isPalindromeList(), true);
  }
}