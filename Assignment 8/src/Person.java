//interface used for comparing two items
interface IComparator<T> {
  int compare(T t1, T t2);
}

// class for the same person predicate
class SamePersonPred implements IComparator<Person> {
  public int compare(Person p1, Person p2) {
    return p1.username.compareTo(p2.username);
  }
}


// represents a Person with a user name and a list of buddies
class Person {

  String username;
  ILoBuddy buddies;
  double dictation;
  double hearing;

  Person(String username, double dictation, double hearing) {
    this.username = username;
    this.buddies = new MTLoBuddy();
    this.dictation = dictation;
    this.hearing = hearing;
  }

  Person(String username) {
    this.username = username;
    this.buddies = new MTLoBuddy();
    this.dictation = 1;
    this.hearing = 1;
  }

  // returns true if this Person has that as a direct buddy
  boolean hasDirectBuddy(Person that) {
    return this.buddies.contains(that);
  }

  // returns the number of people who will show up at the party
  // given by this person
  int partyCount() {
    return this.partyCountHelp(new ConsLoBuddy(this, new MTLoBuddy())).length();
  }

  //helper for partyCount so we can use accumulator
  ILoBuddy partyCountHelp(ILoBuddy acc) {
    return this.buddies.partyCount(acc);
  }

  // returns the number of people that are direct buddies
  // of both this and that person
  int countCommonBuddies(Person that) {
    return this.buddies.countCommonBuddies(that.buddies, 0);
  }

  // will the given person be invited to a party
  // organized by this person?
  boolean hasExtendedBuddy(Person that) {
    return this.hasExtendedBuddyHelp(new ConsLoBuddy(this, new MTLoBuddy()), that);
  }

  // returns true if there is an extended buddy in the list of buddies
  boolean hasExtendedBuddyHelp(ILoBuddy acc, Person that) {
    return this.buddies.contains(that)
            || this.buddies.hasExtendedBuddy(acc, that);
  }

  // EFFECT:
  // Change this person's buddy list so that it includes the given person
  void addBuddy(Person buddy) {
    if (!this.buddies.contains(buddy)) {
      this.buddies = new ConsLoBuddy(buddy, this.buddies);
    }
  }


  //gets likelihood that a message will be received
  double maxLikelihood(Person that) {
    if (!this.hasExtendedBuddy(that)) {
      return 0.0;
    }
    else if (new SamePersonPred().compare(this, that) == 0) {
      return 1.0;
    }
    else {
      return this.dictation * this.maxLikelihood(that);
    }
  }
}
