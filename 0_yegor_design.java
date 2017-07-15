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
    final class BaseRequest implements Request               // a class is either final or abstract
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
        // fluent design of classes: single entry point (Request) of creating related/dependent classes
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

