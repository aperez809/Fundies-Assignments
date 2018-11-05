import tester.*;


// runs tests for the buddies problem
public class ExamplesBuddies {

  Person ann;
  Person bob;
  Person cole;
  Person dan;
  Person ed;
  Person fay;
  Person gabi;
  Person hank;
  Person jan;
  Person kim;
  Person len;

  void initBuddies() {
    ann = new Person("Ann", 0.95, 0.8);
    bob = new Person("Bob", .85, .99);
    cole = new Person("Cole", .95, .9);
    dan = new Person("Dan", 1.0, .95);
    ed = new Person("Ed");
    fay = new Person("Fay");
    gabi = new Person("Gabi");
    hank = new Person("Hank");
    jan = new Person("Jan");
    kim = new Person("Kim");
    len = new Person("Len");

    ann.addBuddy(bob);
    ann.addBuddy(cole);
    bob.addBuddy(ann);
    bob.addBuddy(ed);
    bob.addBuddy(hank);
    cole.addBuddy(dan);
    dan.addBuddy(cole);
    ed.addBuddy(fay);
    fay.addBuddy(ed);
    fay.addBuddy(gabi);
    gabi.addBuddy(ed);
    gabi.addBuddy(fay);

    jan.addBuddy(kim);
    jan.addBuddy(len);
    kim.addBuddy(jan);
    kim.addBuddy(len);
    len.addBuddy(jan);
    len.addBuddy(kim);
  }

  //test for addBuddy
  void testAddBuddy(Tester t) {
    initBuddies();
    t.checkExpect(ann.buddies,
            new ConsLoBuddy(cole,
                    new ConsLoBuddy(bob,
                            new MTLoBuddy())));
    ann.addBuddy(ed);
    t.checkExpect(ann.buddies,
            new ConsLoBuddy(ed,
                    new ConsLoBuddy(cole,
                            new ConsLoBuddy(bob,
                                    new MTLoBuddy()))));
  }

  //test for hasDirectBuddy
  boolean testHasDirectBuddy(Tester t) {
    initBuddies();
    return t.checkExpect(ann.hasDirectBuddy(bob), true)
            && t.checkExpect(ann.hasDirectBuddy(len), false)
            && t.checkExpect(len.hasDirectBuddy(hank), false);
  }

  //test for countCommonBuddies
  boolean testCountCommonBuddies(Tester t) {
    initBuddies();
    return t.checkExpect(ann.countCommonBuddies(fay), 0)
            && t.checkExpect(len.countCommonBuddies(kim), 1);
  }

  //test for hasExtendedBuddy
  boolean testHasExtendedBuddy(Tester t) {
    initBuddies();
    return t.checkExpect(ann.hasExtendedBuddy(ed), true)
            && t.checkExpect(jan.hasExtendedBuddy(gabi), false);
  }

  //test for partyCount
  boolean testPartyCount(Tester t) {
    initBuddies();
    return t.checkExpect(ann.partyCount(), 8)
      && t.checkExpect(jan.partyCount(), 3);
  }

  //test for maxLikelihood lol
  boolean testMaxLikelihood(Tester t) {
    initBuddies();
    return t.checkExpect(ann.maxLikelihood(bob), .95 * .99)
            && t.checkInexact(ann.maxLikelihood(ann), 1.0, 0.0001)
            && t.checkExpect(ann.maxLikelihood(dan), .95 * .99 * .85 * .9 * .95 * .95);
  }



}