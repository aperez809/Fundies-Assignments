import tester.Tester;

interface IList<T> {

  //True if finds the same IList<T> value as other
  boolean findSame(IList<T> other);

  //True if T value is in both lists
  boolean contains(T first);

  ///Count for number of same instructors in IList<T>
  int sameInstructorCount(IList<T> other);

  ///Count for number of same instructors in Conslist<T>
  int sameInstructorCountCons(ConsList<T> other);

  ///Counts the number of times the instructor is the
  //same in an empty list<T>
  int sameInstructorCountMt(MtList<T> other);
}


class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  //finds the same value in the list as other
  public boolean findSame(IList<T> other) {
    return other.contains(this.first)
            || this.rest.findSame(other);
  }

  ///returns true if item T is in both list
  public boolean contains(T item) {
    return item.equals(this.first)
            || this.rest.contains(item);
  }

  //counts the number of times the instructor appears in the list
  public int sameInstructorCount(IList<T> other) {
    return other.sameInstructorCountCons(this);
  }

  //If sameInstructor is found in both list 1 is added
  public int sameInstructorCountCons(ConsList<T> other) {
    if (this.contains(other.first)) {
      return 1 + this.rest.sameInstructorCount(other.rest);
    }

    else {
      return this.rest.sameInstructorCount(other.rest);
    }
  }

  //Returns 0 since there are no instructors in an empty list
  public int sameInstructorCountMt(MtList<T> other) {
    return 0;
  }
}


class MtList<T> implements IList<T> {

  //returns false since no objects are the same in an emptu list
  public boolean findSame(IList<T> other) {
    return false;
  }

  //returns false since nothing is in both lists if they are empty
  public boolean contains(T first) {
    return false;
  }

  //counts the number of the same instuructors in an empty list
  public int sameInstructorCount(IList<T> other) {
    return other.sameInstructorCountMt(this);
  }

  //returns 0 for sameInstructor since this is empty class
  public int sameInstructorCountCons(ConsList<T> other) {
    return 0;
  }

  //returns 0 for sameInstructor since empty class
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

  //does this student appear in more than 1 of the instructors courses
  boolean dejavu(Student s) {
    return this.courses.sameInstructorCount(s.courses) > 1;
  }

  //add the given course to this instructors list of courses
  void addCourse(Course c) {
    if (!this.courses.contains(c)) {
      this.courses = new ConsList<Course>(c, this.courses);
    }
  }
}


