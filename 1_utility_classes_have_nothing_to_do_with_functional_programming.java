// Utility Classes Have Nothing to Do With Functional Programming
// Imperative programming vs. Declarative programming
// 1) imperative programming
//    focus on describing "how" a program operates in terms of statements that change a program state
//    ex.
public class MyMath {
    public double f(double a, double b) {
        double max = Math.max(a, b);      // get result immediately
        double x = Math.abs(max);         // get result immediately
        return x;
    }
}
// refactored
public class MyMath {
    public double f(double a, double b) {
        return Math.abs(Math.max(a, b));
    }
}

// 2) declarative programming
//    focus on "what" the program should accomplish without prescribing how to do it (actions to be taken)
//    ex1. (Lisp)
(defun f (a b) (abs (max a b)))
//    ex1. (Scala)
def f(a: Int, b: Int): Int = math.abs(math.max(a, b)) // both abs() and max() are methods

//    ex2. (Lisp) the maximum between two numbers will only be calculated when starting using it
(let (x (max 1 5))          // get a promise of the maximum value of 1 and 5
  (print "X equals to " x)) // until print starts to output characters, the function max won't be called
//    ex2. (Scala)
def x = math.max(1, 5)
println("X equals to " + x) // X equals to 5

// three roles in programming scenario
// 1) a buyer of the result
// 2) a packager of the result
// 3) a consumer of the result
// ex. imperative programming
public void foo() {                       // 3) a consumer of the result
    double x = this.calc(5, -7);
    System.out.println("max+abs equals to " + x);
} // the buyer gets the result and presents it to the consumer

private double calc(double a, double b) { // 1) a buyer of the result
    double x = Math.f(a, b);              // 2) a packager of the result
    return x;
} // the buyer asks the packager to package the result and presents it to him
  // the result is ready to be used immediately
//
// ex. declarative programming
//     the buyer gets a promise instead and presents it to the consumer
//     only when the consumer decides to convert it to result, the result is packaged and
//       presented to him
//
// imperative: a product is ready to be used
// declarative: a promise for the product which can later be converted into a real product

// 3) functional programming vs. utility class methods
// 3.1) functional programming:
//      declarative: focuses on "what" the program should accomplish without prescribing how to do it
//      (the client only knows of the interface the object subscribed)
//      a function can be assigned to a variable (based on lambda calculus)
// 3.2) utility class methods:
//      imperative: ex. Math or StringUtils return products ready to be used immediately
//      you can't pass a static method as an argument to another method
//      (they are procedures, i.e. Java statements grouped under a unique name)
//
// example: Java static functions are imperative
public class MyMath {
    public static double f(double a, double b) { // a buyer
        return Math.abs(Math.max(a, b));         // a packager
    }
} // the result is ready to be used immediately (i.e. delivers the result here and now)

// example: functional programming
(defun foo (x) (x 5))   // define function foo (packager that delivers result/product)
// some time later
(defun bar (x) (+ x 1)) // define function bar
(print (foo bar))       // pass function bar (result/product) as an argument to function foo

// although utility classes look more like functional programming, but they are not
// 1) you cannot pass a static method as an argument to another method (as the static method gets called/evaluated immediately)
// 2) static methods are Java statements grouped under a unique name

// why are utility class methods bad?
// 1) Testability: calls to static methods in utility classes are hard-coded dependencies
//    there is no way to replace them with test-doubles when testing
//    ex. calling FileUtils.readFile() cannot be tested without a real file on disk
// 2) Efficiency: utility class methods are less efficient
//    ex. StringUtils.split() breaks the string down right now, even if only the first one is required later
//    (declarative programming instead returns a promise and may gain performance based on the usage)
// 3) Readability: utility classes tend to be huge
//    ex. StringUtils or FileUtils
//
// example:
//
// (bad desgin: static utility methods)
public class Math {
    public static double abs(double a);
    // many other utility methods
}

// the client code
double x = Math.abs(3.1415926d);

public class MyMath {
    public double f(double a, double b) {
        double max = Math.max(a, b);
        double x = Math.abs(max);
        return x;
    }
}
