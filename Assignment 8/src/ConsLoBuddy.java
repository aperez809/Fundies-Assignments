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
    return this.first.equals(that)
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

  // returns the number of people who will show up at the party
  // given by this person
  public int partyCount(ILoBuddy acc, int currentCount) {
    if (!acc.contains(this.first)) {
      return this.first.partyCountHelp(new ConsLoBuddy(this.first, acc), 1)
              + this.rest.partyCount(new ConsLoBuddy(this.first, acc), currentCount);
    }
    else {
      return this.rest.partyCount(acc, currentCount);
    }
  }

  public double maxLikelihood(Person that) {
    if (this.first.equals(that)) {
      return this.first.hearing * this.first.maxLikelihood(that);
      //return this.first.hearing * this.first.maxLikelihood(that);
    }
    else {
      return Math.max(this.first.hearing * this.first.maxLikelihood(that),
              this.rest.maxLikelihood(that));
      //return this.rest.maxLikelihood(that);
    }


  }
}
