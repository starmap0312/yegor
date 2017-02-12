// different levels of immutablity:
//   objects are immutable but their behaviors may differ
//   immutable objects are always loyal to the entities they represent (it never changes its coordinates)
// 1) Constant
//    the object is immutable: it doesn't allow any modifications to the encapsulated state
//    its function is pure function: always returns the same value
// 2) Not a Constant
//    the object is immutable: it doesn't allow any modifications to the encapsulated state
//    its function is NOT pure function: may return different value if called multiple times
// 3) Represented Mutability
//    the object is immutable: it never changes its coordinates (i.e. the entity it represents)
//    its function is NOT pure function: may return different value if called multiple times
//    the object represents a mutable real-world entity: it may even accept requests to change the entity's state
// 4) Encapsulated Mutability 
//    the object is immutable: it never changes its coordinates (i.e. the entity it represents)
//    its function is NOT pure function: may return different value if called multiple times
//    the object represents a mutable real-world entity: it may even accept requests to change the entity's state
//    the real-world entity is in memory 

// examples:
//
// 1) constant: its function always returns the same value
class Book {
    private final String title; // final: cannot be modified once set

    String getTitle() {         // a pure function: always return the same value when called with the same parameters
        return this.title;
    }

    Book setTitle(String text) {
        return new Book(text);
    }
}

// 2) not a constant: its function may returns different values when called with the same parameters 
class  Book {
    private final String title;

    String getTitle() {          // not a pure function, depending on external resource, i.e. system date
        return String.format("%s (as of %tR)", this.title, new Date());
    }

    Book setTitle(String text) {
        return new Book(text);
    }
}

// 3) represented mutability: its function may return different values
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


// 4) encapsulated mutability: the represented entity is in memory
//
class Book {
    private final StringBuffer buffer;

    String getTitle() {
        return this.buffer.toString();
    }

    Book setTitle(String text) {
        this.buffer.setLength(0);
        this.buffer.append(text);
        return this;
    }
}
