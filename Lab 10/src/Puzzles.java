import tester.Tester;

import java.util.ArrayList;

class Utils {
  <T> ArrayList<T> reverse(ArrayList<T> source) {
    Stack<T> s = new Stack<>(new Deque<>());

    for (T item: source) {
      s.push(item);
    }

    ArrayList<T> tmp = new ArrayList<>();
    for (int i = 0; i < source.size(); i++) {
      tmp.add(s.pop());
    }

    return tmp;
  }
}

class Examples {
  ArrayList<Integer> arr1;
  ArrayList<Integer> revArr1;

  void initData() {
    arr1 = new ArrayList<>();
    arr1.add(1);
    arr1.add(2);
    arr1.add(3);

    revArr1 = new ArrayList<>();
    revArr1.add(3);
    revArr1.add(2);
    revArr1.add(1);
  }

  boolean testReverse(Tester t) {
    initData();
    return t.checkExpect(new Utils().reverse(arr1), revArr1);
  }
}