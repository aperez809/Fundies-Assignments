/*
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

  //Default Constructor
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

  //Alternative constructor to save typing, as neighbors are not assigned initially
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


  //mutates the given cell to be flooded if it is not null (which would indicate
  //that it is not in the board) and is the same color as the given color
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

  // All the cells of the game, organized as list of lists
  ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(FloodItWorld.BOARD_SIZE);
  //tracks number of turns taken as clicks
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

  //mutates world to make the WorldScene, uses constant CANVAS_SIZE
  //if number of clicks exceeds BOARD_SIZE plus 6 (number of colors), you lose
  //else if all cells are flooded (same color), you win
  //otherwise, just create the world scene
  public WorldScene makeScene() {

    if (this.turnsTaken >= BOARD_SIZE + 6) {
      WorldScene lose = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
      lose.placeImageXY(new TextImage("You Lose.",
              100, Color.RED), CANVAS_SIZE / 2, CANVAS_SIZE / 2);
      return lose;
    }

    else if (this.allFlooded(this.board)) {
      WorldScene win = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
      win.placeImageXY(new TextImage("You Win!",
              100, Color.GREEN), CANVAS_SIZE / 2, CANVAS_SIZE / 2);
      return win;
    }

    WorldScene w = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
    w.placeImageXY(this.drawBoard(), CANVAS_SIZE / 2, CANVAS_SIZE / 2);
    return w;
  }

  //restarts the game if "r" key is pressed. makes a new board,
  //draws it, and assigns cell neighbors
  public void onKeyEvent(String ke) {
    super.onKeyEvent(ke);
    if (ke.equals("r")) {
      this.board = new ArrayUtils().makeBoard(BOARD_SIZE);
      new ArrayUtils().assignNeighbors(this.board);
      this.turnsTaken = 0;
    }
  }


  //onTick method to keep track of conditions at each tick
  public void onTick() {
    super.onTick();
  }

  boolean allFlooded(ArrayList<ArrayList<Cell>> board) {
    for (ArrayList<Cell> arr: board) {
      for (Cell c: arr) {
        if (!c.flooded) {
          return false;
        }
      }
    }
    return true;
  }


  //increments turnsTaken on mouse click, and for each cell, checks if the coordinates of mouse
  //click were within the bounds of a cell
  //if so, prompts flooding with floodWorld function
  public void onMouseClicked(Posn mouse) {
    super.onMouseClicked(mouse);
    for (ArrayList<Cell> arr: this.board) {
      for (Cell c: arr) {
        //if click is inside the given cell
        if (this.cellDetection(mouse, c) && this.turnsTaken <= BOARD_SIZE) {
          this.floodWorld(c);
        }
      }
    }
    this.turnsTaken++;
  }

  //loops through list to find flooded cells. Changes flooded cells to the color of the cell that
  //was clicked in onMouseClicked method and then calls floodNeighbor on c's neighbors to check if
  //they should also be flooded
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


  //were the coordinates within the bounds of a given cell?
  boolean cellDetection(Posn click, Cell c) {
    return click.x >= c.x
            && click.y >= c.y
            && click.x <= c.x + CANVAS_SIZE / BOARD_SIZE
            && click.y <= c.y + CANVAS_SIZE / BOARD_SIZE;
  }
}

class ArrayUtils {

  //creates board building list of lists of given size. Outer list created at beginning of method,
  //inner lists created using the i-loop, and elements of inner lists created using j-loop
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

  //draws the board by pasting images together in rows, then stacking them on top of each other
  //to create the grid
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

  //uses nested loops to access elements of list of lists. Depending on which neighbor is being
  //assigned, sets neighbor to either next or previous indexed element in same list or
  //same indexed element in next or previous list
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

  //confirms given i and j indexes are not outside bounds of board. If so, set neighbor to null as,
  //as that would indicate an edge cell.
  //Otherwise, sets neighbor to element at given index of board
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
  ArrayList<ArrayList<Cell>> arrCell4;


  FloodItWorld w1;
  FloodItWorld w2;
  FloodItWorld w3;

  Cell c1;
  Cell c2;
  Cell c3;

  //initializes world state
  void initWorld() {
    arrCell1 = new ArrayUtils().makeBoard(FloodItWorld.BOARD_SIZE);
    arrCell2 = new ArrayUtils().makeBoard(2);
    arrCell3 = new ArrayUtils().makeBoard(16);
    arrCell4 = new ArrayUtils().makeBoard(4);

    new ArrayUtils().assignNeighbors(arrCell1);
    new ArrayUtils().assignNeighbors(arrCell2);
    new ArrayUtils().assignNeighbors(arrCell3);

    w1 = new FloodItWorld(arrCell1);
    w2 = new FloodItWorld(arrCell2);
    w3 = new FloodItWorld(arrCell3);

    c1 = new Cell(
            100,
            100,
            false,
            null,
            null,
            c2,
            c3);
    c2 = new Cell(
            200,
            200,
            false,
            c1,
            null,
            null,
            null);
    c3 = new Cell(
            300,
            300,
            false,
            null,
            c1,
            null,
            null);
  }

  //tests that cellColor works
  void testCellColor(Tester t) {
    initWorld();
    c1.color = c1.cellColor(2);
    t.checkExpect(c1.color, Color.PINK);
    c1.color = c1.cellColor(3);
    t.checkExpect(c1.color, Color.PINK);
  }

  boolean testCellImage(Tester t) {
    initWorld();
    c1.color = c1.cellColor(3);
    c2.color = c2.cellColor(4);
    return t.checkExpect(c1.cellImage(), new RectangleImage(
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            "solid",
            Color.PINK))
            && t.checkExpect(c2.cellImage(), new RectangleImage(
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            FloodItWorld.CANVAS_SIZE / FloodItWorld.BOARD_SIZE,
            "solid",
            Color.BLACK));
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
  */
