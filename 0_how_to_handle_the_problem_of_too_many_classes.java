// 1) Interfaces are nouns
//    Types (also known as interfaces in Java) are the core part of domain vocabulary, so they have to be nouns
//    ex. Request, Directive, or Domain, etc. (there are no exceptions)
// 2) Classes are prefixed
//    classes always implement interfaces (which are requests, directives, or domains, etc.)
//    Prefix the type name (interface) to a class helps the users to remember they represent nouns
//    ex. RqBuffered  : a buffered request
//        RqSimple    : a simple request
//        RqLive      : a request that represents a "live" HTTP connection
//        RqWithHeader: a request with an extra header
//    alternatively, use the type name (interface) as the central part of the class name, and
//                   add a prefix that explains implementation details
//    ex. DyDomain    : a domain that persists its data in DynamoDB (you know Dy prefix explains implementation)
//        DyUser      : a user that persists its data in DynamoDB
