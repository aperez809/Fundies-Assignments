import tester.Tester;

interface IPred<T> {
  boolean apply(T t);
}

class NodeCompare implements IPred<String> {

  String data;

  NodeCompare(String data) {
    this.data = data;
  }


  public boolean apply(String other) {
    return this.data.equals(other);
  }
}

class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  int size() {
    return this.header.size();
  }

  void addAtHead(T item) {
    this.header.addAtHead(item);
  }

  void addAtTail(T item) {
    this.header.addAtTail(item);
  }

  T removeFromHead() {
    return this.header.next.removeAbstract();
  }

  T removeFromTail() {
    return this.header.prev.removeAbstract();
  }

  ANode<T> find(IPred<T> pred) {
    return this.header.next.find(pred);
  }

  public void removeNode(ANode<T> that) {
    this.header.next.removeNode(that);
  }
}

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

  public abstract int size();

  public abstract int sizeHelp();

  public abstract void addAtHead(T item);

  public abstract void addAtTail(T item);

  public abstract T removeAbstract();

  public abstract ANode<T> find(IPred<T> pred);

  public abstract void removeNode(ANode<T> that);

  public abstract void removeNodeCons(Node<T> tNode);

  public abstract void removeNodeSent(Sentinel<T> tSentinel);
}

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

  public int size() {
    return 1 + this.next.sizeHelp();
  }

  public int sizeHelp() {
    return 1 + this.next.sizeHelp();
  }

  public void addAtHead(T item) {
  }

  public void addAtTail(T item) {
  }

  public T removeAbstract() {
    T data = this.data;
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return data;
  }


  public ANode<T> find(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.find(pred);
    }
  }

  public void removeNode(ANode<T> that) {
    that.removeNodeCons(this);
  }

  public void removeNodeCons(Node<T> that) {
    this.removeAbstract();
  }

  public void removeNodeSent(Sentinel<T> that) {
    return;
  }


}


class Sentinel<T> extends ANode<T> {

  //constantly updated to be linked to the head and tail of the list, respectively
  Sentinel() {
    super();
  }


  public int size() {
    return this.next.sizeHelp();
  }

  public int sizeHelp() {
    return 0;
  }

  public void addAtHead(T item) {
    this.next = new Node<T>(item, this.next, this);
  }


  public void addAtTail(T item) {
    this.prev = new Node<T>(item, this.prev, this);
  }

  public T removeAbstract() {
    throw new RuntimeException("Cannot remove item from empty list");
  }

  public ANode<T> find(IPred<T> pred) {
    return this;
  }

  public void removeNode(ANode<T> that) {
    that.removeNodeSent(this);
  }

  public void removeNodeCons(Node<T> that) {
    return;
  }

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

  Deque<String> deque3;

  void initDeque() {

    deque1 = new Deque<String>();

    sent1 = new Sentinel<String>();
    abc = new Node<String>("abc", bcd, sent1);
    bcd = new Node<String>("bcd", cde, abc);
    cde = new Node<String>("cde", def, bcd);
    def = new Node<String>("def", sent1, cde);

    deque2 = new Deque<String>(sent1);
  }

  boolean testSize(Tester t) {
    initDeque();
    return t.checkExpect(deque1.size(), 0)
            && t.checkExpect(deque2.size(), 4);
  }

  //TODO: Figure out how to write tests for this lol
  void testAddAtHead(Tester t) {
    initDeque();
    deque2.addAtHead("another");
    t.checkExpect(deque2.header.next.next.next, new Node<String>("bcd", cde, abc));
  }

  //TODO: ^^^
  void testAddAtTail(Tester t) {
    initDeque();
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
    deque2.removeNode(abc);
    t.checkExpect(deque2.header.next, bcd);
  }








}