/*boolean testMakeBoard(Tester t) {
    initWorld();
    return t.checkExpect(new ArrayUtils().makeBoard(FloodItWorld.BOARD_SIZE), w1.board)
            && t.checkExpect(new ArrayUtils().makeBoard(2), w2.board)
            && t.checkExpect(new ArrayUtils().makeBoard(16), w3.board);

  }*//*


  boolean testAssignNeighbors(Tester t) {
    initWorld();
    return t.checkExpect(w1.board.get(2).get(2).left, w1.board.get(2).get(1))
            && t.checkExpect(w1.board.get(0).get(0).left, null);
  }

  boolean testFindNeighbor(Tester t) {
    initWorld();
    return t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, -1, 0), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, 0, -1), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell4, 5, 1), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell4, 1, 5), null)
            && t.checkExpect(new ArrayUtils().findNeighbor(arrCell1, 2, 0),
            arrCell1.get(2).get(0));
  }

  //tests for cellDetection method
  boolean testCellDetection(Tester t) {
    initWorld();
    return t.checkExpect(w1.cellDetection(new Posn(100,100), w1.board.get(0).get(0)),
            true)
            && t.checkExpect(w1.cellDetection(new Posn(700, 300), w1.board.get(2).get(3)),
            false);
  }

  */
