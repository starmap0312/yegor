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


// DI (Dependency Injection) Container

