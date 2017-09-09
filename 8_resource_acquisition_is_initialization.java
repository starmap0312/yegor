// Resource Acquisition Is Initialization (RAII)
// 1) resource allocation (acquisition) is done during object creation by the constructor
//    resource deallocation (release) is done by the destructor
//    (C++)
//      an instance's local variables allow easy management of multiple resources
//      local variables are destroyed in the reverse order of construction
//    (Java/Python) 
//      because Java is a garbage collected language you cannot predict when an object will be destroyed
//      1.1) use finalize() method:
//           it is the Java's destructor called by the garbage collector
//             a method of Object class, invoked when an object is picked by the garbage collector
//           use finalize() only for sanity checking, not resource management (especially for non-memory resources)
//             do not use it for non-memory resources, because you do not know when the release will take place
//             otherwise, you will have memory leak or lock of files, etc.
//           it allows an object to clean up its state at destruction
//      1.2) use try/catch/finally construct (context-management)
//           for classes that need to explicitly tidy up, the convention is to define a close() method
//             then close() method is automatically called when exiting a try/catch/finally construct
//           used to explicitly close/release a non-memory resource, or cleaning up/logging
//             ex. closing a FileStream, I/O stream objects, Database connections, HTTP connections, etc.
//           you explicitly release a non-memory resource when you are done with using that resource
//      1.3) use try([allocation of resource]) { ... use of resource ... } statement
//           resource must implement Closable, and it's close() method will be called when exiting scope { ... }
// 2) RAII means:
//      holding a resource as a class invariant (i.e. private field), which is tied to the object's lifetime
//    this ensures that:
//      if there are no object leaks, then there will be no resource leaks
// 3) RAII advantages:
//    RAII greatly simplifies resource management
//      it reduces the code size and helps ensure program correctness
//    RAII provides encapsulation (resource allocation)
//      resource management logic is defined once in the class, not at each call site
//      ex. context manager or try/catch/finally construct
//    RAII provides exception safety for stack resources
//      a) if an exception is thrown and proper exception handling is in place
//         the only code that will be executed when exiting the current scope are the destructors of objects
//      b) tying the resource to the lifetime of a stack variable
//      c) resources that are released in the same scope as they are acquired
//      d) in C++, stack unwinding is only guaranteed to occur if the exception is caught somewhere
//         if no matching handler is found in a program, the function terminate() is called
//         this behavior is usually acceptable, since OS releases remaining resources like memory, files, sockets, etc.
//         at program termination
//    RAII provides code locality (it improves readability)
//      acquisition and release logic are written next to each other in the class definition
// 4) RAII vs. try/finally construct
//    i.e. the try { ... resource allocation ... } finally {...} construct in Java
//    RAII has less code than use of a try/finally construct
// 5) RAII typical uses (examples) :
//    a) interacting with files
//       the object represents a file that is open for writing
//       the file is opened in the constructor and closed when execution leaves the object's scope
//       (care must be taken to maintain exception safety)
//    b) controlling mutex locks in multi-threaded applications
//       the code that locks the mutex essentially includes the logic that the lock will be released
//       the object releases the lock when destroyed (i.e. when execution leaves the scope of the RAII object)
// 6) limitations
//    RAII only works for resources acquired and released (directly or indirectly) by stack-allocated objects
//    for Heap-allocated objects, RAII needs smart pointers and weak-pointers for cyclically referenced objects
//    i.e. ownership of dynamically allocated objects (memory allocated with new in C++) controlled with RAII
//    ex.  C++11 standard library (smart pointer classes) std::unique_ptr for single-owned objects
//                                                        std::shared_ptr for objects with shared ownership
//         C++98 standard library                         std::auto_ptr
//         Boost libraries                                boost::shared_ptr

// Scope-based Resource Management (SBRM)
// 1) special case of automatic variables
// 2) RAII ties resources to object lifetime
//    this may not coincide with entry and exit of a scope
//    but using RAII for automatic variables (SBRM) is the most common use case

// RAII vs. garbage collected languages (Python/Java)
// 1) deterministic object destruction tied to scope is impossible in a garbage collected language
//    you cannot predict when (or even if) an object will be destroyed
//    ex. Python manages object lifetime by reference counting, which makes it not possible to use RAII
//        there is no guarantee when __del__() will be called
//        it’s just for memory manager use not for resource handling
// 2) in garbage collected languages, objects that are no longer referenced are destroyed and released
//      so a destructor can release the resource at that time
//    however, object lifetimes are not necessarily bound to any scope
//      objects may be destroyed non-deterministically or not at all
// 3) it's possible to accidentally leak resources that should have been released at the end of some scope
//    ex. objects stored in a static variable (a global variable) may not be finalized when program terminates
// 4) objects with circular references will not be collected by a simple reference counter
//    they will live indeterminately long
//    even if collected (by garbage collection), destruction time and order will be non-deterministic
// 5) context manager (i.e. with statement) has nothing to do with scoping
//    it is deterministic cleanup, so it overlaps with RAII in the ends, but not in the means
//    ex. in Python,
//        with open('filename.txt', 'rb') as fin:
//            content = fin.read()
//        # the resource (file) is acquired/released deterministically via the context manager
//    ex. in Java,
//        class InputStream extends AutoClosable has a close() method
//        it defines close() method (release resource), which will be called exiting the try construct
try(InputStream is = new FileInputStream("/etc/passwd")) { // FileInputStream implements AutoCloseable
    int data = is.read();
    while(data != -1){
        System.out.print((char) data);
        data = is.read();
    }
}

