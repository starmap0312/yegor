// HTTP Repeater
// problem:
// 1) a web app is supposed to receive HTTP requests from GitHub (i.e. GitHub webhooks), but that app is down
// 2) GitHub gets an HTTP error, and never sends the request again
//    there is no way to receive it again once the app is back up
//
// solution:
// a service mesh between GitHub and web app, to accept HTTP requests and repeat them later if not delivered
// 1) GitHub sends all webhook PUT/POST requests to the ReHTTP server, which stores them in a database (ex. AWS DynamoDB)
// 2) ReHTTP attempts to deliver them immediately
//    if something goes wrong and the server HTTP response code is not in the 200-299 range,
//      the next attempt is made in about an hour
//    then it retries every hour for about a day
//    if all attempts fail, it abandons the request and that's it
// 3) you know how many requests were processed successfully over the last 24 hours, and how many of them failed
//
// Service Mesh
// 1) a service mesh is an infrastructure layer for making service-to-service communication reliable
//    ex. Ticketmaster, Paypal, etc. uses a service mesh
// 2) it is typically implemented as an array of lightweight network proxies deployed alongside application code
//    the application needs not to be aware the existence of a sercie mesh
// 3) the service mesh is a networking model that sits at a layer of abstraction above TCP/IP
//    it must also be capable of handling network failures
//    like the TCP stack which abstracts the mechanics of reliably delivering bytes between network endpoints,
//      the service mesh abstracts the mechanics of reliably delivering requests between services
//    Unlike the TCP, the service mesh can provide a uniform visibility and control into the application at runtime
//
//    TCP:
//      ComputerA             ComputerB
//      ----------------      ----------------
//      ServiceA          --  ServiceB
//      Networking Stack  --  Networking Stack
//
//    Serivce Library: (reusability of the library)
//      ComputerA                ComputerB
//      ----------------         ----------------
//      ServiceA             --  ServiceB
//      Library                  Library
//       [circuit-breaker]        [circuit-breaker]
//       [service discovery]      [service discovery]
//      Networking Stack     --  Networking Stack
//
//    drawbacks of using a Service Library:
//    i ) we need to re-deploying all services whenbever the library is updated
//    ii) the library is written in a specific language (ex. Java) and runs on a specific platform (ex. JVM)
//
//    Service Proxy: (transparent to the application)
//      ComputerA                            ComputerB
//      -------------------------------      -------------------------------
//      ServiceA -- Proxy (Sidecar)      --  Proxy (Sidecar)        ServiceB
//                  [circuit-breaker]        [circuit-breaker]
//                  [service discovery]      [service discovery]
//      Networking Stack                 --  Networking Stack
//
//    Service Proxy:
//      each of your services will have a companion proxy sidecar (a micro-service)
//      it runs aside your application and provides the service with extra features
//      the engineers can work on the business logic instead of the services infrastructure code
//    
// 4) reliably delivering requests in a cloud native application is incredibly complex
//    it may use  a wide array of powerful techniques: 
//    ex. circuit-breaking, latency-aware load balancing, consistent (advisory) service discovery, retries, and deadlines
