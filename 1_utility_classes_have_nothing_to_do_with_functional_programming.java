// Utility Classes Have Nothing to Do With Functional Programming
// Imperative programming vs. Declarative programming
// 1) imperative programming
//    focus on describing how a program operates in terms of statements that change a program state
//    ex.
public class MyMath {
    public double f(double a, double b) {
        double max = Math.max(a, b);
        double x = Math.abs(max);
        return x;
    }
}

// 2) Declarative programming
//    focus on what the program should accomplish without prescribing how to do it (actions to be taken)
//    ex1.
(defun f (a b) (abs (max a b)))

//    ex2. the maximum between two numbers will only be calculated when starting using it (in Lisp)
(let (x (max 1 5))          // get a promise of the maximum value of 1 and 5
  (print "X equals to " x)) // until print starts to output characters, the function max won't be called

// three roles in programming scenario
// 1) a buyer of the result (product)
// 2) a packager of the result (product)
// 3) a consumer of the result (product)
// ex. imperative programming
public void foo() {                       // 3) a consumer of the result (product)
    double x = this.calc(5, -7);
    System.out.println("max+abs equals to " + x);
} // the buyer gets the result (product) and presents it to the consumer

private double calc(double a, double b) { // 1) a buyer of the result (product)
    double x = Math.f(a, b);              // 2) a packager of the result (product)
    return x;
} // the buyer asks the packager to package the result (product) and presents it to him
  // the result (product) is ready to be used immediately

// ex. declarative programming
//     the buyer gets a promise instead and presents it to the consumer
//     only when the consumer decides to convert it to result (product), the result is packaged and
//       presented to him
// imperative: a product is ready to be used
// declarative: a promise for the product which can later be converted into a real product

// 
//   functional programming:
//     declarative: focuses on "what" the program should accomplish without prescribing how to do it
//       (the client only knows of the interface the object subscribed)
//     a function can be assigned to a variable (based on lambda calculus)
//   utility class methods:
//     imperative: ex. Math or StringUtils return products ready to be used immediately
//     you can't pass a static method as an argument to another method
//       (they are procedures, i.e. Java statements grouped under a unique name)
//
// example: Java static functions are imperative
public class MyMath {
    public double f(double a, double b) { // a buyer
        return Math.abs(Math.max(a, b));  // a packager
    }
} // the result is ready to be used immediately (i.e. delivers the result here and now)

// example: functional programming
(defun foo (x) (x 5))   // define function foo (packager that delivers result/product)
// some time later
(defun bar (x) (+ x 1)) // define function bar
(print (foo bar))       // pass function bar (result/product) as an argument to function foo

// although utility classes look more like functional programming, but they are not
//   you can't pass a static method as an argument to another method
//   static methods are Java statements grouped under a unique name

// why are utility class methods bad?
//   Testability: calls to static methods in utility classes are hard-coded dependencies
//     there is no way to replace them with test-doubles when testing
//     ex. calling FileUtils.readFile() cannot be tested without a real file on disk
//   Efficiency: utility class methods are less efficient
//     ex. StringUtils.split() breaks the string down right now, even if only the first one is required later
//     (declarative programming instead returns a promise and may gain performance based on the usage)
//   Readability: utility classes tend to be huge
//     ex. StringUtils or FileUtils
//
// example:
//
// (bad desgin)

public class Math {

    public static double abs(double a);
    // many other utility methods
}

// the client code
double x = Math.abs(3.1415926d);

// (good design)

public class MyMath {
    public double f(double a, double b) {
        double max = Math.max(a, b);
        double x = Math.abs(max);
        return x;
    }
}
