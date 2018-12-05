import java.awt.*;
import java.util.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import tester.*;


class Vertex {

  static final int VERTEX_SIZE = MazeWorld.CANVAS_SIZE / MazeWorld.BOARD_SIZE;
  int x;
  int y;
  ArrayList<Edge> outEdges;
  Color color;
  boolean visited;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.outEdges = new ArrayList<>();
    this.color = Color.lightGray;
    this.visited = false;
  }


  // draws the vertex as a cell
  WorldScene drawVertex(WorldScene scene) {
    if (this.visited) {
      this.color = Color.PINK;
    }
    if (this.x == MazeWorld.CANVAS_SIZE - VERTEX_SIZE
            && this.y == MazeWorld.CANVAS_SIZE - VERTEX_SIZE) {
      this.color = Color.RED;
    } else if (this.x == 0 && this.y == 0) {
      this.color = Color.GREEN;
    }
    WorldImage cell = new RectangleImage(VERTEX_SIZE, VERTEX_SIZE,
            OutlineMode.SOLID, this.color);
    scene.placeImageXY(cell, (this.x + (VERTEX_SIZE / 2)),
            (this.y + (VERTEX_SIZE / 2)));

    // draws every edge in this vertex
    for (Edge e : this.outEdges) {
      e.drawEdge(scene);
    }

    return scene;
  }

  boolean sameVertex(Vertex vertex) {
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


  WorldScene drawEdge(WorldScene scene) {
    if (!this.toRemove) {
      // draws right edge
      if (this.from.x < this.to.x) {
        WorldImage edge = new RectangleImage(3, Vertex.VERTEX_SIZE,
                OutlineMode.SOLID, Color.BLACK);
        scene.placeImageXY(edge, (this.from.x + Vertex.VERTEX_SIZE),
                this.from.y + (Vertex.VERTEX_SIZE / 2));
      }
      // draws the bottom edge
      if (this.from.y < this.to.y) {
        WorldImage edge = new RectangleImage(Vertex.VERTEX_SIZE, 3,
                OutlineMode.SOLID, Color.BLACK);
        scene.placeImageXY(edge,
                this.from.x + (Vertex.VERTEX_SIZE / 2),
                this.from.y + Vertex.VERTEX_SIZE);
      }
    }
    return scene;
  }
}

class MazeWorld extends World {
  static final int CANVAS_SIZE = 500;
  static final int BOARD_SIZE = 10;

  ArrayList<Edge> edges;
  ArrayList<ArrayList<Vertex>> vertexList;
  Vertex current;

  public MazeWorld() {
    this.vertexList = new ArrayList<ArrayList<Vertex>>();
    makeVertices(BOARD_SIZE);
    makeBridges();


    this.edges = new ArrayList<Edge>();
    makeEdges();
    sortEdges();
    unionFind();
    edgeRemove();

    this.current = this.vertexList.get(0).get(0);
  }

