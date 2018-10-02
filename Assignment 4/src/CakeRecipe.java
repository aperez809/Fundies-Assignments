class CakeRecipe {
  double flour;
  double sugar;
  double eggs;
  double butter;
  double milk;
  boolean areVolume;

  CakeRecipe(double flour, double sugar, double eggs, double butter, double milk) {
    if (flour == sugar) {
      this.flour = flour;
      this.sugar = sugar;
    }
    else {
      invalidArgs(this.flour, this.sugar);

    }

    if (eggs == butter) {
      this.eggs = eggs;
      this.butter = butter;
    }
    else {
      invalidArgs(this.eggs, this.butter);
    }

    if (eggs + milk == sugar) {
      this.milk = milk;
    }
    else {
      invalidArgs(this.eggs, this.milk, this.sugar);
    }
  }

  CakeRecipe(double flour, double eggs, double milk) {
    this.flour = flour;
    this.sugar = flour;
    this.eggs = eggs;
    this.butter = eggs;

    if (eggs + milk == sugar) {
      this.milk = milk;
    }
    else {
      invalidArgs(this.eggs, this.milk, this.sugar);
    }
  }

  CakeRecipe(int flour, int eggs, int milk, boolean areVolume) {
    this(flour, eggs, milk);
    this.areVolume = areVolume;
  }

  Exception invalidArgs(double arg1, double arg2) {
    throw new IllegalArgumentException("Unequal "
            + arg1
            + " + "
            + arg2
            + " arguments: "
            + Double.toString(arg1)
            + ", "
            + Double.toString(arg2));
  }

  Exception invalidArgs(double arg1, double arg2, double arg3) {
    throw new IllegalArgumentException("Weight of "
            + arg1
            + " + "
            + arg2
            + " not equal to "
            + arg3
            + ": "
            + Double.toString(arg1)
            + " + "
            + Double.toString(arg2)
            + " != "
            + Double.toString(arg3));
  }
}
