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
    return /*new OverlayImage(
            new TextImage(
                    Integer.toString(this.x)
                            + ", "
                            + Integer.toString(this.y)
                            + " "
                            + this.flooded, Color.WHITE),*/
            new RectangleImage(
                    FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
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
    colors.add(Color.pink);
    colors.add(Color.CYAN);

    Random rand = new Random();
    int i = rand.nextInt(colors.size());
    return colors.get(i);
  }

  //For use in testing cellColor method which takes a seed to produce a certain color
  Color cellColor(int seed) {
    ArrayList<Color> colors = new ArrayList<Color>();

    colors.add(Color.red);
    colors.add(Color.blue);
    colors.add(Color.green);
    colors.add(Color.black);
    colors.add(Color.yellow);
    colors.add(Color.pink);
    colors.add(Color.CYAN);

    Random rand = new Random(seed);
    int i = rand.nextInt(colors.size());
    return colors.get(i);
  }


  public void floodNeighbor(Cell neighbor, Color color) {
    if (neighbor == null) {
      return;
    }
    else if (neighbor.color == color) {
      neighbor.flooded = true;
    }
  }
}


class FloodItWorld extends World {

  // Defines an int constant
  static final int BOARD_SIZE = 7;
  static final int CANVAS_SIZE = 700;

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
    return new ArrayUtils().drawBoard(this.board);
  }

  public WorldScene makeScene() {
    WorldScene w = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
    w.placeImageXY(this.drawBoard(), CANVAS_SIZE / 2, CANVAS_SIZE / 2);
    return w;
  }


  //where go?
  public void onKeyEvent(String ke) {
    super.onKeyEvent(ke);
    if (ke.equals("r")) {
      this.board = new ArrayUtils().makeBoard(BOARD_SIZE);
      new ArrayUtils().assignNeighbors(this.board);
      this.turnsTaken = 0;
    }
  }


  public void onTick() {
    super.onTick();
    if (this.turnsTaken == BOARD_SIZE * 10) {
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
          this.floodWorld(c);
        }
      }
    }
  }

  void floodWorld(Cell clicked) {
    for (ArrayList<Cell> arr : this.board) {
      for (Cell c : arr) {
        if (c.flooded) {
          c.color = clicked.color;
          c.floodNeighbor(c.left, c.color);
          c.floodNeighbor(c.right, c.color);
          c.floodNeighbor(c.top, c.color);
          c.floodNeighbor(c.bottom, c.color);
        }
      }
    }
  }


  public boolean cellDetection(Posn click, Cell c) {
    return click.x >= c.x
            && click.y >= c.y
            && click.x <= c.x + CANVAS_SIZE / BOARD_SIZE
            && click.y <= c.y + CANVAS_SIZE / BOARD_SIZE;
  }
}

class ArrayUtils {

  ArrayList<ArrayList<Cell>> makeBoard(int boardSize) {
    ArrayList<ArrayList<Cell>> arrCell = new ArrayList<ArrayList<Cell>>(boardSize);

    for (int i = 0; i < boardSize; i++) {
      arrCell.add(new ArrayList<Cell>(boardSize));

      for (int j = 0; j < boardSize; j++) {
        if (i == 0 && j == 0) {
          arrCell.get(i).add(
                  new Cell((FloodItWorld.CANVAS_SIZE / boardSize) * j,
                          (FloodItWorld.CANVAS_SIZE / boardSize) * i,
                          true));
        } else {
          arrCell.get(i).add(
                  new Cell((FloodItWorld.CANVAS_SIZE / boardSize) * j,
                          (FloodItWorld.CANVAS_SIZE / boardSize) * i,
                          false));
        }
      }
    }
    return arrCell;
  }

  WorldImage drawBoard(ArrayList<ArrayList<Cell>> board) {
    WorldImage grid = new EmptyImage();  //vertical cells stacked on each other, final image

    //for each list contained in "this.board"
    for (ArrayList<Cell> arr: board) {
      WorldImage row = new EmptyImage();   //horizontal cells being rendered side-to-side
      //for each cell in current list contained in "this.board"
      for (Cell c: arr) {

        //mutate row to add another image on its right
        row = new BesideImage(row, c.cellImage());
      }

      //adds the new row of images to the grid, underneath the previous row
      grid = new AboveImage(grid, row);
    }
    return grid;
  }

