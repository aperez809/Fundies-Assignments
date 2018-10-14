import tester.Tester;

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
  int compare(T t1, T t2);
}

class BooksByTitle implements IComparator<Book> {

  public int compare(Book b1, Book b2) {
    return b1.title.compareTo(b2.title);
  }
}

class BooksByAuthor implements IComparator<Book> {

  public int compare(Book b1, Book b2) {
    return b1.author.compareTo(b2.author);
  }
}

class BooksByPrice implements IComparator<Book> {

  public int compare(Book b1, Book b2) {
    return b1.price - b2.price;
  }
}

abstract class ABST<T> {
  IComparator<T> order;

  ABST(IComparator<T> order) {
    this.order = order;
  }

  //insert an item at the correct spot in the tree
  abstract ABST<T> insert(T item);

  //returns left most item in the tree
  abstract T getLeftMost();

  abstract T getLeftMostHelp(T data);
}

class Leaf<T> extends ABST<T> {

  Leaf(IComparator<T> order) {
    super(order);
  }

  public ABST<T> insert(T t) {
    return new Node<T>(this.order, t, this, this);
  }

  public T getLeftMost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  public T getLeftMostHelp(T accData) {
    return accData;
  }
}

class Node<T> extends ABST<T> {
  T data;
  ABST left;
  ABST right;

  Node(IComparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }


  public ABST<T> insert(T t) {
    if (this.order.compare(t, this.data) < 0) {
      return new Node<T>(this.order, this.data, this.left.insert(t), this.right);
    } else {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(t));
    }
  }

  public T getLeftMost() {
    return (T) this.left.getLeftMostHelp(this.data);
  }

  public T getLeftMostHelp(T accData) {
    return (T) this.left.getLeftMostHelp(this.data);
  }
}

  class ExamplesTree {

    Book b1 = new Book("War and Peace", "Leo Tolstoy", 15);
    Book b2 = new Book("Jurassic Park", "Michael Crichton", 10);
    Book b3 = new Book("Art of War", "Sun Tzu", 5);
    Book b4 = new Book("How to Design Programs", "Matthias Felleissen", 99999);

    ABST leafBBT = new Leaf<Book>(new BooksByTitle());
    ABST tree1 = new Node<Book>(new BooksByTitle(), b2,
            new Node<Book>(new BooksByTitle(), b4,
                    new Node<Book>(new BooksByTitle(), b3,
                            leafBBT,
                            leafBBT),
                    leafBBT),
            new Node<Book>(new BooksByTitle(), b1,
                    leafBBT,
                    leafBBT));

    boolean testInsert(Tester t) {
      return t.checkExpect(tree1.insert(b1),
              new Node<Book>(new BooksByTitle(), b2,
                      new Node<Book>(new BooksByTitle(), b4,
                              new Node<Book>(new BooksByTitle(), b3,
                                      leafBBT,
                                      leafBBT),
                              leafBBT),
                      new Node<Book>(new BooksByTitle(), b1,
                              leafBBT,
                              new Node<Book>(new BooksByTitle(), b1,
                                      leafBBT,
                                      leafBBT))));
    }
  }



