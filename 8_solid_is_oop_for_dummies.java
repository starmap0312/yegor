// SOLID Is OOP for Dummies
// 1) (S)ingle Responsibility Principle: (vague)
//    a class should have only one reason to change
//    i.e. objects must be problem-centered and responsible for one thing
//         changes to one part should not affect the specification of the class
//    but what that "one thing" is too vauge, because there are different levels of "cohesion"
// 2) (O)pen/Close Principle: (bad)
//    open for extension, but closed for modification
//    this principle is not really applicable to objects and OOP
//    it may work with modules and services, but not with objects ("implementation inheritance" is bad)
// 3) (L)iskov Substitution Principle: (so so)
//    if your method expects a Collection,
//      an ArrayList will & should work too
//    i.e. subtyping: objects in a program should be replaceable with instances of their subtypes
//    but this principle should apply to "variables" and "method calling" too
// 4) (I)nterface Segregation Principle: (good)
//    you must not declare List x,
//      if you only need Collection x or even Iterable x
//    i.e. many client-specific interfaces are better than one general-purpose interface
// 5) (D)ependency Inversion Principle: (good)
//    instead of ArrayList x,
//      you must declare List x and let the provider of the object decide whether it is ArrayList or LinkedList
//    i.e. one should depend upon abstractions, not concrete classes
//
// we should emphasize on the concepts of "cohesion" and "loose coupling" instead
// 
