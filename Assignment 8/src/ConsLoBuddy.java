// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

  Person first;
  ILoBuddy rest;

  ConsLoBuddy(Person first, ILoBuddy rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean contains(Person that) {
    return this.first.equals(that)
            || this.rest.contains(that);
  }

  public int countCommonBuddies(ILoBuddy that, int acc) {
    return that.countCommonBuddiesCons(this, acc);
  }

  public int countCommonBuddiesCons(ConsLoBuddy that, int acc) {
    if (that.contains(this.first)) {
      return this.rest.countCommonBuddies(that, acc + 1);
    }
    else {
      return this.rest.countCommonBuddies(that, acc);
    }
  }

  public int countCommonBuddiesMT(MTLoBuddy that, int acc) {
    return acc;
  }


  public boolean hasExtendedBuddy(ILoBuddy acc, Person that) {
    if (!acc.contains(this.first)) {
      return this.first.hasExtendedBuddyHelp(new ConsLoBuddy(this.first, acc), that)
              || this.rest.hasExtendedBuddy(new ConsLoBuddy(this.first, acc), that);
    }
    else {
      return this.rest.hasExtendedBuddy(acc, that);
    }
  }

  public int partyCount(ILoBuddy acc, int currentCount) {
    if (!acc.contains(this.first)) {
      System.out.println(this.first);
      return this.first.partyCountHelp(new ConsLoBuddy(this.first, acc), 1)
              + this.rest.partyCount(new ConsLoBuddy(this.first, acc), currentCount);
    }
    else {
      return this.rest.partyCount(acc, currentCount);
    }
  }

  public double maxLikelihood(Person that) {
    if (this.first.hasDirectBuddy(that)) {
      return this.first.getScore(that);
    }
    else if (this.first.hasExtendedBuddy(that)) {
      return this.first.maxLikelihood(that);
    }
    else {
      return this.rest.maxLikelihood(that);
    }
  }
}
