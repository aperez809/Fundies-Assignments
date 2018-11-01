
// represents a list of Person's buddies
interface ILoBuddy {

  // contains function, true if that is contained
  boolean contains(Person that);

  // counts common buddies in a list using an accumulator
  int countCommonBuddies(ILoBuddy that, int acc);

  // true if extended buddy is present in list of buddies
  boolean hasExtendedBuddy(ILoBuddy acc, Person that);

  // counts common buddies in a cons list of buddies
  int countCommonBuddiesCons(ConsLoBuddy that, int acc);

  // counts common buddies in an empty list of buddies
  int countCommonBuddiesMT(MTLoBuddy that, int acc);

  //returns the number of people who will show up at the party
  // given by this person
  int partyCount(ILoBuddy acc, int currentCount);

  // computes the max likely hood that that person has an extended buddy.
  //Returns a double
  double maxLikelihood(Person that);
}