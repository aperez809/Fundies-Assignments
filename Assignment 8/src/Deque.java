/*

import tester.Tester;

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
}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.prev = null;
    this.next = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;

    if (this.next == null || this.prev == null) {
      throw new IllegalArgumentException("next and prev nodes cannot be null");
    }
    this.next = next;
    this.prev = prev;
  }

  public int size() {
    return 1 + this.next.sizeHelp();
  }

  public int sizeHelp() {
    return 1 + this.next.sizeHelp();
  }
}


class Sentinel<T> extends ANode<T> {

  //constantly updated to be linked to the head and tail of the list, respectively
  Sentinel() {
    super();
  }

  void addAtHead(T newHead) {
    this.prev = new Node<T>(newHead, this.next, this);
  }

  public int size() {
    return this.next.sizeHelp();
  }

  public int sizeHelp() {
    return 0;
  }
}


class ExamplesDeque {

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;

  Sentinel<String> sent1;

  Node<String> abc = new Node<String>("abc");
  Node<String> bcd = new Node<String>("bcd");
  Node<String> cde = new Node<String>("cde");
  Node<String> def = new Node<String>("def");


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
}
*/

import tester.*;

//Represents a boolean-valued question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

//an abstract class for a generic node
abstract class ANode<T> {

  ANode<T> next;
  ANode<T> prev;

  ANode() {
    this.next = this;
    this.prev = this;
  }

  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // helps determine the size of a list
  abstract int sizeHelp();

  //adds a node
  public void addHelp(T t) {
    this.next = new Node<T>(t, this.next, this);
  }

  //helps remove the head from this list
  abstract T removeHelp();

  //helps find the node we want to find
  abstract ANode<T> findHelp(IPred<T> pred);

  //do nothing in this case
  abstract void removeNodeHelp(ANode<T> node);

}

// a class to represent a generic sentinel
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    super();
  }

  // a sentinel has no size
  public int sizeHelp() {
    return 0;
  }

  //can't remove item from empty list
  public T removeHelp() {
    throw new RuntimeException("can't remove item from empty list");
  }

  //if no node is found the sentinel is returned
  public ANode<T> findHelp(IPred<T> pred) {
    return this;
  }

  //do nothing in this case
  public void removeNodeHelp(ANode<T> node) {
    return;
  }
}

// a class to represent a generic node
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
      throw new IllegalArgumentException("one or both of "
              + "the given Nodes is null");
    }
    else {
      next.prev = this;
      prev.next = this;
    }
  }

  //counts the number of nodes that occur after this one
  public int sizeHelp() {
    return 1 + this.next.sizeHelp();
  }

  //removes this item from the list
  public T removeHelp() {
    T data = this.data;
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return data;
  }

  //if this node contains data that we want to find, then return it,
  //else recurse through the list
  public ANode<T> findHelp(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.findHelp(pred);
    }
  }

  //if this node is the given node remove it
  public void removeNodeHelp(ANode<T> node) {
    if (this == node) {
      this.removeHelp();
    }
    else {
      this.next.removeNodeHelp(node);
    }
  }

}

// a class to represent a generic list that can be traversed in either direction
class Deque<T> {

  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  //returns the number of elements in this deque, excluding the sentinel
  int size() {
    return header.next.sizeHelp();
  }

  //consumes a value of type t and adds it to head of list
  //EFFECT: this.header.next points to new node
  // with T as this.data, sentinel as this.prev, and original first node
  // as this.next
  void addAtHead(T t) {
    this.header.addHelp(t);
  }

  //consumes a value of type t and adds it to tail of list
  //EFFECT: this.header.prev points to new node
  // with T as this.data, sentinel as this.next, and original last node
  // as this.prev
  void addAtTail(T t) {
    this.header.prev.addHelp(t);
  }

  //removes the first item from this deque, but throws runtime exception
  // if the only item in this list is a sentinel
  //EFFECT: the sentinel now points to the second item in this list
  T removeFromHead() {
    return this.header.next.removeHelp();
  }


  //removes the first item from this deque, but throws runtime exception
  // if the only item in this list is a sentinel
  //EFFECT: the second to last item now points to the sentinel, and the sentinel
  //prev field now points to this.prev
  T removeFromTail() {
    return this.header.prev.removeHelp();
  }

  //produces the first node in this deque for
  //which given predicate returns true
  ANode<T> find(IPred<T> pred) {
    return this.header.next.findHelp(pred);
  }

