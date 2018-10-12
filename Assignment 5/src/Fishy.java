import java.awt.Color;
import java.util.Random;

import tester.*;
import javalib.funworld.*;
import javalib.worldimages.*;

interface ILoBG {

  //draw image of the fish
  WorldImage drawFish();

  //randomly move around each fish in the list
  ILoBG randomMove(int n);

  //have any of the fish in the list collided with the player?
  boolean collision(AFish that);

  //if collision, remove a background fish from the screen
  ILoBG removeEaten(AFish player);
}

class ConsLoBG implements ILoBG {
  AFish first;
  ILoBG rest;

  ConsLoBG(AFish first, ILoBG rest) {
    this.first = first;
    this.rest = rest;
  }

  //draw image of each fish in the list
  public WorldImage drawFish() {
    return new OverlayImage(this.first.fishImage().movePinholeTo(this.first.center),
            this.rest.drawFish());
  }

  //randomly move each fish in the list around the board
  public ILoBG randomMove(int n) {
    return new ConsLoBG(this.first.randomMove(n), this.rest.randomMove(n));
  }

  //has a fish in the list collided with player?
  public boolean collision(AFish player) {
    return player.collision(this.first)
            || this.rest.collision(player);
  }

  public ILoBG removeEaten(AFish player) {
    if (player.collision(this.first) && player.radius > this.first.radius) {
      return this.rest.removeEaten(player);
    }
    else {
      return new ConsLoBG(this.first, this.rest.removeEaten(player));
    }
  }
}

class MtLoBG implements ILoBG {


  //draw image of the fish
  public WorldImage drawFish() {
    return new EmptyImage();
  }

  //move each fish in the list randomly
  public ILoBG randomMove(int n) {
    return new MtLoBG();
  }

  //have any fish in the list collided with the player?
  public boolean collision(AFish that) {
    return false;
  }

  //removes any fish eaten by the player from the list
  public ILoBG removeEaten(AFish player) {
    return this;
  }
}


//an IFish is one of:
//  -PlayerFish
//  -BackgroundFish
interface IFish {

  //draw image of fish
  WorldImage fishImage();

  //randomly move fish around map
  AFish randomMove(int n);

  //move the fish around with the arrow keys
  AFish moveFish(String ke);

  //has the fish collided with any other fish?
  boolean collision(AFish that);

  //grow the fish if it eats another
  AFish growFish();
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

  //randomly move fish around map
  public abstract AFish randomMove(int n);

  //move the fish around with the arrow keys
  public abstract AFish moveFish(String ke);

  //has the fish collided with any other fish?
  public abstract boolean collision(AFish that);

  //grow the fish if it eats another
  public abstract AFish growFish();
}



class PlayerFish extends AFish {

  PlayerFish(Posn center, int radius, Color col) {
    super(center, radius, col);
  }

  //draws image of the fish
  public WorldImage fishImage() {
    return new CircleImage(this.radius, "solid", this.col);
  }


  //move this PlayerFish 5 pixels in the direction given by the ke or change its
  public PlayerFish moveFish(String ke) {
    if (ke.equals("right")) {
      return new PlayerFish(new Posn(this.center.x + 10, this.center.y),
              this.radius, this.col);
    }
    else if (ke.equals("left")) {
      return new PlayerFish(new Posn(this.center.x - 10, this.center.y),
              this.radius, this.col);
    }
    else if (ke.equals("up")) {
      return new PlayerFish(new Posn(this.center.x, this.center.y - 10),
              this.radius, this.col);
    }
    else if (ke.equals("down")) {
      return new PlayerFish(new Posn(this.center.x, this.center.y + 10),
              this.radius, this.col);
    }
    else {
      return this;
    }
  }

  //move fish randomly
  public AFish randomMove(int n) {
    return this;
  }

  //have fish collided? Uses distance formula to determine
  public boolean collision(AFish that) {
    return (this.radius + that.radius) >= Math.sqrt(Math.pow(
            (double)(this.center.x - that.center.x),2)
            + Math.pow((double)(this.center.y - that.center.y),2));
  }

  //if valid collision, grows the fish to the next size tier
  public AFish growFish() {
    return new PlayerFish(
            new Posn(this.center.x, this.center.y),
            this.radius + 10,
            this.col);
  }
}


class BackgroundFish extends AFish {

  BackgroundFish(Posn center, int radius, Color col) {
    super(center, radius, col);
  }

  //produce a new fish moved by a random distance n pixels away
  public BackgroundFish randomMove(int n) {
    return new BackgroundFish(new Posn(
              this.center.x + this.randomInt(n),
              this.center.y + this.randomInt(n)),
              this.radius,
              this.col);
  }

  //return a random int between 0 and n
  int randomInt(int n) {
    return -n + (new Random().nextInt(2 * n + 1));
  }

  //draw image of the fish
  public WorldImage fishImage() {
    return new CircleImage(this.radius, "outline", this.col);
  }

  //move fish with the arrow keys
  public AFish moveFish(String ke) {
    return this;
  }

  //has this fish collided with another?
  public boolean collision(AFish that) {
    return false;
  }

  //grow fish by if valid collision
  public AFish growFish() {
    return this;
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


  /** Move the Playerfish when the player presses a key */
  public World onKeyEvent(String ke) {
    if (ke.equals("x")) {
      return this.endOfWorld("Goodbye");
    } else {
      return new FishyWorld(this.player.moveFish(ke), this.bg);
    }
  }

  /**
   * On tick move the BG fish in a random direction.
   */
  public World onTick() {
    if (this.bg.collision(this.player)) {
      return new FishyWorld(this.player.growFish(), this.bg.removeEaten(this.player));
    }

    else {
      return new FishyWorld(this.player, this.bg.randomMove(10));
    }
  }
}


class ExamplesFish {

  AFish player1 = new PlayerFish(new Posn(150, 100), 20, Color.RED);
  AFish smallBG1 = new BackgroundFish(new Posn(200, 250), 10, Color.BLUE);
  AFish smallBG2 = new BackgroundFish(new Posn(100, 100), 10, Color.BLUE);
  AFish smallMidBG3 = new BackgroundFish(new Posn(300, 200), 20, Color.PINK);
  AFish smallMidBG4 = new BackgroundFish(new Posn(300, 300), 20, Color.PINK);
  AFish midBG5 = new BackgroundFish(new Posn(400, 300), 30, Color.GREEN);
  AFish midBG6 = new BackgroundFish(new Posn(450, 400), 30, Color.GREEN);
  AFish midLargeBG7 = new BackgroundFish(new Posn(200, 400), 40, Color.BLACK);
  AFish midLargeBG8 = new BackgroundFish(new Posn(400, 400), 40, Color.BLACK);
  AFish largeBG9 = new BackgroundFish(new Posn(600, 600), 50, Color.RED);
  AFish largeBG10 = new BackgroundFish(new Posn(300, 600), 50, Color.RED);

  boolean testCollision(Tester t) {
    return t.checkExpect(player1.collision(largeBG10), false)
            && t.checkExpect(player1.collision(new BackgroundFish(
                    new Posn(150,100),40, Color.RED)), true)
            && t.checkExpect(player1.collision(new BackgroundFish(
                    new Posn(210,100),40, Color.RED)), true);
  }

  boolean testGrowFish(Tester t) {
    return t.checkExpect(player1.growFish(), new PlayerFish(
            new Posn(150, 100), 30, Color.RED));
  }


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
  }
}
