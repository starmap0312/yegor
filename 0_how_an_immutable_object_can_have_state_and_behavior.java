// How an Immutable Object Can Have State and Behavior
//   hwo an immutable object represents a frequently changing real-world entity
// rule of thumb:
//   if a new document frequently changes its title, then title should not be a state of the document but its behavior
// mutable object vs. immutalbe object
//   a mutalbe object has an identity and its state may change
//   an immutable object does not have an identity and its state never changes
//
// example: representing a non-frequently changing-title document
//
// (bad design: a mutable object with title as its state)
class Document {

    private int id;                       // a state of the object
    private String title;                 // a mutable state of the object

    Document(int id, String text) {
        this.id = id;
        this.title = text;
    }

    public String getTitle() {
        return this.title;
    }

    public String setTitle(String text) { // the object's internal state may change
        this.title = text;
    }

    @Override
    public String toString() {
        return String.format("doc #%d about '%s'", this.id, this.title);
    }
}

// client code
Document first = new Document(50);
first.setTitle("How to grill a sandwich");

Document second = new Document(50);
second.setTitle("How to grill a sandwich");

if (first.equals(second)) { // return FALSE as the two objects have different identities (although encapsulate same states)
    System.out.println(String.format("%s is equal to %s", first, second));
}
// we can modify its internal state (a mutable object)
first.setTitle("How to cook pasta");

// (good design: an immutable object)
@Immutable
class Document {

    private final int id;
    private final String title;

    Document(int id, String text) {
        this.id = id;
        this.title = text;
    }

    public String getTitle() {
        return this.title;
    }

    public Document setTitle(String text) {
        return new Document(this.id, text);  // create a new immutable object
    }

    @Override
    public String toString() {
        return String.format("doc #%d about '%s'", this.id, this.title);
    }

    @Override
    public boolean equals(Object doc) { // compare objects based on their encapsulated states, not object identities
        return doc instanceof Document
            && Document.class.cast(doc).id == this.id
            && Document.class.cast(doc).title.equals(this.title);
    }
}

// client code
Document first = new Document(50, "How to grill a sandwich");
Document second = new Document(50, "How to grill a sandwich");
if (first.equals(second)) { // return TRUE as the two objects encapsulate the same states 
    System.out.println(String.format("%s is equal to %s", first, second));
}
// we cannot modify the object's internal state but create a new immutalbe object instead
third = first.setTitle("How to cook pasta");

// example: representing a frequently changing-title document
//   if document's title frequently changes, then title should not be its state but its behavior instead
//
// (good design: an immutable object with title as its behavior)
class Document {
    private final int id;                // object's state
    private final Storage storage;       // object's collaborator

    Document(int id) {
        this.id = id;
        this.storage = new Storage();    // the collaborator (encapsulated knowledge, hidden from the client)
    }

    public String getTitle() {           // read title from the collaborator (i.e. storage)
        return new String(this.storage.read());
    }

    public void setTitle(String title) { // use the collaborator to save title
        this.storage.write(text.getBytes());
    }

    @Override
    public String toString() {
        return String.format("doc #%d about '%s'", this.id, this.title());
    }

    @Override
    public boolean equals(Object doc) {
        return doc instanceof Document
            && Document.class.cast(doc).id == this.id;
    }
}

// interface: both getTitle() and setTitle() are the object's behaviors
@Immutable
interface Document {
    String getTitle();
    void setTitle(String text);
}
// why is it good?
// 1) the document the object represents stays the same, i.e. it's state (id) is not changed
// 2) it remains the same document, even though its title frequently changes (not a state of the document
// 3) it's title is something in the real world, outside of the document
//    i.e. the document is just a proxy used to read and write the title

// what is the Storage object:
// 1) Document object is collaborating with the Storage object to access the title
//    i.e. not using getter/setter methods to change its internal state
// 2) Document object needs extra knowledge to access to the data
//    ex. a database unique key, an HTTP address, a file name, or a storage address
// 3) the Storage object should be a data animator, not just holding data (because object should be alive)

// ex. the Storage object represents computer memory
@Immutable
class Document {
    private final int id;
    private final Memory memory;

    Document(int id) {
        this.id = id;
        this.memory = new Memory();
    }

    public String getTitle() {
        return new String(this.memory.read());
    }

    public void setTitle(String text) {
        this.memory.write(text.getBytes());
    }
}