/*boolean testFloodWorld(Tester t) {
    initWorld();

  }*//*


  //tests floodNeighbor method
  void testFloodNeighbor(Tester t) {
    initWorld();

    t.checkExpect(w1.board.get(0).get(1).flooded, false);
    w1.board.get(0).get(0).floodNeighbor(w1.board.get(0).get(1), w1.board.get(0).get(0).color);
    t.checkExpect(w1.board.get(0).get(1).flooded, false);

    w2.board.get(0).get(1).color = w2.board.get(0).get(0).color;

    t.checkExpect(w2.board.get(0).get(1).flooded, false);
    w2.board.get(0).get(0).floodNeighbor(w2.board.get(0).get(1), w2.board.get(0).get(0).color);
    t.checkExpect(w2.board.get(0).get(1).flooded, true);
  }

  void testAllFlooded(Tester t) {
    initWorld();

    t.checkExpect(w1.allFlooded(w1.board), false);

    for (ArrayList<Cell> arr: w1.board) {
      for (Cell c: arr) {
        c.color = Color.BLACK;
        c.flooded = true;
      }
    }

    t.checkExpect(w1.allFlooded(w1.board), true);
  }
  */
/*public static void main(String[] argv) {

    // run the tests - showing only the failed test results
    ExamplesWorld w = new ExamplesWorld();

    // run the game
    w.initWorld();
    FloodItWorld world = new FloodItWorld(w.arrCell1);
    world.bigBang(FloodItWorld.CANVAS_SIZE, FloodItWorld.CANVAS_SIZE, 0.3);
  }*//*

}
*/



import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//Assignment 10
//Luke Bakker
//lbakker1858
//Sophie Wigmore
//wigmores

//represents a Node
class Node {
  public static int NODE_SIZE = 50;
  int x;
  int y;
  ArrayList<Edge> connections;

  Node(int x, int y) {
    this.x = x;
    this.y = y;
    this.connections = new ArrayList<Edge>();
  }

  // draws the node
  WorldScene drawNode(WorldScene scene) {
    Color c = Color.lightGray;
    if (this.x == MazeWorld.WORLD_X - 1
            && this.y == MazeWorld.WORLD_Y - 1) {
      c = Color.PINK;
    }
    else if (this.x == 0 && this.y == 0) {
      c = Color.green;
    }
    WorldImage node = new RectangleImage(NODE_SIZE, NODE_SIZE,
            OutlineMode.SOLID, c);
    scene.placeImageXY(node, (this.x * NODE_SIZE) + (NODE_SIZE / 2),
            this.y * NODE_SIZE + (NODE_SIZE / 2));

    // draws every edge in this node
    for (Edge e : connections) {
      e.drawEdge(scene);
    }

    return scene;

  }

  // determines if this node is the same as that node
  public boolean sameNode(Node that) {
    return this.x == that.x && this.y == that.y;

  }
}

// represents an Edge
class Edge {
  public static int EDGE_SIZE = Node.NODE_SIZE;
  int weight;
  Node start;
  Node end;
  boolean toBeRemoved;

  Edge(Node start, Node end) {
    this.start = start;
    this.end = end;
    Random rand = new Random();
    this.weight = rand.nextInt(100);
    toBeRemoved = false;
  }

  Edge(int weight, Node start, Node end) {
    this.weight = weight;
    this.start = start;
    this.end = end;
  }

  // draws the edges
  WorldScene drawEdge(WorldScene scene) {
    if (!this.toBeRemoved) {
      // draws right edge
      if (this.start.x < this.end.x) {
        WorldImage edge = new RectangleImage(1, EDGE_SIZE,
                OutlineMode.SOLID, Color.BLACK);
        scene.placeImageXY(edge, (this.start.x * EDGE_SIZE + EDGE_SIZE),
                this.start.y * EDGE_SIZE + (EDGE_SIZE / 2));
      }
      // draws the bottom edge
      if (this.start.y < this.end.y) {
        WorldImage edge = new RectangleImage(EDGE_SIZE, 1,
                OutlineMode.SOLID, Color.BLACK);
        scene.placeImageXY(edge,
                this.start.x * EDGE_SIZE + (EDGE_SIZE / 2),
                this.start.y * EDGE_SIZE + EDGE_SIZE);
      }
    }
    return scene;
  }

}

// represents the game world
class MazeWorld extends World {
  public static int WORLD_X = 10;
  public static int WORLD_Y = 10;
  ArrayList<ArrayList<Node>> allNodes;
  ArrayList<Edge> edges;

