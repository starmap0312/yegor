// NULL references
//   NULL refernece is a clear indicator of code smell
//   NULL references is an inheritance of procedural programming, ex. C
//   use Null Objects / Exceptions instead
// in java, null reference can be assigned to any object type
//   ex. String str = null;
//   ex. Integer i  = null;
//   ex. Double num = null;
// in java, primitive types cannot be assigned as null, ex. int, float, double 
//   ex. int n = null;      ==> compile error
//   ex. double num = null; ==> compile error
//
// example: InputStream, resource that needs to be open and close explicitly
//
// (bad design: use of NULL reference)
InputStream input = null;
try {
    input = url.openStream();    // open the resource, may throw IOException if unable to open
    //... ...                    // read the stream, may throw IOException
} catch (IOException ex) {
    throw new RuntimeException(ex);
} finally {
    if (input != null) {         // finally close the resource if not null
        input.close();
    }
}
// why is it bad?
//   requires Ad-hoc Error Handling of the Null reference

// (good design: no NULL reference, throw exceptions)
final InputStream input;
try {
    input = url.openStream();    // open the resource, may throw IOException
} catch (IOException ex) {
    throw new RuntimeException(ex);
}

try {
    //... ...                    // read the stream, may throw IOException
} catch (IOException ex) {
    throw new RuntimeException(ex);
} finally {                      // finally close the resource
    input.close();
}
// why is it good?
//   fail fast: hiding failure from the client (easy to identify problems and bugs)

// example:
// 1) use of NULL references: need Ad-hoc Error Handling in client code
//    whenever you get an object, you need to check whether it is NULL or a valid object reference
//    if you forget to check, a NullPointerException (NPE) may break execution in runtime
//    thus, your logic becomes polluted with multiple checks and if/then/else blocks
//
// ex. the client code in procedural programming 
Employee employee = dept.getByName("Jeffrey");                 // may return null
Employee employee = dept.getByNameOrNullIfNotFound("Jeffrey"); // better naming

if (employee == null) {                                        // an ad-hoc error handling block (fail slowly)
    System.out.println("can't find an employee");
    System.exit(-1);
} else {
    employee.transferTo(dept2);
}

public Employee getByNameOrNullIfNotFound(String name) {
    int id = database.find(name);
    if (id == 0) {
        return null;                                           // return null if not found
    }
    return new Employee(id);
}

// 2) in OOP, there are two alternatives to NULL references:
//    a) use NULL object with common behaviors or throw exceptions
//    b) throw Exceptions directly: fail fast
//
// a) implementation of NULL objects
public Employee getByName(String name) {
    int id = database.find(name);
    if (id == 0) {
        return Employee.NOBODY;                                // return NULL object
    }
    return Employee(id);
}

// b) implementation of throwing Exceptions
public Employee getByName(String name) {
    int id = database.find(name);
    if (id == 0) {
        throw new EmployeeNotFoundException(name);             // throw an exception
    }
    return Employee(id);
}
// if getByName fails, it should raise an exception: i.e. fail fast, hiding failure from the client

// the client code is simplified (not ad-hoc handling)
dept.getByName("Jeffrey").transferTo(dept2);


// 3) NULL reference has performance advantanges? No.
// a) NULL reference
// ex. a real example of using Map in Java: only one search in Map is required
//     use null reference to require only one search
public Employee getByName(String name) {
    Employee employee = employees.get("Jeffrey"); // use of get() method of Map interface in Java: one search
    if (employee == NULL) {                       // use of null reference to avoid second search
        throw new EmployeeNotFoundException();
    }
    return employee;
}

// b) throwing Exceptions: two searches are needed? No.
// ex. implementation of throwing Exceptions in get() method may require two searches
public Employee getByName(String name) {
    if (!employees.containsKey("Jeffrey")) {    // first search
        throw new EmployeeNotFoundException();
    }
    return employees.get("Jeffrey");            // second search
}

// improvement: uses an Iterator to avoid null reference as well as second search 
public Employee getByName(String name) {
    Iterator found = Map.search("Jeffrey");     // returns an iterator
    if (!found.hasNext()) {                     // search happens only when the queue is empty
        throw new EmployeeNotFoundException();
    }
    return found.next();                        // search happens only when the queue is empty
}

// example of null object
// a) fail fast: make your code as fragile as possible, letting it break immediately and when necessary
//    i.e. hiding this failure from its client (easier to debug, don't make it fail slowly)
//    don't use NULL references, instead, use NULL objects or raise exceptions
// b) make your methods extremely demanding as to the data they manipuate
//    complain by throwing exceptions, if the provided data is not sufficient or wrong
//    you can also use a NULL object instead
//   
// implementation of NULL object in getByName()
public Employee getByName(String name) {
    int id = database.find(name);
    Employee employee;
    if (id == 0) {
        employee = new Employee() {                   // a anonymous class of NULL object
            public String name() {                    // define common behavior on some methods
                return "anonymous";
            }
            public void transferTo(Department dept) { // throws an exception on other methods
                                                      // i.e. Null object should throw exceptions on all other calls
                throw new AnonymousEmployeeException("I can't be transferred, I'm anonymous");
            }
        };
    } else {
        employee = Employee(id);
    }
    return employee;
}
// note: Null Object exposes common behavior and throws exceptions on all other method calls

// the client code is rather simple
employee = dept.getByName("Unknown")
System.out.println(employee.name()); // common behavior
employee.transferTo(dept2);          // throw an exception

