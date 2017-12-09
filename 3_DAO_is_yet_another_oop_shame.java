// Data Access Object (DAO)
//   an object that provides an abstract interface to database (or other persistence mechanism)
//
// (bad design: DAO)
class BookDAO {
    Book find(int id);
    void update(Book book);
    // Other methods here ...
}

BookDAO dao = BookDAOFactory.getBookDAO(); // an abstract interface to database
Book book = dao.find(123);    // the Book DAO's find() method creates a Book DTO (Data Transfer Object)
book.setTitle("Don Quixote"); // one can inject new data into the Book DTO
dao.update(book);             // the Book DTO is passed to Book DAO's update() method to update the database

// why is it bad?
// 1) Book DTO is NOT an active object, but a passive data container
// 2) DAO does NOT encapsulate database interaction inside an object
//    instead, it extracts database interaction away, literally tearing a solid and cohesive living organism apart
// 3) Data Access Object is WRONG
//    the object is used to access data from database
//    it goes to the database, retrieves some data, and returns the "data (DTO)"
//    intead, it should return an active object, not DTO
// 4) direct data manipulations break encapsulation
//    direct data manipulations make object-oriented code procedural and unmaintainable

// (good design: active objects)
interface Pipes {           // a collection interface
    void add(String json);  // add() creates a new item in the collection
    Pipe pipe(long number); // pipe() returns a single object from the collection
}

Pipe.status("Complete")     // status() updates the object's status to database

// why is it good?
// 1) Pipe is not a DTO, it is an active object (fully capable of doing all necessary database operations)
//    it does not need any help from a DAO
// 2) use active objects, instead of using DTO and DAO to manipulate data

// (good design: Jare.io)
// interface Base
//   it returns a list of objects of type Domain
// domain.delete()
//   this deletes a domain, i.e. the domain is fully capable of doing all necessary database manipulations
