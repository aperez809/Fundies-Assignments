import java.awt.Color;
import java.util.Random;

import tester.*;
import javalib.funworld.*;
import javalib.worldimages.*;

interface ILoBG {
  WorldImage drawFish();
  ILoBG randomMove(int n);
  boolean collision(AFish that);
}

class ConsLoBG implements ILoBG {
  AFish first;
  ILoBG rest;

  ConsLoBG(AFish first, ILoBG rest) {
    this.first = first;
    this.rest = rest;
  }

  public WorldImage drawFish() {
    return new OverlayImage(this.first.fishImage().movePinholeTo(this.first.center),
            this.rest.drawFish());
  }

  @Override
  public ILoBG randomMove(int n) {
    return new ConsLoBG(this.first.randomMove(n), this.rest.randomMove(n));
  }

  @Override
  public boolean collision(AFish that) {
    return (that.collision(this.first) && this.first.radius <= that.radius)
            || this.rest.collision(that);
  }
}

class MtLoBG implements ILoBG {

  public WorldImage drawFish() {
    return new EmptyImage();
  }

  @Override
  public ILoBG randomMove(int n) {
    return new MtLoBG();
  }

  @Override
  public boolean collision(AFish that) {
    return false;
  }
}


//an IFish is one of:
//  -PlayerFish
//  -BackgroundFish
interface IFish {

  //draw image of fish
  WorldImage fishImage();
}

abstract class AFish implements IFish {
  Posn center;
  int radius;
  Color col;

  AFish(Posn center, int radius, Color col) {
    this.center = center;
    this.radius = radius;
    this.col = col;
  }

  public abstract AFish randomMove(int n);

  public abstract AFish moveFish(String ke);

  public abstract boolean collision(AFish that);
}



class PlayerFish extends AFish {

  PlayerFish(Posn center, int radius, Color col) {
    super(center, radius, col);
  }

  public WorldImage fishImage() {
    return new CircleImage(this.radius, "solid", this.col);
  }


  //move this PlayerFish 5 pixels in the direction given by the ke or change its
  //color to Green, Red or Yellow
  public PlayerFish moveFish(String ke) {
    if (ke.equals("right")) {
      return new PlayerFish(new Posn(this.center.x + 5, this.center.y),
              this.radius, this.col);
    } else if (ke.equals("left")) {
      return new PlayerFish(new Posn(this.center.x - 5, this.center.y),
              this.radius, this.col);
    } else if (ke.equals("up")) {
      return new PlayerFish(new Posn(this.center.x, this.center.y - 5),
              this.radius, this.col);
    } else if (ke.equals("down")) {
      return new PlayerFish(new Posn(this.center.x, this.center.y + 5),
              this.radius, this.col);
    } else {
      return this;
    }
  }

  public AFish randomMove(int n) {
    return this;
  }

  public boolean collision(AFish that) {
    return this.center.x > that.center.x - this.radius  && this.center.x < that.center.x + this.radius
            && this.center.y >= that.center.y - this.radius
            && this.center.y <= that.center.y + this.radius;
  }
}


class BackgroundFish extends AFish {

  BackgroundFish(Posn center, int radius, Color col) {
    super(center, radius, col);
  }

  /** produce a new blob moved by a random distance < n pixels */
  public BackgroundFish randomMove(int n) {
    return new BackgroundFish(new Posn(
            this.center.x + this.randomInt(n),
            this.center.y),
            this.radius,
            this.col);
  }

  public WorldImage fishImage() {
    return new CircleImage(this.radius, "outline", this.col);
  }

  int randomInt(int n) {
    return -n + (new Random().nextInt(2 * n + 1));
  }

  @Override
  public AFish moveFish(String ke) {
    return this;
  }

  public boolean collision(AFish that) {
    return false;
  }
}

class FishyWorld extends World {
  int width = 1000;
  int height = 1000;
  AFish player;
  ILoBG bg;

  public FishyWorld(AFish player, ILoBG bg) {
    super();
    this.player = player;
    this.bg = bg;
  }

  public WorldScene makeScene() {
    return this.getEmptyScene()
            .placeImageXY(this.player.fishImage(), this.player.center.x, this.player.center.y)
            .placeImageXY(this.bg.drawFish(), 500, 500);
  }