  // draws the world
  public WorldScene makeScene() {
    WorldScene w = new WorldScene(CANVAS_SIZE, CANVAS_SIZE);
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        vertexList.get(i).get(j).drawVertex(w);
      }
    }
    return w;
  }

  //onTick method
  public void onTick() {
    super.onTick();
  }

  @Override
  public void onKeyEvent(String s) {
    super.onKeyEvent(s);
    if (s.equals("b")) {
      this.breadthFirst(this.vertexList.get(0).get(0),
              this.vertexList.get(BOARD_SIZE - 1).get(BOARD_SIZE - 1));
    }
    else if (s.equals("d")) {
      this.depthFirst(this.vertexList.get(0).get(0),
              this.vertexList.get(BOARD_SIZE - 1).get(BOARD_SIZE - 1));
    }
    else if (s.equals("r")) {
      MazeWorld newW = new MazeWorld();
      this.vertexList = newW.vertexList;
      this.edges = newW.edges;
    }

    else if (s.equals("left") || s.equals("right") || s.equals("up") || s.equals("down")) {
      this.moveCurrent(s);
    }
  }

  void moveCurrent(String s) {
    if (s.equals("left") && this.current.x > 0) {
      this.current = this.vertexList.get(this.current.y / Vertex.VERTEX_SIZE)
              .get(this.current.x / Vertex.VERTEX_SIZE - 1);
    }
    else if (s.equals("right") && this.current.x < CANVAS_SIZE) {
      this.current = this.vertexList.get(this.current.y / Vertex.VERTEX_SIZE)
              .get(this.current.x / Vertex.VERTEX_SIZE + 1);
    }
    else if (s.equals("up") && this.current.y > 0) {
      this.current = this.vertexList.get(this.current.y / Vertex.VERTEX_SIZE - 1)
              .get(this.current.x / Vertex.VERTEX_SIZE);
    }
    else if (s.equals("down") && this.current.y < CANVAS_SIZE) {
      this.current = this.vertexList.get(this.current.y / Vertex.VERTEX_SIZE + 1)
              .get(this.current.x / Vertex.VERTEX_SIZE);
    }
    this.current.visited = true;
  }

  void makeVertices(int boardSize) {
    for (int i = 0; i < boardSize; i++) {
      ArrayList<Vertex> temp = new ArrayList<>();
      for (int j = 0; j < boardSize; j++) {
        temp.add(
                new Vertex((CANVAS_SIZE / boardSize * j),
                        (CANVAS_SIZE / boardSize) * i));
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
    for (ArrayList<Vertex> vertices : vertexList) {
      for (Vertex v : vertices) {
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

    // initializes every vertex in worklist to be its own representative
    for (ArrayList<Vertex> vertices : vertexList) {
      for (Vertex v : vertices) {
        representatives.put(v, v);
      }
    }

    int numConnections = 0;
    int worklistIdx = 0;

    while (numConnections < BOARD_SIZE * BOARD_SIZE - 1) {

      Edge currentCheap = workList.get(worklistIdx);

      if (this.find(representatives, currentCheap.from).sameVertex(
              this.find(representatives, currentCheap.to))) {

        worklistIdx++;

      }

      else {

        edgesInTree.add(workList.get(worklistIdx));
        union(representatives,
                this.find(representatives, currentCheap.from),
                find(representatives, currentCheap.to));

        worklistIdx++;
        numConnections++;
      }
    }
    return edgesInTree;
  }

  Vertex find(HashMap<Vertex, Vertex> reps, Vertex v) {
    if (v.sameVertex(reps.get(v))) {
      return v;
    }

    else {
      return find(reps, reps.get(v));
    }
  }

  void union(HashMap<Vertex, Vertex> reps, Vertex v, Vertex u) {
    for (Vertex curr : reps.keySet()) {
      if (reps.get(curr).sameVertex(v)) {
        reps.put(curr, u);
      }
      //reps.put(v, u);
    }
  }

  void edgeRemove() {
    for (Edge e : this.unionFind()) {
      e.toRemove = true;
    }
  }

  //is there a path using depth first search
  boolean depthFirst(Vertex from, Vertex to) {
    return this.hasPath(from, to, new Stack<Vertex>());
  }

  //is there a path using depth first search
  boolean breadthFirst(Vertex from, Vertex to) {
    return this.hasPath(from, to, new Queue<Vertex>());
  }

  //is there a path from vertex from to vertex to
  boolean hasPath(Vertex from, Vertex to, ICollection<Vertex> worklist) {
    ArrayList<Vertex> seen = new ArrayList<>();

    worklist.add(from, 0);

    while (worklist.size() > 0) {
      Vertex next = worklist.remove();

      if (next == to) {
        return true;
      }
      else if (seen.contains(next)) {}

      else {
        for (Edge e : next.outEdges) {
          if (e.toRemove) {
            worklist.add(e.to, 0);
          }
        }
        seen.add(next);
        next.color = Color.PINK;
        this.makeScene();
      }
    }
    return false;
  }
}

interface ICollection<T> {
  //adds an item to this collection
  void add(T item, int priority);
  //removes an item from this collection
  T remove();
  //counts the number of items in this collection
  int size();
}

class Queue<T> implements ICollection<T> {
  Deque<T> items;

  Queue() {
    this.items = new Deque<T>();
  }

  @Override
  public void add(T item, int priority) {
    this.items.addAtTail(item);
  }

  @Override
  public T remove() {
    return this.items.removeFromHead();
  }

  @Override
  public int size() {
    return this.items.size();
  }
}

class Stack<T> implements ICollection<T> {
  Deque<T> items;

  Stack() {
    this.items = new Deque<T>();
  }

  @Override
  public void add(T item, int priority) {
    this.items.addAtHead(item);
  }

  @Override
  public T remove() {
    return this.items.removeFromHead();
  }

  @Override
  public int size() {
    return this.items.size();
  }

}

class PriorityQueue<T> implements ICollection<T> {
  Deque<T> items;

  PriorityQueue(Deque<T> items) {
    this.items = items;
  }

  @Override
  public void add(T item, int priority) {
    return;
  }

  @Override
  public T remove() {
    return this.items.removeFromTail();
  }

  @Override
  public int size() {
    return this.items.size();
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


  void testRun(Tester t) {
    initData();
    m.bigBang(MazeWorld.CANVAS_SIZE, MazeWorld.CANVAS_SIZE, 2);
  }

  // tests buildConnections method
  void testMakeBridges(Tester t) {
    initData();
    t.checkExpect(m.vertexList.get(0).get(0).outEdges.size() == 2, true);
    t.checkExpect(m.vertexList.get(MazeWorld.BOARD_SIZE - 1).get(0).outEdges
            .size() == 1, true);
    t.checkExpect(
            m.vertexList.get(MazeWorld.BOARD_SIZE - 1)
                    .get(MazeWorld.BOARD_SIZE - 1).outEdges.size() == 0,
            true);
    t.checkExpect(m.vertexList.get(1).get(1).outEdges.size() == 2, true);
    t.checkExpect(m.vertexList.get(0).get(MazeWorld.BOARD_SIZE - 1).outEdges
            .size() == 1, true);
  }

  // tests buildEdgeList
  void testMakeEdges(Tester t) {
    initData();
    t.checkExpect(m.edges.size() > 0, true);
    t.checkExpect(m.edges.size(),
            (MazeWorld.BOARD_SIZE * (MazeWorld.BOARD_SIZE - 1))
                    + (MazeWorld.BOARD_SIZE * (MazeWorld.BOARD_SIZE - 1)));

  }

  // tests sortEdges
  void testSortEdges(Tester t) {
    initData();
    for (int i = 0; i < MazeWorld.BOARD_SIZE * MazeWorld.BOARD_SIZE; i++) {
      t.checkExpect(m.edges.get(i).weight <= m.edges.get(i + 1).weight,
              true);
    }
  }

  // tests kruskal algorithms
  void testUnionFind(Tester t) {
    initData();
    t.checkExpect(m.unionFind().size(), MazeWorld.BOARD_SIZE * MazeWorld.BOARD_SIZE - 1);

  }

  // tests removal of edges
  void testEdgeRemove(Tester t) {
    initData();
    for (int i = 0; i < MazeWorld.BOARD_SIZE; i++) {
      t.checkExpect(m.unionFind().get(i).toRemove, true);
    }
  }

  //tests that onKey returns a new board
  boolean testOnKey(Tester t) {
    initData();
    MazeWorld old = m;
    m.onKeyEvent("r");
    return !t.checkExpect(m.vertexList, old.vertexList)
            && !t.checkExpect(m.edges, old.edges);
  }
}



