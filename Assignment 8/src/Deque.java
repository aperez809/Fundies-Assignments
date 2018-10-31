import tester.Tester;

interface IPred<T> {
  boolean apply(T t);
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

  ANode<T> removeFromHead() {
    return this.header.removeFromHead();
  }

  ANode<T> removeFromTail() {
    return this.header.removeFromTail();
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

  public abstract ANode<T> removeFromHead();

  public abstract ANode<T> removeFromTail();

  public abstract ANode<T> getNextANode(ANode<T> acc);

  public abstract ANode<T> getPrevANode(ANode<T> acc);
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

  public ANode<T> removeFromHead() {
    return null;
  }


  public ANode<T> removeFromTail() {
    return null;
  }

  public ANode<T> getNextANode(ANode<T> acc) {
    this.prev = acc.prev;
    return this;
  }

  public ANode<T> getPrevANode(ANode<T> acc) {
    this.next = acc.next;
    return this;
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

  public ANode<T> removeFromHead() {
    ANode<T> head = this.next;
    this.next = this.next.getNextANode(this.next);
    return head;
  }


  public ANode<T> removeFromTail() {
    ANode<T> tail = this.prev;
    this.prev = this.prev.getPrevANode(this.prev);
    return tail;
  }

  public ANode<T> getPrevANode(ANode<T> acc) {
    throw new RuntimeException("Cannot get prev of empty list");
  }

  public ANode<T> getNextANode(ANode<T> acc) {
    throw new RuntimeException("Cannot get next of empty list");
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
  }

  boolean testRemoveFromHead(Tester t) {
    initDeque();
    return t.checkException(new RuntimeException("Cannot get next of empty list"), deque1, "removeFromHead")
            && t.checkExpect(deque2.removeFromHead(), abc);
  }

  boolean testRemoveFromTail(Tester t) {
    initDeque();
    return t.checkException(new RuntimeException("Cannot get prev of empty list"), deque1, "removeFromTail")
            && t.checkExpect(deque2.removeFromTail(), def);
  }






}