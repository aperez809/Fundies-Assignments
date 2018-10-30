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
    this.next = null;
    this.prev = null;
  }

  public abstract int size();

  public abstract int sizeHelp();
}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
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
    this.next = this;
    this.prev = this;
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

  Node<String> abc ;
  Node<String> bcd;
  Node<String> cde;
  Node<String> def;


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
