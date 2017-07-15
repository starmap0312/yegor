// Public Static Literals Are Not a Solution for Data Duplication
//   similarly, utility classes with public static methods are also evil
//
// ex. Apache Commons defines a public static property: CharEncoding.UTF_8

// (bad design: use public static literals to avoid code duplicaiton)
package org.apache.commons.lang3;
public class CharEncoding {
    public static final String UTF_8 = "UTF-8";  // a public static literal (constant String)
    // some other methods and properties
}

// example 1: the client code of converting a byte array into a String
import org.apache.commons.lang3.CharEncoding;
String text = new String(array, CharEncoding.UTF_8);
// why is it bad?
// 1) once you use CharEncoding.UTF_8, your object starts to depend on this data
//    the user of your object can't break this dependency
// 2) placing data into one shared place (CharEncoding.UTF_8) doesn't really solve the duplication problem
//    it encourages everyone to duplicate functionality using the piece of constant data
// (good design: encapsulate the public constant inside a new class that provides the same service)
// 1) make a new class/object for this code and place the data there
// 2) declare it as a private (static) property (that's how you get rid of duplication)
String text = new UTF8String(array);  // create a new class extending String that provides that service
// encapsulate the "UTF-8" constant inside that class
// but as Java makes class String final, in reality we will have to write this:
//   UTF8String does not extend String, but providing a method toString() that provides the service
String text = new UTF8String(array).toString();
// why is it good?
// a) encapsulate the "UTF-8" constant inside the new class UTF8String
// b) this decouples the client from the constant string data (i.e. CharEncoding.UTF_8)
//    the client has no idea how exactly this "byte array to string" conversion is happening
//    it constructs a UTF8String instance and use its toString() method for the conversion
// c) this encapsulates the functionality inside a class 
//    let everybody instantiate objects and use them independently
//    resolves the problem of functionality duplication, not just data duplication

// example 2: the client code of converting a String into a byte array
// (bad design)
import org.apache.commons.lang3.CharEncoding;
String text = new String(array, CharEncoding.UTF_8);
byte[] array = text.getBytes(CharEncoding.UTF_8);

// (good design)
byte[] array = new UTF8String(array).toByteArray();

// rule of thumbs
//   every time seeing some data duplication in your application
//   instead of thinking about the data duplication, thinking about the functionality duplication
//   you will find the code that is repeated again and again
