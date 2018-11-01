import tester.Tester;

// interface for IPred<T> use for applying methods
interface IPred<T> {
  boolean apply(T t);
}

//class for NodeCompare which contains a String of data
class NodeCompare implements IPred<String> {

  String data;

  NodeCompare(String data) {
    this.data = data;
  }

  //boolean method for applying NodeCompare
  public boolean apply(String other) {
    return this.data.equals(other);
  }
}

//Deque<T> class in which the header = Sentinel<T>
class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  //returns the size of the header
  int size() {
    return this.header.size();
  }

  // void method for adding header item T
  void addAtHead(T item) {
    this.header.addNode(item);
  }

  // void method for adding a tail item T
  void addAtTail(T item) {
    this.header.prev.addNode(item);
  }

  //remove function for abstract header T
  T removeFromHead() {
    return this.header.next.removeAbstract();
  }

  //remove function for abstract tail T
  T removeFromTail() {
    return this.header.prev.removeAbstract();
  }

  //finds the given pred in IPred<T>
  ANode<T> find(IPred<T> pred) {
    return this.header.next.find(pred);
  }

  //void remove method for a the node from Node<T>
  public void removeNode(Node<T> that) {
    this.header.next.removeNode(that);
  }
}

//abstract class for ANode<T> contains next and prev
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  ANode() {
    this.next = this;
    this.prev = this;
  }

  // abstract method for computing the size
  public abstract int size();

  // abstract method helper for size
  public abstract int sizeHelp();

  // abstract void method for adding T item as header
  public void addNode(T item) {
    this.next = new Node<T>(item, this.next, this);
  }

  // abstract method which removes abstract item T
  public abstract T removeAbstract();

  // abstract method for finding given pred
  public abstract ANode<T> find(IPred<T> pred);

  //removes the given node from the list
  public abstract void removeNode(ANode<T> that);

  //double dispatch for NODES
  public abstract void removeNodeCons(Node<T> tNode);

  //double dispatch for SENTINEL
  public abstract void removeNodeSent(Sentinel<T> tSentinel);
}

//Class for Node<T> contains T data
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.prev = null;
    this.next = null;
    this.data = data;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;

    if (next == null || prev == null) {
      throw new IllegalArgumentException("next and prev nodes cannot be null");
    }
    next.prev = this;
    prev.next = this;
  }

  // computes the size of Node<T>, adds 1 to count for prev
  public int size() {
    return 1 + this.next.sizeHelp();
  }

  // helper method for size
  public int sizeHelp() {
    return 1 + this.next.sizeHelp();
  }

  //abstract method which removes abstract item T
  public T removeAbstract() {
    T data = this.data;
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return data;
  }

  //finder method which finds the given pred
  public ANode<T> find(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.find(pred);
    }
  }

  //void method which removes a Node<T>
  public void removeNode(ANode<T> that) {
    that.removeNodeCons(this);
  }

  //double dispatch for NODES
  public void removeNodeCons(Node<T> that) {
    if (this.data.equals(that.data)) {
      this.removeAbstract();
    }
    else {
      that.next.removeNode(this);
    }
  }

  //double dispatch for SENTINELS
  public void removeNodeSent(Sentinel<T> that) {
    return;
  }


}


//class for Sentinel<T> which contains
class Sentinel<T> extends ANode<T> {

  //constantly updated to be linked to the head and tail of the list, respectively
  Sentinel() {
    super();
  }

  // size method for Sentinel
  public int size() {
    return this.next.sizeHelp();
  }

  // helper method for size, returns 0 since empty
  public int sizeHelp() {
    return 0;
  }

  // removes abstract from list, returns exception since empty list
  public T removeAbstract() {
    throw new RuntimeException("Cannot remove item from empty list");
  }

  //finder method for ANode<T> finds the given pred
  public ANode<T> find(IPred<T> pred) {
    return this;
  }

