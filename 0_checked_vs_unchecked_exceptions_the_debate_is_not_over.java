// JAVA checked exceptions vs. unchecked exceptions
//
//   Object -> Throwable -> Error     -> AssertionError                           (unchecked exception)
//                       -> Exception -> RuntimeException -> NullPointerException (unchecked exception)
//                                    -> InterruptedException                     (checked exception) 
//                                    -> ...
//
// you can throw everything that is Throwable
// 1) if you throw checked excpetions (i.e. not Error/RuntimeException)
//    the compiler will check for you and any method beyond must be declared with "throws" keyword
// 2) if a method catches it without re-throwing, it doesn't need the throws keyword (not recommended)
// 3) python only has unchecked excpeitons (no checked exceptions at compile-time)
//    i.e. you don't know if a method may throw any exception written by the programmer
//
// unchecked vs. checked exceptions:
// 1) unchecked exceptions:
//    exceptions that extend RuntimeException/Error
//    compiler will never force you to catch unchecked exceptions
//    methods do not need to be declared with the throws keyword
// 2) checked exceptions:
//    all other exception types not extending RuntimeException/Error
//    there will be compiler error for methods not declared with throws keyword
//    i.e. checked exceptions need to be explicitly catched or rethrown
//
// why is unchecked exception bad?
//   hiding the fact that a method may fail
//   often, a method is too complex and want to keep some exceptions "hidden" (i.e. unchecked)
//   try to refactor method so that it is responsible for one thing and throws checked exception if fails
//
// why is checked exception good?
//   unlike unchecked exceptions, we can't ignore failures: need to try/catch somewhere
//   every method is either "safe" (throws nothing) or "unsafe" (throws Exception)
//     to be safe, the method needs to handle the excpetion by itself
//     otherwise, the method is not safe and the client have to worry about the safety
//
// rule of thumbs:
// 1) always use checked exceptions and never use unchecked exceptions
//    all methods are declared either as "safe" (throws nothing) or "unsafe" (throws Exception)
// 2) never use exceptions for flow control: use only Exception without any sub-types
//    we don't need multiple exception types, if we don't use exceptions to control flow
// 3) never recover from exceptions
//    i.e. no fail slow: never catch without rethrowing
//
// example.
// (bad design: use Exception subtypes for control flow)
//   use an Exception subtype for memory allocation errors (unchecked exception)
//   ex. OutOfMemoryException
// (good design: introduce some memory manager for control flow, writing methods to check the memory)
//   use a method to tell whether heap is big enough for the next operation
//   ex. bigEnough()
// (bad design: fail slow, recover from the exception)
try {
    save(file, data);
} catch (Exception ex) {
    // can't save the file, but it's OK and move on and do something else
}

// (good design: handle the exception at the top)
App::run()                 // if want to recover from exception, go up to the top and start the chain again
    Data::update()
        Data::write()
            File::save()   // an exception is thrown
                           // don't do anything in catch block: only report problem and rethrow