  void assignNeighbors(ArrayList<ArrayList<Cell>> board) {
    for (int i = 0; i < board.size(); i++) {
      for (int j = 0; j < board.size(); j++) {
        board.get(i).get(j).top = this.findNeighbor(board, i - 1, j);
        board.get(i).get(j).bottom = this.findNeighbor(board, i + 1, j);
        board.get(i).get(j).left = this.findNeighbor(board, i, j - 1);
        board.get(i).get(j).right = this.findNeighbor(board, i, j + 1);
      }
    }
  }

  Cell findNeighbor(ArrayList<ArrayList<Cell>> arr, int i, int j) {
    if (i < 0 || i >= arr.size()
            || j < 0 || j >= arr.size()) {
      return null;
    }
    else {
      return arr.get(i).get(j);
    }

  }
}

class ExamplesWorld {

  ArrayList<ArrayList<Cell>> arrCell1;
  ArrayList<ArrayList<Cell>> arrCell2;
  ArrayList<ArrayList<Cell>> arrCell3;


  FloodItWorld w1;
  FloodItWorld w2;
  FloodItWorld w3;

  Cell c1;
  Cell c2;
  Cell c3;

  //
  void initWorld() {
    arrCell1 = new ArrayUtils().makeBoard(FloodItWorld.BOARD_SIZE);
    arrCell2 = new ArrayUtils().makeBoard(2);
    arrCell3 = new ArrayUtils().makeBoard(16);

    new ArrayUtils().assignNeighbors(arrCell1);
    new ArrayUtils().assignNeighbors(arrCell2);
    new ArrayUtils().assignNeighbors(arrCell3);

    w1 = new FloodItWorld(arrCell1);
    w2 = new FloodItWorld(arrCell2);
    w3 = new FloodItWorld(arrCell3);

    c1 = new Cell(100, 100, false);
    c2 = new Cell(200, 200, false);
    c3 = new Cell(300, 300, false);
  }

  //tests that cellColor works
  void testCellColor(Tester t) {
    initWorld();
    c1.color = c1.cellColor(2);
    t.checkExpect(c1.color, Color.green);
    c1.color = c1.cellColor(109851);
    t.checkExpect(c1.color, Color.red);
  }

  boolean testCellImage(Tester t) {
    initWorld();
    c1.color = c1.cellColor(3);
    c2.color = c2.cellColor(109851);
    return t.checkExpect(c1.cellImage(), new RectangleImage(
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            "solid",
            Color.green))
            && t.checkExpect(c2.cellImage(), new RectangleImage(
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            "solid",
            Color.red));
  }

  //tests that onKey returns a new board
  boolean testOnKey(Tester t) {
    initWorld();
    FloodItWorld old = w1;
    w1.onKeyEvent("r");
    return !t.checkExpect(w1.board, old.board);
  }

  //test that the board is created the same every time based on input
  //only difference is randomly generated color of cells
  //x, y, flooded, and neighbors are identical to examples
  /*boolean testMakeBoard(Tester t) {
    initWorld();
    return t.checkExpect(new ArrayUtils().makeBoard(FloodItWorld.BOARD_SIZE), w1.board)
            && t.checkExpect(new ArrayUtils().makeBoard(2), w2.board)
            && t.checkExpect(new ArrayUtils().makeBoard(16), w3.board);

  }*/

  boolean testAssignNeighbors(Tester t) {
    initWorld();
    return t.checkExpect(w1.board.get(2).get(2).left, w1.board.get(2).get(1))
            && t.checkExpect(w1.board.get(0).get(0).left, null);
  }

  boolean testFindNeighbor(Tester t) {
    initWorld();
    return t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, -1, 0), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, 0, -1), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, 5, 1), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, 1, 5), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, 2, 0),
            arrCell1.get(2).get(0));
  }

  boolean testCellDetection(Tester t) {
    initWorld();
    return t.checkExpect(w1.cellDetection(new Posn(100,100), w1.board.get(0).get(0)),
            false);
  }

  public static void main(String[] argv) {

    // run the tests - showing only the failed test results
    ExamplesWorld w = new ExamplesWorld();

    // run the game
    w.initWorld();
    FloodItWorld world = new FloodItWorld(w.arrCell1);
    world.bigBang(FloodItWorld.CANVAS_SIZE, FloodItWorld.CANVAS_SIZE, 0.3);
  }
}
