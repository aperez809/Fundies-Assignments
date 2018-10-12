class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

interface IComparator<T> {
  boolean apply(IComparator pred);
}

class BooksByTitle implements IComparator {

  @Override
  public boolean apply(IComparator pred) {
    return false;
  }
}

abstract class ABST<T> {
  IComparator<T> order;

  ABST(IComparator<T> order) {
    this.order = order;
  }
}

class Leaf<T> extends ABST<T> {

  Leaf(IComparator<T> order) {
    super(order);
  }
}

class Node<T> extends ABST<T> {
  T data;
  ABST left;
  ABST right;

  public Node(IComparator<T> order, T data, ABST left, ABST right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }
}


