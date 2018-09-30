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
  boolean sameMag(Magazine that);
  boolean sameTV(TVSeries that);
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
    return 50;
  }

  public String format() {
    return this.name + ", " + Double.toString(this.price) + ".";
  }

  public abstract boolean sameEntertainment(IEntertainment that);
}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, int installments, String genre, int pages) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  public boolean sameMag(Magazine that) {
    return this.name.equals(that.name)
            && this.price == that.price
            && this.genre.equals(that.genre)
            && this.pages == that.pages
            && this.installments == that.installments;
  }

  @Override
  public boolean sameTV(TVSeries that) {
    return false;
  }

  @Override
  public boolean samePodcast(Podcast that) {
    return false;
  }

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  //computes the minutes of entertainment of this Magazine, (includes all installments)
  public int duration() {
    return this.pages * 5;
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

  @Override
  public boolean sameMag(Magazine that) {
    return false;
  }

  @Override
  public boolean sameTV(TVSeries that) {
    return this.name.equals(that.name)
            && this.price == that.price
            && this.installments == that.installments
            && this.corporation.equals(that.corporation);
  }

  @Override
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

  public boolean sameMag(Magazine that) {
    return false;
  }

  public boolean sameTV(TVSeries that) {
    return false;
  }

  @Override
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
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);

  //testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55*12, .0001)
            && t.checkInexact(this.houseOfCards.totalPrice(), 5.25*13, .0001)
            && t.checkInexact(this.serial.totalPrice(), 0.0, .0001);
  }

  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 300)
            && t.checkExpect(this.houseOfCards.duration(), 50)
            && t.checkExpect(this.serial.duration(), 50);
  }

  boolean testFormat(Tester t) {
    return t.checkExpect(rollingStone.format(), "Rolling Stone, 2.55.")
            && t.checkExpect(houseOfCards.format(), "House of Cards, 5.25.")
            && t.checkExpect(serial.format(), "Serial, 0.0.");
  }

  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(rollingStone.sameEntertainment(rollingStone), true)
            && t.checkExpect(houseOfCards.sameEntertainment(serial), false);
  }
}