// Resource Acquisition Is Initialization (RAII)
// 1) resource allocation (acquisition) is done during object creation by the constructor
//    resource deallocation (release) is done by the destructor
//    ex. in C++, local variables allow easy management of multiple resources within a single function
//        local variables are destroyed in the reverse order of their construction
//        (an object is destroyed only if fully constructed, i.e. if no exception propagates from its constructor)
// 2) in RAII, holding a resource is a class invariant (private field) and is tied to object lifetime
//    so if there are no object leaks, then there will be no resource leaks
// 3) RAII greatly simplifies resource management, reduces overall code size and helps ensure program correctness
//    RAII provides encapsulation and avoid code duplication (resource allocation)
//      resource management logic is defined once in the class, not at each call site
//    RAII provides exception safety for stack resources
//      a) tying the resource to the lifetime of a stack variable
//      b) resources that are released in the same scope as they are acquired
//      c) if an exception is thrown and proper exception handling is in place
//         the only code that will be executed when exiting the current scope are the destructors of objects
//         declared in that scope
//      d) in C++, stack unwinding is only guaranteed to occur if the exception is caught somewhere
//         if no matching handler is found in a program, the function terminate() is called
//         this behavior is usually acceptable, since OS releases remaining resources like memory, files, sockets, etc.
//         at program termination
//    RAII provides code locality (improved readability)
//      acquisition and release logic are written next to each other in the class definition
// 4) RAII vs. Java Try { ... resource allocation ... } finally {...} construct
//    comparing RAII with the finally construct in Java
//    RAII technique has less code than use of a try/finally construct
// 5) typical uses (examples) 
//    a) interacting with files
//       the object represents a file that is open for writing
//       the file is opened in the constructor and closed when execution leaves the object's scope
//       (care must be taken to maintain exception safety)
//    b) controlling mutex locks in multi-threaded applications
//       the code that locks the mutex essentially includes the logic that the lock will be released
//       the object releases the lock when destroyed (i.e. when execution leaves the scope of the RAII object)
//    c) ownership of dynamically allocated objects (memory allocated with new in C++) can be controlled with RAII
//    ex.  C++11 standard library: smart pointer classes std::unique_ptr for single-owned objects
//                                                       std::shared_ptr for objects with shared ownership
//         C++98 standard library: std::auto_ptr
//         Boost libraries: boost::shared_ptr
// 6) limitations
//    RAII only works for resources acquired and released (directly or indirectly) by stack-allocated objects
//    but for Heap-allocated objects, RAII needs smart pointers and weak-pointers for cyclically referenced objects

// Scope-based Resource Management (SBRM)
// 1) special case of automatic variables
// 2) RAII ties resources to object lifetime, which may not coincide with entry and exit of a scope
//    however, using RAII for automatic variables (SBRM) is the most common use case

// RAII vs. garbage collected languages (Python/Java)
// 1) deterministic object destruction tied to scope is impossible in a garbage collected language
//    you cannot predict when (or even if) an object will be destroyed
//    ex. for Java deconstrucotr, a class that extends AutoClosable has a close() method
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
//    they will live indeterminately long; even if collected (by garbage collection), destruction time and order
//    will be non-deterministic
// 5) context manager (i.e. with statement) has nothing to do with scoping
//    it is deterministic cleanup: so it overlaps with RAII in the ends, but not in the means
//    ex. in Java, a class that extends AutoClosable has a close() method
