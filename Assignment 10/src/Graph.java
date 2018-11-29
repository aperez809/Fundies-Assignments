import java.awt.*;
import java.util.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import tester.*;


class GraphUtils {

  boolean hasPathBetween(Vertex a, Vertex b) {
    HashSet<Vertex> alreadySeen = new HashSet<>();
    Stack<Vertex> workList = new Stack<>(new Deque<>());

    workList.push(a);

    while (workList.size() > 0) {
      Vertex v = workList.pop();

      if (alreadySeen.contains(v)) { }

      else if (v == b) {
        return true;
      }

      else {
        for (Edge e : v.outEdges) {
          workList.push(e.to);
        }

        alreadySeen.add(v);
      }

    }
    return false;
  }

  //Dijkstra's algorithm
  /*int shortestPath(Vertex a, Vertex b) {

    HashMap<Vertex, Integer> distTo = new HashMap<>();
    PriorityQueue<Vertex> workList = new PriorityQueue<>();

    distTo.put(a, 0);
    workList.add(a);

    while (!workList.isEmpty()) {
      //TODO: Make removeMin, obviously
      Vertex u = workList.removeMin(); //design helper
      for (Edge e: u.outEdges) {
        int newDist = distTo.get(u) + e.weight;
        if (distTo.get(u) > newDist) {
          distTo.put(u, newDist);
        }
      }
    }
  }*/
}

class Vertex {
  int x;
  int y;
  ArrayList<Edge> outEdges;

  public Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.outEdges = new ArrayList<>();
  }

  boolean hasPathTo(Vertex dest) {
    if (this == dest) {
      return true;
    } else {
      for (Edge e : this.outEdges) {
        if (e.to == dest || e.to.hasPathTo(dest)) {
          return true;
        }
      }
    }
    return false;
  }

  WorldImage cellImage() {
    WorldImage init = new EmptyImage();
    for (Edge e : this.outEdges) {
      init = new OverlayImage(e.edgeImage(), init);
    }

    if (this.x == MazeWorld.CANVAS_SIZE - (MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE)
            && this.y == MazeWorld.CANVAS_SIZE - (MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE)) {
      return new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM, init, 0, 0, new RectangleImage(
              MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
              MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
              "solid",
              Color.GREEN));
    }
    else if (this.x == 0 && this.y == 0) {
      return new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM, init, 0, 0, new RectangleImage(
              MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
              MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
              "solid",
              Color.RED));
    }
    else {
      return new OverlayImage(
            new TextImage(
                    Integer.toString(this.x) + ", " + Integer.toString(this.y), Color.CYAN),
              new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM, init, 0, 0, new RectangleImage(
                      MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
                      MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
                      "solid",
                      Color.lightGray)));
    }
  }

  public boolean sameNode(Vertex vertex) {
    return this.x == vertex.x
            && this.y == vertex.y;
  }
}

class Edge {
  Vertex from;
  Vertex to;
  int weight;
  boolean toRemove;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
    this.toRemove = false;
  }

  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    this.weight = new Random().nextInt(100);
    this.toRemove = false;
  }

  WorldImage edgeImage() {
    WorldImage init = new EmptyImage();
    if (!this.toRemove) {
      // draws right edge
      if (this.from.x < this.to.x) {
        System.out.println("Drawing right for " + this.from.x + ", " + this.from.y + " ---- " + from.x + ", " + to.x);
        init = new BesideAlignImage(AlignModeY.TOP, init,
                new RectangleImage(5, MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
                OutlineMode.SOLID, Color.WHITE));
      }
      // draws the bottom edge
      if (this.from.y < this.to.y) {
        System.out.println("Drawing bottom for " + this.from.x + ", " + this.from.y + " ---- " + from.y + ", " + to.y);
        return new AboveAlignImage(AlignModeX.RIGHT, init,
                new RectangleImage(MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE, 5,
                OutlineMode.SOLID, Color.WHITE));
      }
    }
    return init;
  }
}

class MazeWorld extends World {
  static final int CANVAS_SIZE = 400;
  static final int BOARD_SIZE = 4;

  ArrayList<Edge> edges;
  ArrayList<ArrayList<Vertex>> vertexList;

  public MazeWorld() {
    this.vertexList = new ArrayList<ArrayList<Vertex>>();
    makeVertices(BOARD_SIZE);
    makeBridges();


    this.edges = new ArrayList<Edge>();
    makeEdges();
    sortEdges();
    unionFind();
    edgeRemove();
  }

  public WorldScene makeScene() {
    WorldScene w = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
    w.placeImageXY(this.drawBoard(),
                CANVAS_SIZE / 2,
                CANVAS_SIZE / 2);
    return w;
  }

