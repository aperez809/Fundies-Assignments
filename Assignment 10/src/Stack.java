class Stack<T> {
  Deque<T> contents;

  public Stack(Deque<T> contents) {
    this.contents = contents;
  }

  // adds an item to the head of the list
  void push(T item) {
    this.contents.addAtHead(item);
  }
  boolean isEmpty() {
    return this.contents.size() == 0;
  }

  // removes and returns the head of the list
  T pop() {
    return this.contents.removeFromHead();
  }

  int size() {
    return this.contents.size();
  }
}