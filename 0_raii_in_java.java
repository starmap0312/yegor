// RAII (Resource Acquisition Is Initialization)
//   it is used for exception-safe resource management
//
// example: no resource management for exceptions
// i.e. there is a problem of resource leakage if no RAII
class Foo {

    private Semaphore semaphore = new Semaphore(5);

    void print(int x) throws Exception {
        this.semaphore.acquire();              // at most 5 threads can acquire the resource (semaphore) and print
        if (x > 1000) {                        // but if there is an exception,
            throw new Exception("Too large!"); // the resource (semaphore) is not released when exception occurs
        }
        System.out.printf("x = %d", x);
        this.semaphore.release();              // the resource (semaphore) is only release when normally exits 
    }
}

// (bad design: code duplication of resource release)
// i.e. explicitly release the resource (semaphore) when exception occurs 
class Foo {

    private Semaphore semaphore = new Semaphore(5);

    void print(int x) throws Exception {
        this.semaphore.acquire();
        if (x > 1000) {
            this.semaphore.release();
            throw new Exception("Too large!");
        }
        System.out.printf("x = %d", x);
        this.semaphore.release();
    }
}
// why is it bad?
// 1) code duplication: release the resource at multiple places of the code
// 2) hard to manage the code if there are multiple places for releasing the resource

// (good design)
// use of RAII: acquire & release the resource at one place
// i.e. acquire and release the resource at the class's constructor & destructor
class Resource {

    private Semaphore semaphore;

    Resource(Semaphore sem) { // acquire the resource during construction
        this.semaphore = sem;
        this.semaphore.acquire();
    }

    @Override
    public void finalize() { // release the resource during desctruction
        this.semaphore.release();
    }
}

class Foo {

    private Semaphore semaphore = new Semaphore(5);

    void print(int x) throws Exception {
        new Resource(this.semaphore); // resource is automatically acquired at construction and released at destruction
        if (x > 1000) {
            throw new Exception("Too large!");
        }
        System.out.printf("x = %d", x);
    }
}
// what is the problem:
//   Java is a garbage collection language, so we do not know when the resource object will be desctructed

// (solution)
// use try-with-resource statement and define the reource release at the close() method
class Resource implements Closeable {

    private Semaphore semaphore;

    Resource(Semaphore sem) {
        this.semaphore = sem;
        this.semaphore.acquire(); // acquire the resource (semaphore)
    }

    @Override
    public void close() {
        this.semaphore.release(); // release the resource (semaphore)
    }
}

class Foo {

    private Semaphore semaphore = new Semaphore(5);

    void print(int x) throws Exception {
        try (Resource resource = new Resource(this.semaphore)) { // use of try-with-resource statement
            if (x > 1000) {
                throw new Exception("Too large!");
            }
            System.out.printf("x = %d", x);
        }
    }
}
// why is it good?
// 1) without the try-with-resource statement, we are not sure when an Object's finalize() method will be called 
//    define the release of a resource in the close() method (must implement the Closable/AutoClosable interfaces)
// 2) resource acquirsition and release is still defined at one place/class
//    resource class's constructor & close() method
// 3) use try-with-resource statement to make the resource's close() method explicitly called when exiting the scope