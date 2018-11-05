// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

  Person first;
  ILoBuddy rest;

  ConsLoBuddy(Person first, ILoBuddy rest) {
    this.first = first;
    this.rest = rest;
  }

  // true if person is in the list of buddies
  public boolean contains(Person that) {
    return new SamePersonPred().compare(this.first, that) == 0
            || this.rest.contains(that);
  }

  //counts the common buddies using an accumulator and double dispatch
  //returns the number of buddies in common
  public int countCommonBuddies(ILoBuddy that, int acc) {
    return that.countCommonBuddiesCons(this, acc);
  }

  //counts the common buddies in a cons list also using an
  //accumulator
  public int countCommonBuddiesCons(ConsLoBuddy that, int acc) {
    if (that.contains(this.first)) {
      return this.rest.countCommonBuddies(that, acc + 1);
    }
    else {
      return this.rest.countCommonBuddies(that, acc);
    }
  }

  //counts the common buddies in an empty list. returns the accumulator
  public int countCommonBuddiesMT(MTLoBuddy that, int acc) {
    return acc;
  }

  //true if extended buddy is present in the list of buddies
  public boolean hasExtendedBuddy(ILoBuddy acc, Person that) {
    if (!acc.contains(this.first)) {
      return this.first.hasExtendedBuddyHelp(new ConsLoBuddy(this.first, acc), that)
              || this.rest.hasExtendedBuddy(new ConsLoBuddy(this.first, acc), that);
    }
    else {
      return this.rest.hasExtendedBuddy(acc, that);
    }
  }

  // returns the list of people who will show up at the party
  // given by this person
  public ILoBuddy partyCount(ILoBuddy acc) {
    if (!acc.contains(this.first)) {
      return this.rest.partyCount(this.first.partyCountHelp(new ConsLoBuddy(this.first, acc)));
    }
    else {
      return this.rest.partyCount(acc);
    }
  }

  //gets the max likelihood that a message will be received correctly
  public double maxLikelihood(ILoBuddy acc, Person that) {
    if (new SamePersonPred().compare(this.first, that) == 0) {
      return that.hearing;
      //return Math.max(this.first.hearing * this.first.maxLikelihood(that), 0);
      //return this.first.hearing * this.first.maxLikelihood(that);
    }
    else if (!acc.contains(this.first)) {
      return Math.max(this.first.hearing * this.first.maxLikelihood(that),
              this.rest.maxLikelihood(acc, that));
      //return this.rest.maxLikelihood(that);
    }
    else {
      return that.hearing;
    }
  }

  //gets the length of a list
  public int length() {
    return 1 + this.rest.length();
  }
}