  //removes the given node from this deck
  //EFFECT: once the given node is found, this.prev.next is aliased to this.next
  //and this.next.prev is aliased to this.prev, effectively "de-linking" the
  //node that matches this node. if this node isnt found, return nothing
  void removeNode(ANode<T> node) {
    this.header.next.removeNodeHelp(node);
  }

}

class ExamplesDeque {

  //empty deque
  Deque<String> deque1;

  Sentinel<String> sentinel;
  Node<String> abc = new Node<String>("abc");
  Node<String> bcd = new Node<String>("bcd");
  Node<String> cde = new Node<String>("cde");
  Node<String> def = new Node<String>("def");
  Deque<String> deque2;

  Sentinel<String> sentinel2;
  Node<String> jan = new Node<String>("jan");
  Node<String> vitek = new Node<String>("vitek");
  Node<String> is = new Node<String>("is");
  Node<String> the = new Node<String>("the");
  Node<String> best = new Node<String>("best");
  Node<String> teacher = new Node<String>("teacher");
  Node<String> ever = new Node<String>("ever");
  Deque<String> deque3;

  void initData() {
    deque1 = new Deque<String>();
    sentinel = new Sentinel<String>();
    abc = new Node<String>("abc", bcd, sentinel);
    bcd = new Node<String>("bcd", cde, abc);
    cde = new Node<String>("cde", def, bcd);
    def = new Node<String>("def", sentinel, cde);
    deque2 = new Deque<String>(sentinel);
    sentinel2 = new Sentinel<String>();
    jan = new Node<String>("jan", vitek, sentinel2);
    vitek = new Node<String>("vitek", is, jan);
    is = new Node<String>("is", the, vitek);
    the = new Node<String>("the", best, is);
    best = new Node<String>("best", teacher, the);
    teacher = new Node<String>("teacher", ever, best);
    ever = new Node<String>("ever", sentinel2, teacher);
    deque3 = new Deque<String>(sentinel2);
  }

  //tests the size method
  void testSize(Tester t) {
    initData();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 7);
  }

  //tests the add at head method
  void testAddAtHead(Tester t) {
    initData();
    t.checkExpect(deque1, new Deque<String>());
    t.checkExpect(deque2, new Deque<String>(sentinel));
    t.checkExpect(deque3, new Deque<String>(sentinel2));
    deque1.addAtHead("abc");
    deque2.addAtHead("abc");
    deque3.addAtHead("abc");
    t.checkExpect(deque1.size(), 1);
    t.checkExpect(deque2.size(), 5);
    t.checkExpect(deque3.size(), 8);
    Sentinel<String> testsentinel = new Sentinel<String>();
    Node<String> testadd = new Node<String>("abc");
    Node<String> testabc = new Node<String>("abc");
    Node<String> testbcd = new Node<String>("abc");
    Node<String> testcde = new Node<String>("abc");
    Node<String> testdef = new Node<String>("abc");
    testadd = new Node<String>("abc", testabc, testsentinel);
    testabc = new Node<String>("abc", testbcd, testadd);
    testbcd = new Node<String>("bcd", testcde, testabc);
    testcde = new Node<String>("cde", testdef, testbcd);
    testdef = new Node<String>("def", testsentinel, testcde);
    Deque<String> testdeque = new Deque<String>(testsentinel);
    t.checkExpect(deque2, testdeque);
  }

  //tests the add at tail method
  void testAddAtTail(Tester t) {
    initData();
    t.checkExpect(deque1, new Deque<String>());
    t.checkExpect(deque2, new Deque<String>(sentinel));
    t.checkExpect(deque3, new Deque<String>(sentinel2));
    deque1.addAtTail("abc");
    deque2.addAtTail("abc");
    deque3.addAtTail("abc");
    t.checkExpect(deque1.size(), 1);
    t.checkExpect(deque2.size(), 5);
    t.checkExpect(deque3.size(), 8);
    Sentinel<String> testsentinel = new Sentinel<String>();
    Node<String> testadd = new Node<String>("abc");
    Node<String> testabc = new Node<String>("abc");
    Node<String> testbcd = new Node<String>("abc");
    Node<String> testcde = new Node<String>("abc");
    Node<String> testdef = new Node<String>("abc");
    testabc = new Node<String>("abc", testbcd, testsentinel);
    testbcd = new Node<String>("bcd", testcde, testabc);
    testcde = new Node<String>("cde", testdef, testbcd);
    testdef = new Node<String>("def", testadd, testcde);
    testadd = new Node<String>("abc", testsentinel, testdef);
    Deque<String> testdeque = new Deque<String>(testsentinel);
    t.checkExpect(deque2, testdeque);
  }

}


