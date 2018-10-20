import tester.Tester;

interface IArith {

  <R> R accept(IArithVisitor<R> visitor);
}


interface IArithVisitor<R> extends IFunc<IArith, R> {

  R visitConst(Const c);

  R visitFormula(Formula f);
}

interface IFunc<A, R> {

  //applies this function
  R apply(A a);
}

interface IFunc2<A1, A2, R> {
  R apply(A1 a1, A2 a2);
}

class Const implements IArith {
  double num;

  Const(double num) {
    this.num = num;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}


class Formula implements IArith, IFunc2<Double, Double, Double> {
  IFunc2<Double, Double, Double> fun;
  String name;
  IArith left;
  IArith right;

  Formula(IFunc2<Double, Double, Double> fun, String name, IArith left, IArith right) {
    this.fun = fun;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  public Double apply(Double a1, Double a2) {
    return 0.0;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitFormula(this);
  }

}

class Addition implements IFunc2<Double, Double, Double> {

  public Double apply(Double a1, Double a2) {
    return a1 + a2;
  }
}

class Subtraction implements IFunc2<Double, Double, Double> {

  public Double apply(Double a1, Double a2) {
    return a1 - a2;
  }
}

class Multiplication implements IFunc2<Double, Double, Double> {

  public Double apply(Double a1, Double a2) {
    return a1 * a2;
  }
}

class Division implements IFunc2<Double, Double, Double> {

  public Double apply(Double a1, Double a2) {
    return a1 / a2;
  }
}


class EvalVisitor implements IArithVisitor<Double> {
  public Double apply(IArith arith) {
    return arith.accept(this);
  }

  public Double visitConst(Const c) {
    return c.num;
  }

  public Double visitFormula(Formula f) {
    return f.fun.apply(f.left.accept(this), f.right.accept(this));
  }
}

class PrintVisitor implements IArithVisitor<String> {

  public String apply(IArith a) {
    return a.accept(this);
  }

  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  public String visitFormula(Formula f) {
    return "("
            + f.name
            + " "
            + f.left.accept(this)
            + " "
            + f.right.accept(this)
            + ")";
  }
}

class DoublerVisitor implements IArithVisitor<IArith> {

  public IArith apply(IArith a) {
    return a.accept(this);
  }

  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }

  public IArith visitFormula(Formula f) {
    return new Formula(f.fun, f.name, f.left.accept(this), f.right.accept(this));
  }
}

class AllSmallVisitor implements IArithVisitor<Boolean> {

  public Boolean apply(IArith a) {
    return a.accept(this);
  }

  public Boolean visitConst(Const c) {
    return c.num < 10;
  }

  public Boolean visitFormula(Formula f) {
    return f.left.accept(this) && f.right.accept(this);
  }
}

class NoDivBy0 implements IArithVisitor<Boolean> {

  public Boolean apply(IArith a) {
    return a.accept(this);
  }

  public Boolean visitConst(Const c) {
    return true;
  }

  public Boolean visitFormula(Formula f) {
    if (f.name.equals("div")) {
      return Math.abs(f.right.accept(new EvalVisitor())) > 0.0001
              && f.left.accept(this)
              && f.right.accept(this);
    } else {
      return f.left.accept(this)
              && f.right.accept(this);
    }

  }
}


class ExamplesVisitors {

  IFunc2<Double, Double, Double> plus = new Addition();
  IFunc2<Double, Double, Double> div = new Division();
  IFunc2<Double, Double, Double> sub = new Subtraction();
  IFunc2<Double, Double, Double> mult = new Multiplication();


  IArith a1 = new Const(0.0);
  IArith a2 = new Const(5.5);
  IArith a3 = new Const(1.1);
  IArith a4 = new Formula(plus, "plus", a2, a3);
  IArith a5 = new Formula(div, "div", a4, a3);
  IArith a6 = new Const(99.9);
  IArith a7 = new Formula(div, "div", a3, a1);
  IArith a8 = new Formula(sub, "sub", a3, a3);
  IArith a9 = new Formula(div, "div", a3, a8);
  IArith a10 = new Const(10000.0);
  IArith a11 = new Formula(mult, "mult", a1, a2);
  IArith a12 = new Formula(mult, "mult", a9, a10);


  boolean testEvalVisitors(Tester t) {
    return t.checkExpect(a2.accept(new EvalVisitor()), 5.5)
            && t.checkExpect(a4.accept(new EvalVisitor()), 6.6)
            && t.checkExpect(a8.accept(new EvalVisitor()), 0.0);
  }

  boolean testPrintVisitors(Tester t) {
    return t.checkExpect(a4.accept(new PrintVisitor()), "(plus 5.5 1.1)")
            && t.checkExpect(a8.accept(new PrintVisitor()), "(sub 5.5 5.5)")
            && t.checkExpect(a9.accept(new PrintVisitor()), "(div 1.1 (sub 5.5 5.5))");
  }

  boolean testDoublerVisitor(Tester t) {
    return t.checkExpect(a1.accept(new DoublerVisitor()), new Const(0.0))
            && t.checkExpect(a2.accept(new DoublerVisitor()), new Const(11.0))
            && t.checkExpect(a4.accept(new DoublerVisitor()),
            new Formula(plus, "plus", new Const(11.0), new Const(2.2)));
  }

  boolean testAllSmallVisitor(Tester t) {
    return t.checkExpect(a1.accept(new AllSmallVisitor()), true)
            && t.checkExpect(a10. accept(new AllSmallVisitor()), false)
            && t.checkExpect(a8.accept(new AllSmallVisitor()), true);
  }

  boolean testNoDiv0Visitor(Tester t) {
    return t.checkExpect(a5.accept(new NoDivBy0()), true)
            && t.checkExpect(a7.accept(new NoDivBy0()), false)
            && t.checkExpect(a9.accept(new NoDivBy0()), false);
  }
}