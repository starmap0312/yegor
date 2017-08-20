// RAII (Resource Acquisition Is Initialization)
//   it is used for exception-safe resource management
//
// example: problem of resource leakage if no RAII
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

// (bad design)
// explicit release the resource (semaphore) when exception occurs 
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
//   code duplication: release the resource at multiple places of the code

// (good design)
// use of RAII: acquire & release the resource at one place (resource class's constructor & destructor)
class Resource {

    private Semaphore semaphore;

    Resource(Semaphore sem) { // acquire the resource during construction
        this.semaphore = sem;
        this.semaphore.acquire();
    }

    @Override
    public void finalize() { // release the resource in finalize() method, which will be called during desctruction
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
// use of try-with-resource statement

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
        try (Resource resource = new Resource(this.semaphore)) {
            if (x > 1000) {
                throw new Exception("Too large!");
            }
            System.out.printf("x = %d", x);
        }
    }
}
// why is it good?
// 1) resource acquirsition and release is defined at one place: resource class's constructor & destructor
// 2) use try-with-resource statement to make the resource's close() method explicitly called when exiting the scope