  // constructor for MazeWorld:
  MazeWorld() {
    // builds nodes
    allNodes = new ArrayList<ArrayList<Node>>();
    buildNodeList();
    // builds connections between the nodes
    buildConnections();
    // builds edges w/ randomly weighted edges
    this.edges = new ArrayList<Edge>();
    buildEdgeList();
    // sorts the edges by weight
    sortEdges();
    // creates the spanning tree for the maze
    kruskalAlgorithm();
    // EFFECT: removes the spanning edges from maze
    edgeRemove();
  }

  // draws the world
  public WorldScene makeScene() {
    WorldScene w = new WorldScene(WORLD_X * Node.NODE_SIZE,
            WORLD_Y * Node.NODE_SIZE);
    for (int i = 0; i < WORLD_X; i += 1) {
      for (int j = 0; j < WORLD_Y; j += 1) {
        allNodes.get(i).get(j).drawNode(w);
      }
    }
    return w;
  }

  // EFFECT: builds list of nodes nodes
  void buildNodeList() {
    for (int i = 0; i < WORLD_X; i += 1) {
      ArrayList<Node> temp = new ArrayList<Node>();
      for (int j = 0; j < WORLD_Y; j += 1) {
        temp.add(new Node(i, j));
      }
      this.allNodes.add(temp);
    }
  }

  // EFFECT: updates the connections between nodes
  void buildConnections() {
    for (int i = 0; i < WORLD_X; i += 1) {
      for (int j = 0; j < WORLD_Y; j += 1) {
        // adds connection on right to list of connections
        if (i < WORLD_X - 1) {
          allNodes.get(i).get(j).connections
                  .add(new Edge(allNodes.get(i).get(j),
                          allNodes.get(i + 1).get(j)));
        }
        // adds connection on bottom to list of connections
        if (j < WORLD_Y - 1) {
          allNodes.get(i).get(j).connections
                  .add(new Edge(allNodes.get(i).get(j),
                          allNodes.get(i).get(j + 1)));
        }
      }
    }
  }

  // returns the spanning tree for the edges of the maze
  ArrayList<Edge> kruskalAlgorithm() {
    HashMap<Node, Node> representatives = new HashMap<Node, Node>();
    ArrayList<Edge> worklist = this.edges;
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();

    // initializes every node in worklist to be its own representative
    for (ArrayList<Node> nodeList : this.allNodes) {
      for (Node n : nodeList) {
        representatives.put(n, n);
      }
    }
    int numConnections = 0;
    int worklistIdx = 0;
    while (numConnections < WORLD_X * WORLD_Y - 1) {
      Edge currentCheap = worklist.get(worklistIdx);
      if (this.find(representatives, currentCheap.start)
              .sameNode(this.find(representatives, currentCheap.end))) {
        worklistIdx = worklistIdx + 1;
      }
      else {
        edgesInTree.add(worklist.get(worklistIdx));
        union(representatives,
                this.find(representatives, currentCheap.start),
                find(representatives, currentCheap.end));
        worklistIdx = worklistIdx + 1;
        numConnections = numConnections + 1;
      }
    }
    return edgesInTree;
  }

  // finds the representative of the current node
  Node find(HashMap<Node, Node> representatives, Node node) {
    // the node is its own representative
    if (node.sameNode(representatives.get(node))) {
      return node;
    }
    // the node maps to another representative
    else {
      return find(representatives, representatives.get(node));
    }
  }

  // EFFECT: "unions" or joins two disjoint groups together using their
  // representatives
  void union(HashMap<Node, Node> representatives, Node n1, Node n2) {
    representatives.put(n1, n2);
  }

  // EFFECT: builds the list of edges between nodes
  void buildEdgeList() {
    for (ArrayList<Node> nodeList : allNodes) {
      for (Node node : nodeList) {
        for (Edge edge : node.connections) {
          this.edges.add(edge);
        }
      }

    }
  }

