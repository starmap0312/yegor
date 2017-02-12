// immutablity:
//   not allowint any modifications to the encapsulated state
// different levels of immutablity: objects are immutable but their behaviors differ
// 1) constant: a function always returns the same value
class Book {
    private final String title; // final: cannot be modified once set

    String getTitle() {         // a pure function: always return the same value when called with the same parameters
        return this.title;
    }

    Book setTitle(String text) {
        return new Book(text);
    }
}

// 2) not a constant: a function may returns different values when called with the same parameters 
class  Book {
    private final String title;

    String getTitle() {          // not a pure function, depending on external resource, i.e. system date
        return String.format("%s (as of %tR)", this.title, new Date());
    }

    Book setTitle(String text) {
        return new Book(text);
    }
}

// 3) represented mutability: not constnat, i.e. a function may return different values
//                            the represented entity (ex. file) is a mutable real-world entity 
class Book {
    private final Path path;      // final: the coordinate of the object never changes

    String getTitle() {           // not a pure function, depending on the represented mutable object
        return new String(Files.readAllBytes(this.path));
    }

    Book setTitle(String text) {
        Files.write(this.path, text.getBytes(), StandardOpenOption.CREATE);
        return this;
    }
}

