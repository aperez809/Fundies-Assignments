import tester.Tester;

class CakeRecipe {
  double flour;
  double sugar;
  double eggs;
  double butter;
  double milk;
  boolean areVolume;


  /* Template

  Fields:
  this.flour ... double
  this.sugar ... double
  this.eggs ... double
  this.butter ... double
  this.milk ... double
  this.areVolume ... boolean

  Methods:
  this.sameRecipe ... boolean


   */

  CakeRecipe(double flour, double sugar, double eggs, double butter, double milk) {
    this.flour = new Utils().checkEquality(flour, sugar, "Unequal flour and sugar args");
    this.sugar = new Utils().checkEquality(sugar, flour, "Unequal flour and sugar args");
    this.eggs = new Utils().checkEquality(eggs, butter, "Unequal flour and sugar args");
    this.butter = new Utils().checkEquality(butter, eggs, "Unequal flour and sugar args");
    this.milk = new Utils().checkSumEquality(milk, eggs, sugar, "Milk + eggs != flour");
    this.areVolume = areVolume;
  }

  CakeRecipe(double flour, double eggs, double milk) {
    this.flour = flour;
    this.sugar = flour;
    this.eggs = eggs;
    this.butter = eggs;
    this.milk = new Utils().checkSumEquality(milk, eggs, flour, "Milk + eggs != flour");
  }

  CakeRecipe(double flour, double eggs, double milk, boolean areVolume) {
    this.flour = flour * 4.25;
    this.sugar = flour * 4.25;
    this.eggs = eggs * 1.75;
    this.butter = eggs * 1.75;
    this.milk = new Utils().checkSumEquality(
            milk * 8, this.eggs, this.flour, "Milk + eggs != flour");
    this.areVolume = areVolume;
  }

  //is this CakeRecipe the same as the other?
  boolean sameRecipe(CakeRecipe other) {
    return (this.flour - other.flour < 0.0001)
            && (this.sugar - other.sugar < 0.0001)
            && (this.eggs - other.eggs < 0.0001)
            && (this.butter - other.butter < 0.0001)
            && (this.milk - other.milk < 0.0001);
  }
}

class Utils {
  double checkEquality(double val, double other, String msg) {
    if (Math.abs(val - other) < 0.0001) {
      return val;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }

  double checkSumEquality(double val, double other, double total, String msg) {
    if (total == val + other) {
      return val;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }

  int checkSumEquality(int val, int other, int total, String msg) {
    if (total == val + other) {
      return val;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }
}

class ExamplesRecipes {
  CakeRecipe cake1 = new CakeRecipe(5.0,5.0,3.0,3.0,2.0);
  CakeRecipe cake2 = new CakeRecipe(10.0, 4.0, 6.0);
  CakeRecipe cake3 = new CakeRecipe(20, 12, 8, true);
  CakeRecipe cake4 = new CakeRecipe(85.0, 85.0, 21.0, 21.0, 64.0);



  boolean testConstructors(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException("Milk + eggs != flour"),
            "CakeRecipe",
            20.0,20.0,9.0,9.0,12.0)

            && t.checkConstructorException(
                    new IllegalArgumentException("Unequal flour and sugar args"),
            "CakeRecipe",
            20.0,19.0,9.0,9.0,11.0);

  }

  boolean testSameRecipe(Tester t) {
    return t.checkExpect(cake1.sameRecipe(cake1), true)
            && t.checkExpect(cake2.sameRecipe(cake1), false)
            && t.checkExpect(cake3.sameRecipe(cake2), false)
            && t.checkExpect(cake3.sameRecipe(cake3), true)
            && t.checkExpect(cake3.sameRecipe(cake4), true);
  }
}

