// Web Framework
//   ex. Play Framework, Servlets, JSP, JAX-RS, Spring Framework, Play Framework, JSF with Facelets, Spark, etc.
// why are they bad?
//   full of static methods, un-testable data structures, and dirty hacks
// Takes Framework
//   1) No NULLs (i.e. no ad-hoc null reference handling)
//   2) no public static methods (i.e. no global variable or global singleton/method)
//   3) no mutable classes (i.e. simpler to construct, test, use, and thread-safe)
//   4) no class casting, reflection, and instanceof operators
//
// Java Web Architecture in a Nutshell
//
// (Step 1)

// start a web server, by creating a new network socket that accepts connections on a certain TCP port

import java.net.ServerSocket;

public class Foo {

    public static void main(final String... args) throws Exception {
        final ServerSocket server = new ServerSocket(8080);
        while (true);
    }
}

// (Step 2)
//
// accept the incoming connections, through a blocking call to the accept() method

final Socket socket = server.accept();
// the method is blocking its thread and waiting until a new connection arrives
// as soon as that happens, it returns an instance of Socket
// in order to accept the next connection, we should call accept() again


// basically, our web server should work like this
public class Foo {

    public static void main(final String... args) throws Exception {

        final ServerSocket server = new ServerSocket(8080);
        while (true) { // loop for creating a socket for serving a new connection and close it afterwards
            final Socket socket = server.accept();
            // 1. Read HTTP request from the socket
            // 2. Prepare an HTTP response
            // 3. Send HTTP response to the socket
            // 4. Close the socket
        }
    }
}

// (Step 3)
// code for reading an input stream from the socket
final BufferedReader reader = new BufferedReader(
    new InputStreamReader(socket.getInputStream())
);

while (true) {
    final String line = reader.readLine();
    if (line.isEmpty()) {
        break;
    }
    System.out.println(line);
}

// example output of the above code
GET / HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Cache-Control: max-age=0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36
Accept-Encoding: gzip, deflate, sdch
Accept-Language: en-US,en;q=0.8,ru;q=0.6,uk;q=0.4

// the client (ex. Google Chrome) passes the above text into the connection established
// as soon as the connection is ready, it sends this text into it and waits for a response

// (Step 4)
// code for creating an HTTP response and sending back to the socket

import java.net.Socket;
import java.net.ServerSocket;
import org.apache.commons.io.IOUtils;

public class Foo {

    public static void main(final String... args) throws Exception {

        final ServerSocket server = new ServerSocket(8080);
        while (true) {
            try (final Socket socket = server.accept()) {
                IOUtils.copy(
                    IOUtils.toInputStream("HTTP/1.1 200 OK\r\n\r\nHello, world!"),
                    socket.getOutputStream()
                );
            }
        }
    }
}

// example response of the above code
$ curl http://localhost:8080 -v
* Rebuilt URL to: http://localhost:8080/
* Connected to localhost (::1) port 8080 (#0)
> GET / HTTP/1.1
> User-Agent: curl/7.37.1
> Host: localhost:8080
> Accept: */*
>
< HTTP/1.1 200 OK
* no chunk, no close, no size. Assume close to signal end
<
* Closing connection 0
Hello, world!

// Takes Framework
//   1) No NULLs
//   2) no public static methods
//   3) no mutable classes
//   4) no class casting, reflection, and instanceof operators
// 
// example
//
// create a working web application: i.e. create a single class that implements Take interface
import org.takes.Request;
import org.takes.Take;

public final class TkFoo implements Take {

      @Override
      public Response route(final Request request) {
          return new RsText("Hello, world!");
      }
}

// start a server
import org.takes.http.Exit;
import org.takes.http.FtBasic;

public class Foo {
    public static void main(final String[] args) throws Exception {
        new FtBasic(new TkFoo(), 8080).start(Exit.NEVER);
    }
}

