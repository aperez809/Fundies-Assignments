
// represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {
    MTLoBuddy() {}

    public boolean contains(Person that) {
        return false;
    }

    public int countCommonBuddies(ILoBuddy that, int acc) {
        return that.countCommonBuddiesMT(this, acc);
    }

  public int countCommonBuddiesCons(ConsLoBuddy that, int acc) {
    return acc;
  }

  public int countCommonBuddiesMT(MTLoBuddy that, int acc) {
    return acc;
  }

  public boolean hasExtendedBuddy(ILoBuddy acc, Person that) {
        return false;
  }

  public int partyCount(ILoBuddy acc, int currentCount) {
    return currentCount;
  }


  public double maxLikelihood(Person that) {
    return 1;
  }
}
