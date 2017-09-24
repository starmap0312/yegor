// both -ER and -Client are not good object names
//
// example: (bad design)
// 1) the client encapsulates the destination URL to the server with some access credentials
// 2) the client exposes a number of methods, which transport the data to/from the server
class AmazonS3Client {
    createBucket(String name);
    deleteBucket(String name);
    doesBucketExist(String name);
    getBucketAcl(String name)
    getBucketPolicy(String name);
    listBuckets();
    // 160+ more methods here
}
// why is it bad?
// 1) the client's scope is too broad
//    the client is an abstraction (client) of a server, so it represents the server's entire functionality
//    if the functionality is limited, ex. HttpClient, then the design is ok
//    but if the server is too complex, ex. AmazonS3Client, then the client also becomes complex 
// 2) the client is data focused
//    the client-server relationship is about transferring data (too data focused)
//    ex. HTTP RESTful API of the AWS S3 service
//        there are entities/objects on the AWS side: buckets, objects, versions, access control policies, etc.
//        the server turns them into JSON/XML data, and the client deals with JSON or XML
//        the data never really becomes active objects representing buckets, objects, or versions, etc.
//
// the consequences of the client design
// 1) Procedural code
//    the client receives the data: the code that works with that data will most likely be procedural
//    ex. AmazonS3Client receives S3Object, ObjectMetadata, BucketPolicy, PutObjectResult, etc.
//        these are all DTO (Data Transfer Objects) with only getters and setters inside.
// 2) Duplicated code
//    if we want to avoid DTO, we need to turn the data the client receives into objects
//    this leads to code duplication in multiple projects, otherwise, we need to create a library that does the conversion
//    ex. jcabi-s3 is to convert S3 SDK data
// 3) Difficulties with testing
//    because the client is a big class/interface, mocking it in unit tests or creating its test doubles/fakes is hard 
// 4) Static problems
//    the client class, even though its methods are not static, look very similar to utility classes
// 5) Extendability issues
//    it's almost impossible to decorate a client object when it has 160+ methods and keeps on growing
//    the only possible way to add new functionality is to create new methods, which leads to a monster class 
//
// What is the alternative?
// good design
// 1) replace clients with client-side objects that represent entities of the server side, not the entire server
//    ex. Bucket, Object, Version, Policy, etc. objects with functionality of real buckets, objects and versions
//        which the AWS S3 expose
// 2) create a high-level object that somehow represents the entire API/server, but it should be small
//    ex. Region object represents the entire AWS region with buckets
//        we could retrieve a bucket from it
//        to list objects in the bucket we ask the bucket to do it for us
//        there is no need to communicate with the entire "server object" every time
//          even though technically such a communication happens
//
// summary
// the trouble is not exactly in the name suffix, but in that the client object represents the entire server
//   on the client side rather than its entities
// such an abstraction/object is 1) too big and 2) very data driven
//
// good design examples
// object-oriented clients without client objects: jcabi-github, jcabi-dynamo, jcabi-s3, or jcabi-simpledb
