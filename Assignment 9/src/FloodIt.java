import java.util.ArrayList;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import tester.Tester;

import java.util.Random;





// Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color; //to be determined randomly
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, boolean flooded, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = this.cellColor();
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  //Alternative constructor to save typing
  Cell(int x, int y, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = this.cellColor();
    this.flooded = flooded;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }



  //Creates the image of the cell
  WorldImage cellImage() {
    return new RectangleImage(FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            "solid",
            this.color);
  }

  //Generates random color from the list "colors" to be used by the cell
  Color cellColor() {
    ArrayList<Color> colors = new ArrayList<Color>();

    colors.add(Color.red);
    colors.add(Color.blue);
    colors.add(Color.green);
    colors.add(Color.black);
    colors.add(Color.yellow);

    Random rand = new Random();
    int i = rand.nextInt(4);
    return colors.get(i);
  }
}


class FloodItWorld extends World {

  // Defines an int constant
  static final int BOARD_SIZE = 4;
  static final int CANVAS_SIZE = 400;

  // All the cells of the game
  ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(FloodItWorld.BOARD_SIZE);
  int turnsTaken;

  public FloodItWorld(ArrayList<ArrayList<Cell>> board) {
    super();
    this.board = board;
    this.turnsTaken = 0;
  }

  //draws the board by looping through the list of lists of cells that represent the board.
  public WorldImage drawBoard() {

    WorldImage grid = new EmptyImage();  //vertical cells stacked on each other, final image

    //for each list contained in "this.board"
    for (ArrayList<Cell> arr: this.board) {
      WorldImage row = new EmptyImage();   //horizontal cells being rendered side-to-side
      //for each cell in current list contained in "this.board"
      for (Cell c: arr) {

        //mutate row to add another image on its right
        row = new OverlayOffsetAlign(AlignModeX.CENTER, //AlignModeX.CENTER
                  AlignModeY.MIDDLE,
                  c.cellImage(),
                  row.getWidth() / 2,
                  0,
                  row);
      }

      //adds the new row of images to the grid, underneath the previous row
      grid = new OverlayOffsetAlign(AlignModeX.CENTER,
                AlignModeY.MIDDLE,
                row,
                0,
                grid.getHeight() / 2,
                grid);
    }
    return grid;
  }

  public WorldScene makeScene() {
    WorldScene w = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
    w.placeImageXY(this.drawBoard(), CANVAS_SIZE / 2, CANVAS_SIZE / 2);
    return w;
  }

  public void onKeyEvent(String s) {
    super.onKeyEvent(s);
    if (s.equals('r')) {
      this.makeScene();
    }
  }


  public void onTick() {
    super.onTick();
    if (this.turnsTaken >= BOARD_SIZE) {
      this.endOfWorld("You suck lol");
    }
  }


  public void onMouseClicked(Posn mouse) {
    super.onMouseClicked(mouse);
    this.turnsTaken++;
    for (ArrayList<Cell> arr: this.board) {
      for (Cell c: arr) {
        //if click is inside the given cell
        if (this.cellDetection(mouse, c)) {
          //do something
        }
      }
    }
  }


  public boolean cellDetection(Posn click, Cell c) {
    return Math.hypot(Math.abs(click.x - c.x), Math.abs(click.y - c.x))
            <= FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE;
  }
}

class ArrayUtils {

}

class ExamplesWorld {

  ArrayList<ArrayList<Cell>> arrCell;

  FloodItWorld w;

  //
  void initWorld() {
    arrCell = new ArrayList<ArrayList<Cell>>(FloodItWorld.BOARD_SIZE);
    for (int i = 0; i < FloodItWorld.BOARD_SIZE; i++) {
      arrCell.add(new ArrayList<Cell>(FloodItWorld.BOARD_SIZE));
      for (int j = 0; j < FloodItWorld.BOARD_SIZE; j++) {
        arrCell.get(i).add(
                new Cell((FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE) * j,
                        (FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE) * j,
                        false));
      }
    }

    for (int i = 0; i < FloodItWorld.BOARD_SIZE; i++) {
      for (int j = 0; j < FloodItWorld.BOARD_SIZE; j++) {
        if (notEdgeCell(i, j)) {
          arrCell.get(i).get(j).top = arrCell.get(i - 1).get(j);
          arrCell.get(i).get(j).bottom = arrCell.get(i + 1).get(j);
          arrCell.get(i).get(j).left = arrCell.get(i).get(j - 1);
          arrCell.get(i).get(j).right = arrCell.get(i).get(j + 1);
        }
      }
    }


    w = new FloodItWorld(arrCell);
  }

  boolean notEdgeCell(int i, int j) {
    return !(i == 0 || i == FloodItWorld.BOARD_SIZE - 1
            || j == 0 || j == FloodItWorld.BOARD_SIZE - 1);
  }


  boolean testDrawBoard(Tester t) {
    initWorld();
    return t.checkExpect(w.drawBoard(),
            new WorldScene(FloodItWorld.CANVAS_SIZE,
            FloodItWorld.CANVAS_SIZE));
  }

  boolean testNeighbors(Tester t) {
    initWorld();
    return t.checkExpect(w.board.get(2).get(2).left, w.board.get(2).get(1));
  }

  void testWorld(Tester t) {
    initWorld();
    w.bigBang(FloodItWorld.CANVAS_SIZE,FloodItWorld.CANVAS_SIZE, 0.05);
  }
}
