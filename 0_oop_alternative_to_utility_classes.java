// OOP Alternative to Utility Classes
// 1) utility classes are not proper objects
//    ex. StringUtils, IOUtils, FileUtils (Apache Commons), Iterables, Iterators (Guava), Files (JDK7)
// 2) they inherited from procedural programming (a functional decomposition paradigm)
//
// example: a utility method max() for selecting the maximum value of two
//
// (bad design: procedural programming)
public class NumberUtils {
    // a class without any state and prvoiding common utility code
    public static int max(int a, int b) {
        return a > b ? a : b;
    }
}
// why is it bad?
//   hard to test: hard-coded dependency, cannot be replaced with test-double

// the client code
int max = NumberUtils.max(10, 5);

// (good design: object-oriented programming)
// 1) instantiate and compose objects: let them manage data when and how they desire
// 2) instead of calling supplementary static functions (utility methods), we should create objects that are
//    capable of exposing the behavior we are seeking
//    ex. create a Max object responsible for selecting maximum value of its two values
public class Max implements Number {

    private final int a;
    private final int b;

    public Max(int x, int y) {
        this.a = x;
        this.b = y;
    }

    @Override
    public int intValue() { // the intValue() method provides the maximum value of its two numbers
        return this.a > this.b ? this.a : this.b;
    }
}

// the client code
int max = new Max(10, 5).intValue();

// example: a utility method transform() for reading a text file, trimming every line, and saving in another file
//
// (bad design: procedural programming)
void transform(File in, File out) { // define a procedure of execution
    Collection<String> src = FileUtils.readLines(in, "UTF-8"); // read lines from a file
    Collection<String> dest = new ArrayList<>(src.size());
    for (String line : src) {                                  // trim every line, saving in a collection
        dest.add(line.trim());
    }
    FileUtils.writeLines(out, dest, "UTF-8");                  // save the collection in another file 
}
// why is it bad?
// 1) the method is responsbile for too many things
// 2) it is hard to test the method (we need to prepare a real file if want to test the method)
//    ex. we cannot test the trim() functionality without providing a real file

// (good design: object-oriented programming)
void transform(File in, File out) {
    Collection<String> src = new Trimmed(new FileLines(new UnicodeFile(in)));
    Collection<String> dest = new FileLines(new UnicodeFile(out));
    dest.addAll(src);
}
// 1) class UnicodeFile implements FILE:
//    it reads and writes files as unicode encoding
// 2) class FileLines implements Collection<String>:
//    it behaves exactly as a collection of strings and hides all I/O operations
//    it encapsulates all file reading and writing operations
//      when we iterate() it, a file is being read
//      when we addAll() to it, a file is being written
// 3) class Trimmed implements Collection<String>:
//    every time the next line is retrieved, it gets trimmed
//    it works as a decorator class
// 4) advantages:
//    every class has only one responsibility, following the single responsibility principle
//    it is easier to develop, maintain and unit-test classes
//      ex. we can test the functionality of class Trimmed using a test double
//    it is declarative: i.e. lazy execution, the file is not read until its data is required
//      the whole task starts only after we call addAll()

// another example: a utility method readWords() for reading words from a file
//
// (bad design)
class FileUtils {

    public static Iterable<String> readWords(File f) {
        String text = new String(Files.readAllBytes(Paths.get(f)), "UTF-8");
        Set<String> words = new HashSet<>();
        for (String word : text.split(" ")) {
            words.add(word);
        }
        return words;
    }
}
// why is it bad?
// 1) the utility method readWords() is responsible for too many things
// 2) to test it we need to prepare a file for it to read and debug the code if the result is not what we expect
//
// (good design: distribute the responsibilities to different objects)
// Step 1: turn the utility method into a class
class Words implements Iterable<String> {
    // this class is responsible for both reading a file into a string and iterating over words of that string

    private final File file;

    Words(File src) {
        this.file = src;
    }

    @Override
    public Iterator<String> iterator() { // iterator() provides an iterator for iterating the file's words
        String text = new String(Files.readAllBytes(Paths.get(this.file)), "UTF-8");
        Set<String> words = new HashSet<>();
        for (String word : text.split(" ")) {
            words.add(word);
        }
        return words.iterator();
    }
}
// why is it bad?
//   the class is still responsible for too many things

// Step 2: refactor the class and distribute its responsibilities to other objects
class Text {
    // this class is responsibe for reading the file into a string

    private final File file;

    Text(File src) {
        this.file = src;
    }

    @Override
    public String toString() {
        return new String(Files.readAllBytes(Paths.get(this.file)), "UTF-8");
    }
}

class Words implements Iterable<String> {
    // this class is responsibe for iterating the words of string

    private final String text;

    Words(String txt) {
        this.text = txt;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> words = new HashSet<>();
        for (String word : this.text.split(" ")) {
            words.add(word);
        }
        return words.iterator();
    }
}
// why is this good?
//   the code is more testable and reusable: we don not need to prepare a file for it to read

// ex. the test code
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class WordsTest {

    @Test
    public void parsesSimpleText() {
        assertThat(new Words("How are you?"), hasItems("How", "are", "you"));
    }
}

