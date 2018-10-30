
// represents a list of Person's buddies
interface ILoBuddy {

  boolean contains(Person that);

  int countCommonBuddies(ILoBuddy that, int acc);

  boolean hasExtendedBuddy(ILoBuddy acc, Person that);

  int countCommonBuddiesCons(ConsLoBuddy that, int acc);

  int countCommonBuddiesMT(MTLoBuddy that, int acc);

  int partyCount(ILoBuddy acc, int currentCount);

  double maxLikelihood(Person that);
}
