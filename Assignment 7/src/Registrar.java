import tester.Tester;

interface IPred<T> {
  boolean apply(T t);
}

class ClassmatesPred implements IPred<Student> {
  int id;

  ClassmatesPred(int id) {
    this.id = id;
  }

  public boolean apply(Student s) {
    return this.id == s.id;
  }
}


interface IList<T> {

  boolean findSame(IList<T> other);

  boolean inBothLists(T first);

  int sameInstructorCount(IList<T> other);

  int sameInstructorCountCons(ConsList<T> other);

  int sameInstructorCountMt(MtList<T> other);
}


class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean findSame(IList<T> other) {
    return other.inBothLists(this.first)
            || this.rest.findSame(other);
  }

  public boolean inBothLists(T item) {
    return item.equals(this.first)
            || this.rest.inBothLists(item);
  }


  public int sameInstructorCount(IList<T> other) {
    return other.sameInstructorCountCons(this);
  }


  public int sameInstructorCountCons(ConsList<T> other) {
    if(this.inBothLists(other.first)) {
      return 1 + this.rest.sameInstructorCount(other.rest);
    }

    else {
      return this.rest.sameInstructorCount(other.rest);
    }
  }

  public int sameInstructorCountMt(MtList<T> other) {
    return 0;
  }
}


class MtList<T> implements IList<T> {

  public boolean findSame(IList<T> other) {
    return false;
  }

  public boolean inBothLists(T first) {
    return false;
  }

  public int sameInstructorCount(IList<T> other) {
    return other.sameInstructorCountMt(this);
  }

  public int sameInstructorCountCons(ConsList<T> other) {
    return 0;
  }

  public int sameInstructorCountMt(MtList<T> other) {
    return 0;
  }
}


class Instructor {
  String name;
  IList<Course> courses;

  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  boolean dejavu(Student s) {
    return this.courses.sameInstructorCount(s.courses) > 1;
  }
}


class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>()
    ;
  }

  void addStudent(Student s) {
    if (!this.students.inBothLists(s)) {
      this.students = new ConsList<Student>(s, this.students);
    }
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;

  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  void enroll(Course c) {
    if (!this.courses.inBothLists(c)) {
      this.courses = new ConsList<Course>(c, this.courses);
      c.addStudent(this);
    }
  }

  boolean classmates(Student s) {
    return this.courses.findSame(s.courses);
  }
}


class ExamplesSchool {

  Student alex;
  Student jules;
  Student bob;
  Student chris;
  Student john;

  Instructor leena;
  Instructor clark;

  Course fundies;
  Course discrete;
  Course ood;
  Course finance;

  void initTestConditions() {

    Student alex  = new Student("Alex", 001);
    Student jules = new Student("Jules", 002);
    Student bob   = new Student("Bob", 003);
    Student chris = new Student("Chris", 004);
    Student john  = new Student("John", 005);

    Instructor leena = new Instructor("Leena");
    Instructor clark = new Instructor("Clark");

    Course fundies = new Course("Fundies", leena);
    Course discrete = new Course("Discrete", clark);
    Course ood = new Course("Object Oriented Design", leena);
    Course finance = new Course("Financial Management", clark);
  }


  void testEnroll(Tester t) {
    this.initTestConditions();
    t.checkExpect(fundies.students, new MtList<Student>());
    alex.enroll(fundies);
    t.checkExpect(fundies.students, new ConsList<Student>(alex, new MtList<Student>()));

  }
}



