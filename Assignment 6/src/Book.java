import tester.Tester;

import java.time.temporal.Temporal;

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
  boolean apply(T t);
}

class BooksByTitle implements IComparator<Book> {

  public boolean apply(Book b) {
    return (this.title.compareTo(b.title)) < 0;
  }
}

class BooksByAuthor implements IComparator<Book> {

  public boolean apply(Book b) {
    return (this.author.compareTo(b.author)) < 0;
  }
}

class BooksByPrice implements IComparator<Book> {

  public boolean apply(Book b) {
    return (this.price < (b.price)) < 0;
  }
}

abstract class ABST<T> {
  IComparator<T> order;

  ABST(IComparator<T> order) {
    this.order = order;
  }

  public abstract ABST insert(IComparator<T> criteria, T item);
}

class Leaf<T> extends ABST<T> {

  Leaf(IComparator<T> order) {
    super(order);
  }

  public ABST insert(IComparator<T> criteria, T item) {
    return new Leaf<T>(criteria);
  }
}

class Node<T> extends ABST<T> {
  T data;
  ABST left;
  ABST right;

  Node(IComparator<T> order, T data, ABST left, ABST right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  public ABST<T> insert(IComparator<T> criteria, T item) {
    if (criteria.apply(this.data)) {
      return new Node<T>(criteria, item, this.left, this.right.insert(criteria, item));
    }
    else {
      return new Node<T>(criteria, this.data, this.left, this.right);
    }
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
    return t.checkExpect(tree1.insert(new BooksByTitle(), b1),
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


