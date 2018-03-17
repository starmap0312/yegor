// Fluent interface
//   in a Fluent Interface, the return value of a method will be the same instance on which the method was called.
//   ex.
queryBuilder.
    select("name").
    from("user").
    where("id > 100").
    orderBy("name", "asc");
// pros:
//   the convenient method chaining makes the code short and readable
//   the client code is easier to read, as it aligns with the domain language (users are easier to use fluent interface)
//   fluent interfaces are perfect for their users, since all methods are in one place and the amount of classes is very small
// cons:
// 1) Fluent Interfaces break Decorators (and Composition), so the class grows when new functionalities are introduced
//    this makes developers harder to maintain the code
// ex.
interface Counter {
    public Counter count();
    public Integer getCount();
}

class EchoingCounter implements Counter { // a decorator class
    private Counter counter;

    public EchoingCounter(Counter counter) {
        this.counter = counter;
    }

    public Counter count() {
        System.out.println("Echo");       // decorates the method with an printed "Echo"
        return this.counter.count();      // delegate to the wrapped object's count() method
    }

    public Integer getCount() {
        System.out.println("Echo");
        return this.counter.getCount();
    }
}

counter = new EchoingCounter(new FluentCounter());
counter = counter.
    count(). // "Echo" is printed only once, because this returns the wrapped object FluentCounter()
    count(); // "Echo" is NOT printed here

// 2) Fluent Interfaces are harder to Mock
//    All fluent methods need explicit mocking:
//      we need to mock every fluent method regardless of the parameters or even when we don't have expectations on the API
//      ex. methods count() return a Counter, so the mock object should do that too?

// example: jcabi-http 
String html = new JdkRequest("https://www.google.com").
    method("GET").          // returns a Request
    fetch().                // returns a Response
    as(RestResponse.class). // decorates the Response with RestResponse, in order to make it method-richer
    assertStatus(200).
    body();
// the only way to add a new functionality to class JdkRequest (implements Request) is to make the class bigger
// ex. first, it has method() and fetch() methods
//     then, multipartBody() and timeout() methods are added

// workaround:
//   class Response has a as(RestResponse.class) method to decorates itself
//   so that we don't have to make class Response contain 50+ methods
//   but in Java, we have to rely heavily on Reflection and type casting to achieve this
//   without the Reflection and type casting, you have to write the following code:
class Response {
    RestResponse asRestResponse() {
        return new RestResponse(this);
    }
    XmlResponse asXmlResponse() {
        return new XmlResponse(this);
    }
    // 50+ methods
}

class RestResponse implements Response { // an decorator class
  private final Response origin;
  // Original seven methods from Response
  // Additional 14 methods
}

// in summary, fluent interfaces mean large classes or some ugly workarounds

// better design (in OOP respective): use decorators
String html = new BodyOfResponse(
    new ResponseAssertStatus(
        new RequestWithMethod(
            new JdkRequest("https://www.google.com"),
          "GET"
        ),
        200
    )
).toString();
// cons:
//   we will have to remember many of the names of the classes
//    the construct looks rather difficult to read, as it's very far away from the DSL idea
// pros:
//   each object is small, very cohesive and they are all loosely coupled
//   adding new functionality to the library is as easy as creating a new class (no need to touch existing classes) 
//   unit testing is simplified, since classes are small
//   all classes can be immutable

// trade-off
//   there seems to be a conflict between usefulness and maintainability
//   fluent interfaces are good for users, but bad for library developers
//   small objects are good for developers, but difficult to understand and use
