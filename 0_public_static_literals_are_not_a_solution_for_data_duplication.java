// Public Static Literals Are Not a Solution for Data Duplication
//   similar to utility classes, they create unbreakable hard-coded dependencies
//
// example: Apache Commons defines a public static property: CharEncoding.UTF_8

// (bad design: use public static literals to avoid code duplicaiton)
package org.apache.commons.lang3;
public class CharEncoding {
    public static final String UTF_8 = "UTF-8";  // a public static final literal (named constant) used by many clients
    // some other methods and properties
}

// client
import org.apache.commons.lang3.CharEncoding;
String text = new String(byteArray, "UTF-8");
byte[] byteArray = text.getBytes("UTF_8");
// becomes
String text = new String(byteArray, CharEncoding.UTF_8); // convert a byte array into a String
byte[] byteArray = text.getBytes(CharEncoding.UTF_8);    // convert a String into a byte array
// every client needs to write the same conversion code (functionality duplication)

// why is it bad?
// 1) once you use CharEncoding.UTF_8, your object starts to depend on this data
//    this couples all the clients that use the data 
// 2) placing data into one shared place (CharEncoding.UTF_8) doesn't really solve the duplication problem
//    it encourages everyone to duplicate functionality using the piece of constant data

// (good design: encapsulate the public constant inside a new class that provides the same service)
// 1) make a new class that extends String and place the constant data there
// 2) declare the data as a private static property (that's how you get rid of data duplication)
String text = new UTF8String(byteArray); // "UTF-8" constant is encapsulated in class UTF8String extends String
// but as Java makes class String final, in reality we will have to write this:
//   in reality UTF8String does not extend String, but provides a method toString() that provides the service
String text = new UTF8String(byteArray).toString();
byte[] byteArray = ((UTF8String) text).getBytes();
// every client need NOT to know how the conversion is done
public final class UTF8String {
    private static final String UTF_8 = "UTF-8";

    public UTF8String(final byte[] byteArray) {
        this.bytes = byteArray;
    }

    public String toString() {
        return new String(this.byteArray, UTF_8);
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}

// why is it good?
// 1) this decouples the client from the constant string data (i.e. CharEncoding.UTF_8)
//    the client has no idea how exactly this "byte array to string" conversion is happening
//    it constructs a UTF8String instance and use its toString() method for the conversion
// 2) the functionality is encapsulated inside a class 
//    so everybody instantiate its object and use them independently
//    this resolves the problem of functionality duplication, not just data duplication

// rule of thumbs
//   every time seeing some data duplication in your application
//   instead of thinking about the data duplication, thinking about the functionality duplication
//   you will find the code that is repeated again and again
