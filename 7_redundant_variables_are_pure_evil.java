// Redundant Variables Are Pure Evil
// 1) they exist exclusively to explain its value
// 2) but they make the code more verbose and difficult to understand
//    an increasing length of code degrades readability, so directly use values instead
// 3) you won't need any variables aside from method arguments (in perfectly designed methods)
//   
// example:
//
// (bad design: use a redundant variable)
String fileName = "test.txt"; // the name of the variable explains what the value means
print("Length is " + new File(fileName).length());

// (good design: )
print("Length is " + new File("test.txt").length());

// rule of thumb:
// 1) declare at most four variables in a method
// 2) refactor the code using new objects or methods but not variables
//    make the code shorter by moving pieces of it into new classes or private methods
