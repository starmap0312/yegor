// example: read a story from a file as a UTF-8 text
// (bad design)
class Story {
    String text() {
        return new TextOf(new File("/tmp/story.txt")).asString();
    }
}
// why is it bad?
//   class Story and File("/tmp/story.txt") is tightly coupled
//   therefore, you cannot test the class without the actual file at "/tmp/story.txt"

// improve the design using dependency injection
class Story {
    private final File file;
    Story(File f) {
        this.file = f;
    }
    String text() {
        return new TextOf(this.file).asString();
    }
}
// why is it better?
//   the passed-in File can be injected, so it can be replaced by a mock object
// why is it not good enough?
//   user of class Story needs to know the path of the file
new Story(new File("/tmp/story.txt"));

// (good design: use secondary constructor to hide the actual path from user)
class Story {
    private final File file;

    Story() {       // secondary constructor
        this(new File("/tmp/story.txt"));
    }

    Story(File f) { // primary constructor
        this.file = f;
    }

    String text() {
        return new TextOf(this.file).asString();
    }
}
// why is it good?
//   user of class Story does NOT need to know the path of the file
new Story();
// why is it not good enough?
//   there is still one new operator inside method text()
//   text() is tighted coupled to a TextOf object, making it harder to be reused and tested

// example: if the file is not in UTF-8 encoding but in KOI8-R
class Story {
    private final Text text;

    Story() {                             // secondary constructor
        this(new File("/tmp/story.txt")); // the new operator is inside constructor
    }

    Story(File f) {                       // secondary constructor
        this(f, StandardEncodings.UTF_8);
    }

    Story(File f, Encoding e) {           // secondary constructor
        this(new TextOf(f, e));           // the new operator is inside constructor
    }

    Story(Text t) {                       // primary constructor
        this.text = t;
    }

    String text() {
        return this.text.asString();
    }
}
// why is it good?
//   all new operators are inside constructor: none of them are left in the method text()
//   therefore, it is not coupled to other class, so you can test the method more easily

// rule of thumb:
//   the more the new operators stay in the methods, the less reusable and testable is the class
//   because the method would be tightly coupled to an collaborator, so move them out if possilbe