  public WorldEnd worldEnds() {
    if (this.bg.collision(this.player)) {
      return new WorldEnd(true, this.makeScene().placeImageXY(
              new TextImage("lol u suck", 40, FontStyle.BOLD_ITALIC, Color.red),
              500, 500));
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  public WorldScene lastScene(String s) {
      return super.lastScene(s);
    }


  /** Move the Blob when the player presses a key */
  public World onKeyEvent(String ke) {
    if (ke.equals("x"))
      return this.endOfWorld("Goodbye");
    else
      return new FishyWorld(this.player.moveFish(ke), this.bg);
  }

  /**
   * On tick move the Blob in a random direction.
   */
  public World onTick() {
    return new FishyWorld(this.player, this.bg.randomMove(5));
  }
}


class ExamplesFish {
/*  AFish player1 = new PlayerFish(new Posn(150, 100), 20, Color.RED);
  AFish smallBG1 = new BackgroundFish(new Posn(50, 50), 10, Color.BLUE);
  AFish smallBG2 = new BackgroundFish(new Posn(100, 100), 10, Color.BLUE);
  AFish smallMidBG3 = new BackgroundFish(new Posn(300, 200), 20, Color.PINK);
  AFish smallMidBG4 = new BackgroundFish(new Posn(300, 300), 20, Color.PINK);
  AFish midBG5 = new BackgroundFish(new Posn(400, 300), 30, Color.GREEN);
  AFish midBG6 = new BackgroundFish(new Posn(450, 400), 30, Color.GREEN);
  AFish midLargeBG7 = new BackgroundFish(new Posn(40, 400), 40, Color.BLACK);
  AFish midLargeBG8 = new BackgroundFish(new Posn(400, 400), 40, Color.BLACK);
  AFish largeBG9 = new BackgroundFish(new Posn(600, 600), 50, Color.RED);
  AFish largeBG10 = new BackgroundFish(new Posn(100, 600), 50, Color.RED);*/


  public static void main(String[] argv) {

    AFish player1 = new PlayerFish(new Posn(500, 500), 20, Color.RED);
    AFish smallBG1 = new BackgroundFish(new Posn(50, 50), 10, Color.BLUE);
    AFish smallBG2 = new BackgroundFish(new Posn(100, 100), 10, Color.BLUE);
    AFish smallMidBG3 = new BackgroundFish(new Posn(300, 200), 20, Color.PINK);
    AFish smallMidBG4 = new BackgroundFish(new Posn(300, 300), 20, Color.PINK);
    AFish midBG5 = new BackgroundFish(new Posn(400, 300), 30, Color.GREEN);
    AFish midBG6 = new BackgroundFish(new Posn(450, 400), 30, Color.GREEN);
    AFish midLargeBG7 = new BackgroundFish(new Posn(40, 400), 40, Color.BLACK);
    AFish midLargeBG8 = new BackgroundFish(new Posn(400, 400), 40, Color.BLACK);
    AFish largeBG9 = new BackgroundFish(new Posn(600, 600), 50, Color.RED);
    AFish largeBG10 = new BackgroundFish(new Posn(100, 600), 50, Color.RED);

    // run the tests - showing only the failed test results
    ExamplesFish ef = new ExamplesFish();
    Tester.runReport(ef, false, false);

    // run the game
    FishyWorld w = new FishyWorld(player1, new ConsLoBG(smallBG1, new ConsLoBG(
            smallBG2, new ConsLoBG(smallMidBG3, new ConsLoBG(smallMidBG4, new ConsLoBG(
            midBG5, new ConsLoBG(midBG6, new ConsLoBG(midLargeBG7, new ConsLoBG(
            midLargeBG8, new ConsLoBG(largeBG9, new ConsLoBG(largeBG10,
            new MtLoBG())))))))))) {
    });
    w.bigBang(1000, 1000, 0.3);

        /*
         * Canvas c = new Canvas(200, 300); c.show();
         * System.out.println(" let's see: \n\n" +
         * Printer.produceString(w.makeImage())); c.drawImage(new
         * OverlayImages(new CircleImage(new Posn(50, 50), 20, Color.RED), new
         * RectangleImage(new Posn(20, 30), 40, 20, Color.BLUE)));
         */
  }
}
