// Dependency Injection Containers are Code Polluters
// ex. Google Guice
//
// example:
//
// (good design: dependency injection via constructor)
public class Budget {

    private final DB db;

    public Budget(DB database) { // dependency injection via constructor
        this.db = database;
    }

    public long total() {
        return this.db.cell("SELECT SUM(cost) FROM ledger");
    }
}

// the client code
public class App {

    public static void main(String[] args) {

        Budget budget = new Budget(new Postgres("jdbc:postgresql:5740/main"));
        System.out.println("Total is: " + budget.total());
    }
}

// (bad design: let Budget decide what database it wants to work with)
public class Budget {

    private final DB db = new Postgres("jdbc:postgresql:5740/main"); // a hidden/private knowledge/collaborator

    public long total() {
        return this.db.cell("SELECT SUM(cost) FROM ledger");
    }
}
// why is it bad?
// 1) inability to reuse, code duplication
// 2) inability to test
// but almost any object will want to encapsulate some knowledge (i.e. state)


// DI (Dependency Injection) Container: ex. Google Guice
//
// example
import javax.inject.Inject;

public class Budget {

    private final DB db;

    @Inject                  // the constructor is annotated with @Inject
    public Budget(DB data) {
      this.db = data;
    }

    public long total() {
        return this.db.cell("SELECT SUM(cost) FROM ledger");
    }
}

// not instantiate Budget through the new operator, but use the injector instead
public class App {

    public static void main(String[] args) {
        // configure a container somewhere when the application starts
        Injection injector = Guice.createInjector(
            new AbstractModule() {
                @Override
                public void configure() {
                    this.bind(DB.class).toInstance(new Postgres("jdbc:postgresql:5740/main"));
                }
            }
        );
        // the injection automatically finds out that in order to instantiate a Budget it has to provide an argument for its constructor
        // it will use an instance of class Postgres, which we instantiated in the injector
        Budget budget = injector.getInstance(Budget.class);
        System.out.println("Total is: " + budget.total());
    }
}
// why is it bad?
//   additional complexity: adds more lines to the code base, or even more files (ex. XML)
// use object composition instead