  // EFFECT: sorts the edges by weight
  void sortEdges() {
    sortEdgeHelp(0, this.edges.size());
  }

  // EFFECT: sorts the edges in the range of indices according to weight
  void sortEdgeHelp(int low, int high) {
    if (low >= high) {
      return;
    }
    Edge pivot = this.edges.get(low);
    int pivotIdx = partition(low, high, pivot);
    sortEdgeHelp(low, pivotIdx);
    sortEdgeHelp(pivotIdx + 1, high);
  }

  // returns the index where the pivot ends up in the sorted source
  // EFFECT: modifies the main edge list and the temp in the given range
  // so all values to left of pivot are less than or equal to the pivot
  // and all values to the right of the pivot are greater than it
  int partition(int low, int high, Edge pivot) {
    int curLo = low;
    int curHi = high - 1;
    while (curLo < curHi) {
      while (curLo < high
              && this.edges.get(curLo).weight <= pivot.weight) {
        curLo = curLo + 1;
      }

      while (curHi > low && this.edges.get(curHi).weight > pivot.weight) {
        curHi = curHi - 1;
      }
      if (curLo < curHi) {
        Edge tempLo = this.edges.get(curLo);
        this.edges.set(curLo, this.edges.get(curHi));
        this.edges.set(curHi, tempLo);
      }
    }
    Edge tempHi = this.edges.get(curHi);
    this.edges.set(curHi, pivot);
    this.edges.set(low, tempHi);
    return curHi;
  }

  // EFFECT: updates each edge in spanning tree to be removed from maze
  void edgeRemove() {
    for (Edge e : this.kruskalAlgorithm()) {
      e.toBeRemoved = true;
    }
  }
}

class ExamplesMaze {
  MazeWorld m;

  // initializes data
  void initData() {
    this.m = new MazeWorld();
  }

  void testRun(Tester t) {
    initData();
    m.bigBang((m.WORLD_X) * Node.NODE_SIZE, (m.WORLD_Y) * Node.NODE_SIZE);
  }

  // tests buildConnections method
  void testBuildConnections(Tester t) {
    initData();
    t.checkExpect(m.allNodes.get(0).get(0).connections.size() == 2, true);
    t.checkExpect(m.allNodes.get(MazeWorld.WORLD_X - 1).get(0).connections
            .size() == 1, true);
    t.checkExpect(
            m.allNodes.get(MazeWorld.WORLD_X - 1)
                    .get(MazeWorld.WORLD_Y - 1).connections.size() == 0,
            true);
    t.checkExpect(m.allNodes.get(3).get(3).connections.size() == 2, true);
    t.checkExpect(m.allNodes.get(0).get(MazeWorld.WORLD_Y - 1).connections
            .size() == 1, true);
  }

  // tests buildEdgeList
  void testBuildEdgeList(Tester t) {
    initData();
    t.checkExpect(m.edges.size() > 0, true);
    t.checkExpect(m.edges.size(),
            (MazeWorld.WORLD_X * (MazeWorld.WORLD_Y - 1))
                    + (MazeWorld.WORLD_Y * (MazeWorld.WORLD_X - 1)));

  }

  // tests sortEdges
  void testSortEdges(Tester t) {
    initData();
    for (int i = 0; i < 179; i += 1) {
      t.checkExpect(m.edges.get(i).weight <= m.edges.get(i + 1).weight,
              true);
    }
  }

  // tests kruskal
  void testKruskal(Tester t) {
    initData();
    t.checkExpect(m.kruskalAlgorithm().size(), 99);

  }

  // tests removal of edges
  void testEdgeRemoval(Tester t) {
    initData();
    for (int i = 0; i < 99; i += 1) {
      t.checkExpect(m.kruskalAlgorithm().get(i).toBeRemoved, true);
    }
  }

}
