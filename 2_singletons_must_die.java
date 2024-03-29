// singletons
// 1) they are anti-patterns, as they work as global things
//    ex. a database connection pool, a repository, a configuration map, etc. 
// 2) use dependency injections instead
//   
// example: a database connection pool
//
// (bad design: use singleton, declared as a class's static member)

class Database {

    public static Database INSTANCE = new Database(); // one and the only one static (global) instance

    private Database() {
        // create a connection pool
    }

    public java.sql.Connection connect() {
        // Get new connection from the pool and return
    }
}
// suppose we have to access to the pool in many different places, we can get a new connection from the sigleton object

// client code:
//   ex. a JAX-RS client: a simple MVC architecture, where text() method is a controller
@Path("/")
class Index {
    // client class

    @GET
    public String text() { // a controller method
        java.sql.Connection connection = Database.INSTANCE.connect(); // get a new connection from staic (global) singleton instance
        return new JdbcSession(connection)
            .sql("SELECT text FROM table")
            .fetch(new SingleOutcome(String.class));
    }
}
// why is it bad?
//   we need a singleton instance to be globally available so that any MVC controller have direct access to it
//   client classes are implicitly coupled together due to the (global) singleton

// (good design: use dependency injection instead: get the database connection pool from constructor)
class Index {

    private java.sql.Connection conn;

    public Index(Database db) {
        this.db = db;
    }

    public String text() { // a controller method
        java.sql.Connection connection = db.connect();                // get a new connection from the injected dependency
        return new JdbcSession(this.conn)
            .sql("SELECT text FROM table")
            .fetch(new SingleOutcome(String.class));
    }
}
// forget about singletons: turn them into dependencies and pass them from object to object
//   ex. pass an instance of Database to all objects that may need it through their constructors
// client classes are decoupled (client is coupled to interface of the injected dependency)

