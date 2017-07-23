// 1) jcabi-http
package com.jcabi.http;
    public interface Request  // responsible to construct RequestURI/RequestBody instances and provides Response instance 
        /* the public constant literals related to this interface that can be used by the outside world            */
        String GET = "GET";   // the public constant literals defined in this interface
        String POST = "POST"; //   ex. Request.GET, ex. Request.POST, etc.
        /* the public methods declared in this interface, the contract that all of its implementations must follow */
        RequestURI uri();
        RequestBody body();
        RequestBody multipartBody();
        Response fetch() throws IOException;

    public interface RequestBody
        Request back();      // for backtrace to its Request instance
    public interface RequestURI
        Request back();      // for backtrace to its Request instance
    public interface Response
        Request back();      // for backtrace to its Request instance

    /* an interface for implementation of anonymous Wire classes */
    public interface Wire    // responsible to construct Response instances
        Response send(...);
    // ex.
    // new Wire() {
    //     @Override
    //     public Response send(...) {
    //         return new DefaultResponse(...);
    //     }
    // }

package com.jcabi.http.request;
    final class BaseRequest implements Request {             // a class is either final or abstract
        private static final String ENCODING = "UTF-8";      // constant used in this class (hidden information)

        BaseRequest(final Wire wire, ...) { ... }            // the class's collaborator (Wire) is passed in by constructor

        public RequestURI uri() {                            // BaseRequest provides its own RequestURI implementation
            return new BaseRequest.BaseURI(this, ...);
        }
        public RequestBody body() {                          // BaseRequest provides its own RequestBody implementation
            return new BaseRequest.FormEncodedBody(this, ...);
        }
        public RequestBody multipartBody() {                 // BaseRequest provides another RequestBody implementation
            return new BaseRequest.MultipartFormBody(this, ...);
        }
        public Response fetch() throws IOException {         // BaseRequest delegates construction of Response to its wire
            return this.wire.send(this, ...);
        }
        // fluent design: single entry point (Request) for creating related/dependent classes (RequestURI/Response)
        //   Request -> RequestURI
        //           -> RequestBody
        //           -> Response
        // note, all the above related classes pass "this" during the construction
        //   i.e. as one of the characteristics of the instance of the related classes
        //   so the receiver of the instances can backtrace to its dependent Request instance
        //   ex. Request request = new BaseRequest(new Wire(...));
        //       RequestURI uri = request.uri();      // uri is an instance of BaseRequest.BaseURI
        //       RequestBody body = request.body();   // body is an instance of BaseRequest.FormEncodedBody
        //       Response response = request.fetch(); // the implementation of Response depends on the request's wire
        //       uri.back();                          // returns req
        //       body.back();                         // returns req
        //       response.back();                     // returns req

        /* BaseRequest defines its own RequestURI/RequestBody implementations as inner classes                   *
         *   private to outside world: because classes that receive these instances should program to interfaces *
         *   not implementations, so they need not to know the implementation details                            */
        private static final class BaseURI implements RequestURI { ... }
        private static final class FormEncodedBody implements RequestBody { ... }
        private static final class MultipartFormBody implements RequestBody { ... }
        // note: the inner classes are "static", as they need not to access members of the Request instance

        // used to set Request's this.method, it returns a new BaseRequest with new http method 
        public Request method(final String method) {
            return new BaseRequest(..., method, ...);
        }

        // used to decorate this.wire, it returns a new BaseRequest with a decorated Wire
        public <T extends Wire> Request through(final Class<T> type) { // pass in a Wire decorator (implements Wire)
            return new BaseRequest(decoratedWire, ...);
        }
    }

    /* an implementation of interface Request that has its own Wire implementation (declared as static final)    */
    final class JdkRequest implements Request {
        // this Wire instance is declared as private static, so that it is shared by all JdkRequest instances
        private static final Wire WIRE = new Wire() { // JdkRequest has its own Wire implementation for sending requests
            @Override                                 // this annotation enables the check of compiler for overriding 
            public Response send(...) {               //   you receive a warning if not overriding and it improves readablity
                return new DefaultResponse(...);
            }
        }
        // this Request instance is declared private (non-static)
        //   so that every JdkRequest instance has its own instance with different coordinates (i.e. uri)
        private final transient Request base;

        public JdkRequest(final URI uri) {                     // secondary constructor which uses the primary constructor
            this(uri.toString());
        }

        public JdkRequest(final String uri) {                  // primary constructor
            this.base = new BaseRequest(JdkRequest.WIRE, uri); // it uses and delegates responsibilities to a BaseRequest
        }
    }

    /* an implementation of interface Request that has its own Wire implementation (declared as static final)    */
    final class ApacheRequest implements Request {    // similar to JdkRequest but uses Apache library for sending requests

    }

    /* a default implementation of interface Response */
    public final class DefaultResponse implements Response {
    
        public DefaultResponse(final Request request, ...) {
            this.req = request;                              // used by back() method to trace back to its Request
        }

        public Request back() { return this.req; }

        // wraps itself into another decorator, ex. response.as(XMLResponse.class)
        public <T extends Response> T as(final Class<T> type) {
            return type.getDeclaredConstructor(Response.class).newInstance(this);
        }
    }

// Java nested class:
// 1) why use nested class
//    a way of logically grouping classes
//    encapsulation/privacy
//    readability 
// 2) non-static vs. static
//   a nested class is a member of its enclosing class
//   a) non-static nested classes (inner classes) have access to other members of the enclosing class
//        even if they are declared private
//      it can exist only as a field within an instance of the enclosing class
//      it has direct access to the fields & methods of the instance of the enclosing class
//   b) static nested classes do not have access to other members of the enclosing class
//      i.e. it cannot refer directly to instance fields or methods of the instance of its enclosing class
//
// Java Anonymous Classes:
//   anonymous classes enable you to make your code more concise
//   a) declare and instantiate a class at the same time, without giving it a name
//   b) use anonymous classes if you need to use a local class only once

package com.jcabi.http.wire; // a package of Wire decorator (all implements Wire interface)
    public final class VerboseWire implements Wire { // the decorator implements the same interface as the decoratee
        private final transient Wire origin;         // the original Wire to be decorated

        public VerboseWire(final Wire wire) {
            this.origin = wire;
        }

        @Override
        public Response send(...) { ... }            // add additional responsibilities besides this.origin.send() 
    }

package com.jcabi.http.response;

    abstract class AbstractResponse implements Response {

        private final transient Response response;

        AbstractResponse(final Response resp) {
            this.response = resp;
        }

        @Override
        public final Request back() { return this.response.back(); } // direct delegation

        @Override                                                    // direct delegation
        public final <T extends Response> T as(final Class<T> type) { return this.response.as(type); }
    }

    public final class XMLResponse extends AbstractResponse {        // inherit all responsibilities from AbstractResponse

        public XMLResponse(final Response resp) { ... }              // decorates a Response

        public XML xml() {                                           // add an additional responsibility
            return new XMLDocument(this.body()).merge(this.context());
        }
    }

    public final class JsonResponse extends AbstractResponse {

    }