  WorldImage drawBoard() {
    WorldImage grid = new EmptyImage();  //vertical cells stacked on each other, final image

    //for each list contained in "this.board"
    for (ArrayList<Vertex> arr: this.vertexList) {
      WorldImage row = new EmptyImage();   //horizontal cells being rendered side-to-side
      //for each cell in current list contained in "this.board"
      for (Vertex v: arr) {

        //mutate row to add another image on its right
        row = new BesideImage(row, v.cellImage());
      }

      //adds the new row of images to the grid, underneath the previous row
      grid = new AboveImage(grid, row);
    }
    return grid;
  }



  void makeVertices(int boardSize) {
    for (int i = 0; i < boardSize; i++) {
      ArrayList<Vertex> temp = new ArrayList<>();
      for (int j = 0; j < boardSize; j++) {
        temp.add(
                new Vertex((CANVAS_SIZE / boardSize) * i,
                        (CANVAS_SIZE / boardSize * j)));
      }
      this.vertexList.add(temp);
    }
  }

  void makeBridges() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        // adds connection on right to list of connections
        if (i < BOARD_SIZE - 1) {
          vertexList.get(i).get(j).outEdges
                  .add(new Edge(vertexList.get(i).get(j),
                          vertexList.get(i + 1).get(j)));
        }
        // adds connection on bottom to list of connections
        if (j < BOARD_SIZE - 1) {
          vertexList.get(i).get(j).outEdges
                  .add(new Edge(vertexList.get(i).get(j),
                          vertexList.get(i).get(j + 1)));
        }
      }
    }
  }

  void makeEdges() {
    for (ArrayList<Vertex> vertices: vertexList) {
      for (Vertex v: vertices) {
        this.edges.addAll(v.outEdges);
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

  // returns the spanning tree for the edges of the maze
  ArrayList<Edge> unionFind() {
    HashMap<Vertex, Vertex> representatives = new HashMap<>();
    ArrayList<Edge> workList = this.edges;
    ArrayList<Edge> edgesInTree = new ArrayList<>();

    // initializes every node in worklist to be its own representative
    for (ArrayList<Vertex> vertices : vertexList) {
      for (Vertex v : vertices) {
        representatives.put(v, v);
      }
    }

    int numConnections = 0;
    int worklistIdx = 0;

    while (numConnections < BOARD_SIZE * BOARD_SIZE - 1) {

      Edge currentCheap = workList.get(worklistIdx);

      if (this.find(representatives, currentCheap.from).sameNode(
              this.find(representatives, currentCheap.to))) {

        worklistIdx = worklistIdx + 1;

      }

      else {

        edgesInTree.add(workList.get(worklistIdx));
        union(representatives,
                this.find(representatives, currentCheap.from),
                find(representatives, currentCheap.to));

        worklistIdx = worklistIdx + 1;
        numConnections = numConnections + 1;
      }
    }
    return edgesInTree;
  }

  Vertex find(HashMap<Vertex, Vertex> reps, Vertex v) {
    if (v == reps.get(v)) {
      return v;
    }

    else {
      return find(reps, reps.get(v));
    }
  }

  void union(HashMap<Vertex, Vertex> reps, Vertex v, Vertex u) {
    reps.put(v, u);
  }

  void edgeRemove() {
    for (Edge e : this.unionFind()) {
      e.toRemove = true;
    }
  }
}

class ExampleWorld {

  MazeWorld m;
  Vertex a;
  Vertex b;
  Edge e;


  void initData() {
    this.m = new MazeWorld();
    a = new Vertex(100, 100);
    b = new Vertex(200, 200);
    e = new Edge(a, b, 10);
  }

/*
  boolean testCellImage(Tester t) {
    initData();
    return t.checkExpect(a.cellImage(), new OverlayImage(new EmptyImage(), new RectangleImage(
            MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
            MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE,
            "solid",
            Color.lightGray)));
  }

  void testMakeVertices(Tester t) {
    initData();
    m.makeVertices(MazeWorld.BOARD_SIZE);
    t.checkExpect(m.vertexList.get(1).get(1), new Vertex(1,2));
  }*/

  void testRun(Tester t) {
    initData();
    m.bigBang(MazeWorld.CANVAS_SIZE, MazeWorld.CANVAS_SIZE);
  }

}
/*
  public static void main(String[] argv) {

    // run the tests - showing only the failed test results
    ExamplesMaze w = new ExamplesMaze();

    // run the game
    w.initData();
    MazeWorld world = new MazeWorld();
    world.bigBang(MazeWorld.CANVAS_SIZE, MazeWorld.CANVAS_SIZE, 0.3);
  }
}*/





