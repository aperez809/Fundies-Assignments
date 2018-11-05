import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  String color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, String color, boolean flooded, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  WorldImage cellImage() {
    return new RectangleImage(FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            "solid",
            Color.decode(this.color));
  }
}


class FloodItWorld extends World {

  // Defines an int constant
  static final int BOARD_SIZE = 4;
  static final int CANVAS_SIZE = 1000;

  // All the cells of the game
  ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(FloodItWorld.BOARD_SIZE);

  public FloodItWorld(ArrayList<ArrayList<Cell>> board) {
    super();
    this.board = board;
  }

  public WorldImage drawBoard() {
    WorldImage row = new EmptyImage();
    WorldImage grid = new EmptyImage();

    //use overlayOffsetAlign, for each new block, get the width of the previously created block and offset by that amount


    for (ArrayList<Cell> arr: this.board) {

      for (Cell c: arr) {
        row = new OverlayOffsetAlign(AlignModeX.CENTER,
                  AlignModeY.MIDDLE,
                  row,
                  row.getWidth() / 2,
                  0,
                  c.cellImage());
        }

      grid = new OverlayOffsetAlign(AlignModeX.CENTER,
                AlignModeY.MIDDLE,
                grid,
                0,
                grid.getHeight() / 2,
                row);
    }
    return grid;
  }

  public WorldScene makeScene() {
/*    return new WorldScene(CANVAS_SIZE, CANVAS_SIZE)
            .placeImageXY(this.drawBoard(), CANVAS_SIZE / 2, CANVAS_SIZE / 2);*/
    return this
            .getEmptyScene()
            .placeImageXY(this.drawBoard(), CANVAS_SIZE / 2, CANVAS_SIZE / 2);
  }
}