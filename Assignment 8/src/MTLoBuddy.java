
// represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {
  MTLoBuddy() {}

  //sees if list contains buddy. returns false since empty
  public boolean contains(Person that) {
    return false;
  }

  //counts the common buddies in list. there are none in an empty list.
  public int countCommonBuddies(ILoBuddy that, int acc) {
    return that.countCommonBuddiesMT(this, acc);
  }

  //counts the common buddies in list for cons, there are none so it returns
  //the accumulator
  public int countCommonBuddiesCons(ConsLoBuddy that, int acc) {
    return acc;
  }

  //counts the common buddies in an MTlist. there are none so it returns
  // the accumulator
  public int countCommonBuddiesMT(MTLoBuddy that, int acc) {
    return acc;
  }

  //true if there is an extended buddy in list. returns false since empty
  public boolean hasExtendedBuddy(ILoBuddy acc, Person that) {
    return false;
  }

  // returns the list of people who will show up at the party
  // given by this person
  public ILoBuddy partyCount(ILoBuddy acc) {
    return acc;
  }

  //returns the max likely hood that the message will be received correctly.
  // returns 1 since empty list.
  public double maxLikelihood(ILoBuddy acc, Person that) {
    return 1;
  }

  //gets length of a list
  public int length() {
    return 0;
  }
}