// SOLID Is OOP for Dummies
// 1) (S)ingle Responsibility Principle: (vague)
//    a class should have only one reason to change
//    objects must be problem-centered and responsible for one thing
//    but what that "one thing" is too vauge, because there are different levels of "cohesion"
// 2) (O)pen/Close Principle: (bad)
//    open for extension, but closed for modification
//    this principle is not really applicable to objects and OOP
//    it may work with modules and services, but not with objects ("implementation inheritance" is bad)
// 3) (L)iskov Substitution Principle: (so so)
//    if your method expects a Collection,
//      an ArrayList will & should work too
//    this is known as "subtyping" and should apply to "variables" and "method calling" too
// 4) (I)nterface Segregation Principle: (good)
//    you must not declare List x,
//      if you only need Collection x or even Iterable x
// 5) (D)ependency Inversion Principle: (good)
//    instead of ArrayList x,
//      you must declare List x and let the provider of the object decide whether it is ArrayList or LinkedList
//
// we should emphasize on the concepts of "cohesion" and "loose coupling" instead
// 
