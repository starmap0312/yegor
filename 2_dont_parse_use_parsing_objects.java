// Don't Parse, Use Parsing Objects
// (bad design)
// the traditional way of integrating object-oriented back-end with an external system is through data transfer objects
// 1) serialization step:
//    objects are serialized into JSON string before going out 
// 2) deserialization step (parsing step)
//    JSON string are parsed to a data transfer object when coming back

// (good design)
// 1) serialization step:
//    use printers instead (there can be json, xml, etc. printers)
// ex. the client wants to print the book object into json format
//     so it instantiate a JsonMedia object (printer) and ask the book object to print for him 
JsonMedia media = new JsonMedia();          // the printer object uses JsonObjectBuilder to build the JSON string 
JsonObject json = book.print(media).json(); // book is an active object (not DTO) that provides print([media]) for you
// 2) deserialization step (parsing step): this should be done by objects
// ex. the arriving JSON string is as follows
{
  "title": "Object Thinking",
  "isbn: "0735619654",
  "author: "David West"
}

interface Book {
    String isbn();
}

class Library {
    // class Library expects an object of type Book to be given to its method register()
    public void register(Book book) {
        // Create a new record in the database
    }
}

// the HTTP entry point 
// (bad design: use of DTO)
public class TkUpload implements Take {
    private final Library library;

    @Override
    public Response act(Request req) {
        String body = new RqPrint(new RqMtSmart(new RqMtBase(req)).single("book")).printBody(); // get the http response body
        JsonObject json = Json.createReader(new InputStreamOf(body)).readObject();              // create a json reader
        Book book = new BookDTO();                                                              // create and set the DTO
        book.setIsbn(json.getString("isbn")); // the DTO object is a passive object
        library.register(book);
    }
}
// why is it bad?
// the code is rather procedural and has a lot of temporal coupling
// the class is not reusable
//   if we need something similar in a different place, we have to write this HTTP processing and JSON parsing again
// error handling and validation are not reusable either
//   if we add it to the method above, we will have to copy it everywhere

// (good design: a parsing/active object)
// hide the json parsing inside an active object (ex. a new class JsonBook)
class JsonBook implements Book {
    private final String json;

    JsonBook(String body) { // the object contains a json string
        this.json = body;
    }

    @Override
    public String isbn() {  // the object parses and reads the json object inside
        return Json.createReader(new InputStreamOf(body)).readObject().getString("isbn");
    }
}
// the http (RESTful) entry point
public class TkUpload implements Take {
    private final Library library;

    @Override
    public Response act(Request req) {
        library.register(
            new JsonBook(new RqPrint(new RqMtSmart(new RqMtBase(req)).single("book")).printBody())
        );
        // or verbosely (but this creates temporal coupling)
        String body = new RqPrint(new RqMtSmart(new RqMtBase(req)).single("book")).printBody(); // get the http response body
        library.register(new JsonBook(body));
    }
}