class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
  }

  //add the given student to the course roster
  void addStudent(Student s) {
    if (!this.students.contains(s)) {
      this.students = new ConsList<Student>(s, this.students);
      prof.addCourse(this);
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

  //add the given course to the student's list of courses
  void enroll(Course c) {
    if (!this.courses.contains(c)) {
      this.courses = new ConsList<Course>(c, this.courses);
      c.addStudent(this);
    }
  }

  //boolean for if students are in same course.
  boolean classmates(Student s) {
    return this.courses.findSame(s.courses);
  }
}

class ExamplesRegistrar {

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

  IList<Course> loc1;
  IList<Course> loc2;
  IList<Course> loc3;
  IList<Course> mtCourse;

  IList<Student> los1;
  IList<Student> los2;
  IList<Student> los3;
  IList<Student> mtStudent;

  IList<Instructor> loi1;
  IList<Instructor> loi2;
  IList<Instructor> loi3;
  IList<Instructor> mtInstructor;




  void initTestConditions() {

    alex  = new Student("Alex", 001);
    jules = new Student("Jules", 002);
    bob   = new Student("Bob", 003);
    chris = new Student("Chris", 004);
    john  = new Student("John", 005);

    leena = new Instructor("Leena");
    clark = new Instructor("Clark");

    fundies = new Course("Fundies", leena);
    discrete = new Course("Discrete", clark);
    ood = new Course("Object Oriented Design", leena);
    finance = new Course("Financial Management", clark);

    loc1 = new ConsList<Course>(finance,
            new ConsList<Course>(fundies,
                    new MtList<Course>()));
    loc2 = new ConsList<Course>(ood,
            new ConsList<Course>(discrete,
                    new MtList<Course>()));
    loc3 = new ConsList<Course>(discrete,
            new ConsList<Course>(finance,
                    new MtList<Course>()));
    mtCourse = new MtList<Course>();

    los1 = new ConsList<Student>(alex,
            new ConsList<Student>(jules,
                    new MtList<Student>()));

    los2 = new ConsList<Student>(alex,
            new ConsList<Student>(john,
                    new MtList<Student>()));
    los3 = new ConsList<Student>(bob,
            new ConsList<Student>(chris,
                    new MtList<Student>()));
    mtStudent  = new MtList<Student>();

    loi1 = new ConsList<Instructor>(clark,
            new MtList<Instructor>());
    loi2 = new ConsList<Instructor>(leena,
            new MtList<Instructor>());
    loi3 = new ConsList<Instructor>(leena,
            new ConsList<Instructor>(clark,
                    new MtList<Instructor>()));
    mtInstructor = new MtList<Instructor>();


  }

  void testEnroll(Tester t) {
    initTestConditions();
    t.checkExpect(alex.courses, new MtList<Course>());
    alex.enroll(fundies);
    t.checkExpect(alex.courses, new ConsList<Course>(fundies, new MtList<Course>()));
    jules.enroll(fundies);
    jules.enroll(finance);
    t.checkExpect(jules.courses, new ConsList<Course>(finance,
            new ConsList<Course>(fundies,
                    new MtList<Course>())));
  }

  boolean testFindSame(Tester t) {
    initTestConditions();
    return t.checkExpect(loc1.findSame(loc2), false)
            && t.checkExpect(loc2.findSame(loc3), true)
            && t.checkExpect(loc3.findSame(mtCourse), false)
            && t.checkExpect(los1.findSame(los2), true)
            && t.checkExpect(los2.findSame(los3), false)
            && t.checkExpect(los3.findSame(mtStudent), false)
            && t.checkExpect(loi1.findSame(loi2), false)
            && t.checkExpect(loi2.findSame(loi3), true)
            && t.checkExpect(loi3.findSame(mtInstructor), false);

  }

  boolean testContains(Tester t) {
    initTestConditions();
    return t.checkExpect(loc1.contains(finance), true)
            && t.checkExpect(loc2.contains(fundies), false)
            && t.checkExpect(mtCourse.contains(ood), false)
            && t.checkExpect(los1.contains(alex), true)
            && t.checkExpect(los2.contains(jules), false)
            && t.checkExpect(mtStudent.contains(bob), false)
            && t.checkExpect(loi1.contains(clark), true)
            && t.checkExpect(loi2.contains(clark), false)
            && t.checkExpect(mtInstructor.contains(leena), false);
  }

  boolean testClassmates(Tester t) {
    initTestConditions();
    alex.enroll(ood);
    jules.enroll(ood);
    return t.checkExpect(alex.classmates(jules), true)
            && t.checkExpect(alex.classmates(bob), false)
            && t.checkExpect(chris.classmates(john), false);
  }

  boolean testDejavu(Tester t) {
    initTestConditions();
    alex.enroll(ood);
    bob.enroll(finance);
    bob.enroll(discrete);
    return t.checkExpect(clark.dejavu(bob), true)
            && t.checkExpect(leena.dejavu(chris), false);
  }

  void testAddStudent(Tester t) {
    initTestConditions();
    t.checkExpect(fundies.students, new MtList<Student>());
    chris.enroll(fundies);
    bob.enroll(fundies);
    t.checkExpect(fundies.students, new ConsList<Student>(bob,
            new ConsList<Student>(chris, new MtList<Student>())));
  }

  void testAddCourse(Tester t) {
    initTestConditions();
    t.checkExpect(leena.courses, new MtList<Course>());
    leena.addCourse(ood);
    leena.addCourse(fundies);
    t.checkExpect(leena.courses, new ConsList<Course>(fundies,
            new ConsList<Course>(ood, new MtList<Course>())));
  }
}



