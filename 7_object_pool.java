// Object pool
// 1) a creational design pattern that keeps a set of initialized objects ready to use
//    i.e. using an object pool rather than allocating and destroying them on demand
//    ex. database connections, socket connections, threads, large graphic objects like fonts/bitmaps
// 2) advantages:
//    object pool significantly improves performance
// 3) disadvantages:
//    object pool complicates object lifetime
//    objects obtained from and returned to a pool are not actually created or destroyed at this time
//      therefore, it requires extra care in implementation
// 4) used when:
//    working with a large number of objects expensive to instantiate, and
//    each object is only needed for a short period of time
// 5) implementation details:
//    a) the client requests an object from the pool and performs operations on the returned object
//       when the client finishes, it returns the object to the pool rather than destroying it
//    b) if no objects are present in the pool, a new object/item is created and returned
//    c) if resources are limited, a maximum number of objects is specified
//    d) object pooling may require resources, ex. memory or network sockets
//       therefore, it is preferable that the number of instances in the pool is low (not required)
//    e) handling of empty pools:
//       if it fails to provide an object, then return an error to the client
//       allocating a new object increases the size of the pool
//    f) in a multithreaded environment
//       a pool may block the client until another thread releases an object to the pool

// object pool with context manager (in garbage collected languages, i.e. without destructors):
//   do it manually/explicitly (not recommended)
//     requesting an object from the factory
//     returning the object by calling a dispose method
//   use try/finally construct or context manager (recommended)
//     using a finalizer is not a good idea as there are no guarantees on when the finalizer will be run

// object pool with smart pointers, ex. C++: smart pointers
//   in the constructor of the smart pointer, an object can be requested from the pool
//   in the destructor of the smart pointer, the object can be released back to the pool