  //void method for removing a Node
  public void removeNode(ANode<T> that) {
    return;
  }

  //double dispatch for NODES
  public void removeNodeCons(Node<T> that) {
    return;
  }

  //double dispatch for SENTINELS
  public void removeNodeSent(Sentinel<T> that) {
    return;
  }
}


class ExamplesDeque {

  Deque<String> deque1;


  Sentinel<String> sent1;

  Node<String> abc = new Node<String>("abc");
  Node<String> bcd = new Node<String>("bcd");
  Node<String> cde = new Node<String>("cde");
  Node<String> def = new Node<String>("def");

  Deque<String> deque2;



  Sentinel<String> sent2;

  Node<String> alex = new Node<String>("alex");
  Node<String> and = new Node<String>("and");
  Node<String> jules = new Node<String>("jules");
  Node<String> are = new Node<String>("are");
  Node<String> dope = new Node<String>("dope");

  Deque<String> deque3;

  void initDeque() {

    deque1 = new Deque<String>();

    sent1 = new Sentinel<String>();
    abc = new Node<String>("abc", bcd, sent1);
    bcd = new Node<String>("bcd", cde, abc);
    cde = new Node<String>("cde", def, bcd);
    def = new Node<String>("def", sent1, cde);

    deque2 = new Deque<String>(sent1);

    sent2 = new Sentinel<String>();
    alex = new Node<String>("alex", sent2, and);
    and = new Node<String>("and", alex, jules);
    jules = new Node<String>("jules", and, are);
    are = new Node<String>("are", jules, dope);
    dope = new Node<String>("dope", are, sent2);

    deque3 = new Deque<String>(sent2);
  }

  boolean testSize(Tester t) {
    initDeque();
    return t.checkExpect(deque1.size(), 0)
            && t.checkExpect(deque2.size(), 4);
  }

  void testAddAtHead(Tester t) {
    initDeque();
    deque2.addAtHead("another");
    t.checkExpect(deque2.header.next.next.next, new Node<String>("bcd", cde, abc));
    t.checkExpect(deque2.header.next, new Node<String>("another", abc, sent1));
    deque1.addAtHead("boop");
    t.checkExpect(deque1.header.next, new Node<String>("boop", sent1, sent1));
  }

  void testAddAtTail(Tester t) {
    initDeque();
    deque2.addAtTail("another");
    t.checkExpect(deque2.header.prev, new Node<String>("another", sent1, def));
    t.checkExpect(deque2.header.prev.prev.prev,
            new Node<String>("cde", def, bcd));
    deque1.addAtTail("boop");
    t.checkExpect(deque1.header.prev, new Node<String>("boop", sent1, sent1));
  }

  boolean testRemoveFromHead(Tester t) {
    initDeque();
    return t.checkException(new RuntimeException("Cannot remove item from empty list"),
            deque1,
            "removeFromHead")
            && t.checkExpect(deque2.removeFromHead(), "abc");
  }

  boolean testRemoveFromTail(Tester t) {
    initDeque();
    return t.checkException(new RuntimeException("Cannot remove item from empty list"),
            deque1,
            "removeFromTail")
            && t.checkExpect(deque2.removeFromTail(), "def");
  }

  boolean testFind(Tester t) {
    initDeque();
    return t.checkExpect(deque1.find(new NodeCompare("bcd")),
            new Sentinel<String>())
            && t.checkExpect(deque2.find(new NodeCompare("bcd")),
            bcd)
            && t.checkExpect(deque2.find(new NodeCompare("sgdsgs")),
            sent1);
  }

  void testRemoveNode(Tester t) {
    initDeque();
    t.checkExpect(deque1.header.next, new Sentinel<String>());
    deque1.removeNode(abc);
    t.checkExpect(deque1.header.next, new Sentinel<String>());
    t.checkExpect(deque2.header.next, abc);
    deque2.removeNode(bcd);
    t.checkExpect(deque2.header.next.next, cde);
  }








}