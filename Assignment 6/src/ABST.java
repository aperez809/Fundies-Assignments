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

  boolean sameBook(Book other) {
    return this.title.equals(other.title)
            && this.author.equals(other.author)
            && this.price == other.price;
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

  public abstract ABST<T> getRight();

  public abstract ABST<T> getRightHelper(Node<T> acc);

  public abstract boolean sameTree(ABST<T> that);

  public abstract boolean sameTreeNode(Node<T> that);

  public abstract boolean sameTreeLeaf(Leaf<T> that);

  public abstract boolean sameData(ABST<T> that);

  public abstract boolean sameDataNode(Node<T> that);

  public abstract boolean sameDataLeaf(Leaf<T> that);

  public abstract IList<T> buildList(IList<T> list);

  public abstract boolean sameAsList(IList<T> that);

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

  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  public ABST<T> getRightHelper(Node<T> acc) {
    return acc.right;
  }

  public boolean sameTree(ABST<T> that) {
    return that.sameTreeLeaf(this);
  }

  public boolean sameTreeNode(Node<T> that) {
    return false;
  }

  public boolean sameTreeLeaf(Leaf<T> that) {
    return true;
  }

  public boolean sameData(ABST<T> that) {
    return that.sameDataLeaf(this);
  }

  public boolean sameDataNode(Node<T> that) {
    return false;
  }

  public boolean sameDataLeaf(Leaf<T> that) {
    return true;
  }

  public IList<T> buildList(IList<T> list) {
    return list.reverse();
  }

  public boolean sameAsList(IList<T> that) {
    return true;
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

  public ABST<T> getRight() {
    return this.left.getRightHelper(this);
  }

  public ABST<T> getRightHelper(Node<T> acc) {
    return new Node<T>(acc.order, acc.data, this.left.getRightHelper(this),
            acc.right);
  }


  public boolean sameTree(ABST<T> that) {
    return that.sameTreeNode(this);
  }

  public boolean sameTreeNode(Node<T> that) {
    return this.data.equals(that.data)
            && this.left.sameTree(that.left)
            && this.right.sameTree(that.right);
  }

  public boolean sameTreeLeaf(Leaf<T> that) {
    return false;
  }

  public boolean sameData(ABST<T> that) {
    return that.sameDataNode(this);
  }

  public boolean sameDataNode(Node<T> that) {
    return this.order.compare(this.getLeftMost(), that.getLeftMost()) == 0
            && this.getRight().sameData(that.getRight());
  }

  public boolean sameDataLeaf(Leaf<T> that) {
    return false;
  }

  public IList<T> buildList(IList<T> list) {
    return this.getRight().buildList(new ConsList<T>(this.getLeftMost(), list));
  }

  public boolean sameAsList(IList<T> that) {
    return this.buildList(new MtList<T>()).compareLists(that);
  }
}




interface IList<T> {
  IList<T> reverse();

  IList<T> append(T first);

  boolean compareLists(IList<T> that);

  boolean sameCons(ConsList<T> that);

  boolean sameMT(MtList<T> that);

  ABST<T> buildTree(ABST<T> acc);
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  //reverse the order of the list
  public IList<T> reverse() {
    return this.rest.reverse().append(this.first);
  }

  //add an item to the end of the list
  public IList<T> append(T first) {
    return new ConsList<T>(this.first, this.rest.append(first));
  }

  public boolean compareLists(IList<T> that) {
    return that.sameCons(this);
  }

  public boolean sameCons(ConsList<T> that) {
    return this.first.equals(that.first)
            && this.rest.compareLists(that.rest);
  }

  public boolean sameMT(MtList<T> that) {
    return false;
  }


  //inserts the first tree in this list into the given list
  public ABST<T> buildTree(ABST<T> acc) {
    return this.rest.buildTree(acc.insert(this.first));
  }

}

class MtList<T> implements IList<T> {

  public IList<T> reverse() {
    return this;
  }

  public IList<T> append(T first) {
    return new ConsList<T>(first, this);
  }

  public boolean compareLists(IList<T> that) {
    return true;
  }

  public boolean sameCons(ConsList<T> that) {
    return false;
  }

  public boolean sameMT(MtList<T> that) {
    return true;
  }

  public ABST<T> buildTree(ABST<T> acc) {
    return acc;
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
  ABST tree2 =
            new Node<Book>(new BooksByTitle(), b2,
              new Node<Book>(new BooksByTitle(), b4,
                    new Node<Book>(new BooksByTitle(), b3,
                            leafBBT,
                            leafBBT),
                    leafBBT),
                    leafBBT);

  IList<Book> list1 = new ConsList<Book>(b3,
          new ConsList<Book>(b4,
                  new ConsList<Book>(b2,
                          new ConsList<Book>(b1,
                                  new MtList<Book>()))));

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

  boolean testGetLeftMost(Tester t) {
    return t.checkExpect(tree1.getLeftMost(), b3);
  }

  boolean testGetRight(Tester t) {
    return t.checkExpect(tree1.getRight(),
              new Node<Book>(new BooksByTitle(), b2,
                      new Node<Book>(new BooksByTitle(), b4,
                              leafBBT,
                              leafBBT),
                      new Node<Book>(new BooksByTitle(), b1,
                              leafBBT,
                              leafBBT)));
  }

  boolean testSameTree(Tester t) {
    return t.checkExpect(tree1.sameTree(tree1), true);
  }

  boolean testSameData(Tester t) {
    return t.checkExpect(tree1.sameTree(tree1), true)
              && t.checkExpect(tree2.sameTree(tree1), false);
  }

  boolean testBuildList(Tester t) {
    return t.checkExpect(tree1.buildList(new MtList()), list1);
  }

  boolean testSameAsList(Tester t) {
    return t.checkExpect(tree1.sameAsList(list1), true);
  }

  boolean testBuildTree(Tester t) {
    return t.checkExpect(list1.buildTree(new Leaf<>(new BooksByTitle())), tree1);
  }
}



