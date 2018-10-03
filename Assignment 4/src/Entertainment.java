import tester.*;

interface IEntertainment {

  //compute the total price of this Entertainment
  double totalPrice();

  //computes the minutes of entertainment of this IEntertainment
  int duration();

  //produce a String that shows the name and price of this IEntertainment
  String format();

  //is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  //is this mag the same as that mag?
  boolean sameMag(Magazine that);

  //is this tv the same as that tv?
  boolean sameTV(TVSeries that);

  //is this podcast the same as that podcast?
  boolean samePodcast(Podcast that);
}

abstract class AEntertainment implements IEntertainment {
  String name;
  double price;
  int installments;

  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  public double totalPrice() {
    return this.price * this.installments;
  }

  public int duration() {
    return 50 * installments;
  }

  public String format() {
    return this.name + ", " + Double.toString(this.price) + ".";
  }

  public abstract boolean sameEntertainment(IEntertainment that);
}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  /*Template

  Fields:
  this.name: String
  this.price: double
  this.installments: int
  this.genre: String
  this.pages: int

  Methods:
  this.totalPrice: double
  this.duration: int
  this.format: String
  this.sameEntertainment: boolean
  this.sameMag: boolean
  this.sameTV: boolean
  this.samePodcast: boolean

   */

  //is this magazine the same as that magazine?
  public boolean sameMag(Magazine that) {
    return this.name.equals(that.name)
            && this.price == that.price
            && this.genre.equals(that.genre)
            && this.pages == that.pages
            && this.installments == that.installments;
  }

  //is this magazine the same as that TV series?
  public boolean sameTV(TVSeries that) {
    return false;
  }

  //is this magazine the same as that Podcast?
  public boolean samePodcast(Podcast that) {
    return false;
  }

  //computes the minutes of entertainment of this Magazine, (includes all installments)
  public int duration() {
    return (this.pages * 5) * this.installments;
  }

  //is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMag(this);
  }
}

class TVSeries extends AEntertainment {
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }


  /*Template

  Fields:
  this.name: String
  this.price: double
  this.installments: int
  this.genre: String
  this.pages: int

  Methods:
  this.totalPrice: double
  this.duration: int
  this.format: String
  this.sameEntertainment: boolean
  this.sameMag: boolean
  this.sameTV: boolean
  this.samePodcast: boolean

   */

  //is this TV series the same as that magazine?
  public boolean sameMag(Magazine that) {
    return false;
  }

  //is this TV series the same as that TV series?
  public boolean sameTV(TVSeries that) {
    return this.name.equals(that.name)
            && this.price == that.price
            && this.installments == that.installments
            && this.corporation.equals(that.corporation);
  }

  //is this TV series the same as that podcast?
  public boolean samePodcast(Podcast that) {
    return false;
  }

  //is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTV(this);
  }
}

class Podcast extends AEntertainment {

  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }

  /*Template

  Fields:
  this.name: String
  this.price: double
  this.installments: int
  this.genre: String
  this.pages: int

  Methods:
  this.totalPrice: double
  this.duration: int
  this.format: String
  this.sameEntertainment: boolean
  this.sameMag: boolean
  this.sameTV: boolean
  this.samePodcast: boolean

   */

  //is this Podcast the same as that magazine?
  public boolean sameMag(Magazine that) {
    return false;
  }

  //is this podcast the same as that TV series?
  public boolean sameTV(TVSeries that) {
    return false;
  }

  //is this podcast the same as that podcast?
  public boolean samePodcast(Podcast that) {
    return this.name.equals(that.name)
            && this.price == that.price
            && this.installments == that.installments;
  }

  //is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }
}


class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment vogue = new Magazine("Vogue", 9, "Fashion", 40, 15);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment theOffice = new TVSeries("The Office", 0.0, 5, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment npr = new Podcast("National public radio", 0.0, 6);

  //testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
            && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
            && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
            && t.checkInexact(this.vogue.totalPrice(), 135.0, .0001)
            && t.checkInexact(this.theOffice.totalPrice(), 0.0, .0001)
            && t.checkInexact(this.npr.totalPrice(), 0.0, 0.0001);
  }

  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 3600)
            && t.checkExpect(this.houseOfCards.duration(), 650)
            && t.checkExpect(this.serial.duration(), 400)
            && t.checkExpect(this.vogue.duration(), 3000)
            && t.checkExpect(this.theOffice.duration(), 250)
            && t.checkExpect(this.npr.duration(), 300);
  }

  boolean testFormat(Tester t) {
    return t.checkExpect(rollingStone.format(), "Rolling Stone, 2.55.")
            && t.checkExpect(houseOfCards.format(), "House of Cards, 5.25.")
            && t.checkExpect(serial.format(), "Serial, 0.0.")
            && t.checkExpect(vogue.format(), "Vogue, 9.0.")
            && t.checkExpect(theOffice.format(), "The Office, 0.0.")
            && t.checkExpect(npr.format(), "National public radio, 0.0.");
  }

  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(rollingStone.sameEntertainment(rollingStone), true)
            && t.checkExpect(houseOfCards.sameEntertainment(serial), false)
            && t.checkExpect(serial.sameEntertainment(npr), false)
            && t.checkExpect(vogue.sameEntertainment(vogue), true)
            && t.checkExpect(vogue.sameEntertainment(npr), false)
            && t.checkExpect(theOffice.sameEntertainment(vogue), false)
            && t.checkExpect(serial.sameEntertainment(npr), false);
  }
}