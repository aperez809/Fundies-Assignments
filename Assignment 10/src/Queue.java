class Queue<T> {
  Deque<T> contents;

  public Queue(Deque<T> contents) {
    this.contents = contents;
  }

  // adds an item to the tail of the list
  void enqueue(T item) {
    this.contents.addAtTail(item);
  }

  boolean isEmpty() {
    return this.contents.size() == 0;
  }

  // removes and returns the head of the list
  T dequeue() {
    return this.contents.removeFromHead();
  }
}