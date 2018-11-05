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
  Color color;
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
  static final int BOARD_SIZE = 10;
  static final int CANVAS_SIZE = 400;

  // All the cells of the game
  ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(FloodItWorld.BOARD_SIZE);

  public FloodItWorld(ArrayList<ArrayList<Cell>> board) {
    super();
    this.board = board;
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
    w = new FloodItWorld(arrCell);
  }

  boolean testDrawBoard(Tester t) {
    initWorld();
    return t.checkExpect(w.drawBoard(),
            new WorldScene(FloodItWorld.CANVAS_SIZE,
            FloodItWorld.CANVAS_SIZE));
  }

  void testWorld(Tester t) {
    initWorld();
    w.bigBang(FloodItWorld.CANVAS_SIZE,FloodItWorld.CANVAS_SIZE);
  }
}
