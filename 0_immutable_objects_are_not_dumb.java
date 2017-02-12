// Immutable Objects Are Not Dumb
// 1) an object is immutable when it doesn't change the coordinates of the real-world entity it represents
//    i.e. its state can't be modified after it is created
// 2) immutable object may serve as a representative of a mutable real-world entity
//    ex. an immutable class, File, represents a mutable real-world entity, a file on disk
// 3) immutable objects do not need to always behave the same way (unlike functional programming)
//    i.e. a method may return different results each time we call it
//
// example:

class Page {
    private final URI uri; // declared as final

    Page(URI addr) {
        this.uri = addr;   // this the state uniquely identifies every object of this class and is not modifiable
    }

    public String load() {
        return new JdkRequest(this.uri).fetch().body();
    }

    public void save(String content) {
        new JdkRequest(this.uri).method("PUT").body().set(content).back().fetch();
    }
}
// the class makes only immutable objects
//   each object represents a mutable entity of the real world, a web page with a URI
//   the object doesn't guarantee anything about the immutable behavior of that web page
//   the only thing it promises is that it will always stay loyal to that page (never changes its coordinates)
//
// note:
//   an immutable object is NOT a data structure that never changes
//   the state of the object is the coordinates of the entity being represented
//   an object is immutable when it doesn't change the coordinates of the real-world entity it represents
