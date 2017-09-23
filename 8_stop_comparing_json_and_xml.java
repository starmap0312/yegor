// JSON vs. XML as data format
// 1) Json is shorte and easier to understand
// 2) but XML is not just a data format but a language
//
// JSON is a simple data format with no additional functionality
// 1) Json's best-use case is AJAX
// 2) in all other cases, use XML instead
//
// example
// (JSON)
{
  "id": 123,                     // an attribute (more like metadata, i.e. data about data)
  "title": "Object Thinking",    // a node/field 
  "author": "David West",        // a node/field
  "published": {                 // a node/field with inner nodes/fields
    "by": "Microsoft Press",
    "year": 2004
  }
}
// (XML)
<book id="123">                  // an attribute
  <title>Object Thinking</title> // a node/field
  <author>David West</author>    // a node/field
  <published>                    // a node/field with inner nodes/fields
    <by>Microsoft Press</by>
    <year>2004</year>
  </published>
</book>

// why is XML better than JSON?
//   it can validate itself (XML Schema)
//   it knows how to modify itself (XSL)
//   it gives very convenient access to anything inside it (XPath)
// 1) XPath: XPath 2.0 is a very powerful query engine with its own functions, predicates, axes, etc.
//    to query data:
//      the year of publication from the document
//    send an XPath query:
//      /book/published/year/text() => returns "2004"
//    or query data:
//      How many books were published by David West in 2004?
// 2) Attributes and Namespaces: attach metadata to the data
//    this helps in organizing and structuring information
// 3) XML Schema: to make sure its structure is not broken by any of these actions
//    everyone who wants to work with the document can first validate its correctness using the schema supplied
//    ex. one use <year> to store the publication date, whereas another uses <date> with ISO-8601
// 4) XSL transformation: make modifications to your XML document without any Java/Ruby/etc. code at all
//    create an XSL transformation document and apply it to your original XML
//    the XSL language (purely functional) is designed for hierarchical data manipulations
//      it is more suitable for this task than Java or any other OOP/procedural approach

