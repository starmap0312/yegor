// Object Behavior Must Not Be Configurable
// 1) there should not be any "configurations" in OOP, ex. Spring XML configuration mechanisms
//    this makes objects implicitly mutable because we can configure them
//    i.e. we "change their behavior" by injecting parameters or 
//         by injecting an entire setting/configuration object into them
// 2) using object properties as configuration parameters is a very common mistake
// 3) encapsulated properties must not be used to change the behavior of an object
//    an object's properties are its inherent characteristics (in order to represent a real-world entity)
//    an object's behavior should be immutable
//
// example:
// (bad design: procedural programming)
//
// from simple to complex, adding configuration fields or one complex, configuration object
//
// 1) basic class (works fine) 
//
class Page {                  // a class that represents a web page
    private final String uri;

    Page(final String address) {
        this.uri = address;
    }

    public String html() throws IOException { // read the content of a webpage, decode by "UTF-8" 
        return IOUtils.toString(new URL(this.uri).openStream(), "UTF-8");
    }
}

// the client code: read the content of Google front page
String html = new Page("http://www.google.com").html();

// 2) extend the functionality of the class to decode by other encoding
//
// (bad design: the class becomes a configurable class)
class Page {
    private final String uri;
    private final String encoding;            // add a configurable field

    Page(final String address, final String enc) {
        this.uri = address;
        this.encoding = enc;
    }

    public String html() throws IOException { // the object's behavior changes due to different configurations
        return IOUtils.toString(new URL(this.uri).openStream(), this.encoding);
    }
}

// 3) extend the functionality of the class: can handle an empty page
//
// (bad design: add a configurable field)
class Page {
    private final String uri;
    private final String encoding;      // a configurable field
    private final boolean alwaysHtml;   // another configurable field

    Page(final String address, final String enc, final boolean always) {
        this.uri = address;
        this.encoding = enc;
        this.alwaysHtml = always;
    }

    public String html() throws IOException { // the object's behavior changes due to different configurations
        String html = IOUtils.toString(new URL(this.uri).openStream(), this.encoding);
        if (html.isEmpty() && this.alwaysHtml) { // the method can have different behaviors
            html = "<html/>";                    // return "<html/>" if an empty page is loaded
        }
        return html;
    }
}

// 4) extend the functionality of the class: can handle unknown encoding 
// (bad design: use a configuration field)
class Page {
    private final String uri;
    private final String encoding;      // a configurable field
    private final boolean alwaysHtml;   // a configurable field
    private final boolean encodeAnyway; // add one more configurable field

    Page(final String address, final String enc, final boolean always, final boolean encode) {
        this.uri = address;
        this.encoding = enc;
        this.alwaysHtml = always;
        this.encodeAnyway = encode;     // yet another configurable field
    }

    public String html() throws IOException, UnsupportedEncodingException {
        final byte[] bytes = IOUtils.toByteArray(new URL(this.uri).openStream());
        String html;
        try {
            html = new String(bytes, this.encoding); // the object's behavior changes due to different encoding
        } catch (UnsupportedEncodingException ex) {
            if (!this.encodeAnyway) {
                throw ex;                            // throw an exception if encodeAnyway is not configured
            }
            html = new String(bytes, "UTF-8");       // use default "UTF-8" encoding if encodeAnyway is configured
        }
        if (html.isEmpty() && this.alwaysHtml) {
            html = "<html/>";                        // return an empty page if alwaysHtml is configured
        }
        return html;
    }
}

// 5) encapsulate the configuration fields in an object
//
// (bad design: wrap all configurable fields into a configuration object)
class Page {
    private final String uri;
    private final PageSettings settings; // encapsulate all configurable fields in one object

    Page(final String address, final PageSettings setting) {
        this.uri = address;
        this.settings = setting;
    }

    // it has one big method with many logics and case handling, hard to extend and maintain
    public String html() throws IOException {
        final byte[] bytes = IOUtils.toByteArray(new URL(this.uri).openStream());
        String html;
        try { // the object's behavior changes due to different configurations
            html = new String(bytes, this.settings.getEncoding());
        } catch (UnsupportedEncodingException ex) {
            if (!this.settings.isEncodeAnyway()) {
                throw ex;
            }
            html = new String(bytes, "UTF-8")
        }
        if (html.isEmpty() && this.settings.isAlwaysHtml()) {
            html = "<html/>";
        }
        return html;
    }
}

// the client code
String html = new Page(
    "http://www.google.com",
    new PageSettings()              // construct a configuration object
        .withEncoding("ISO_8859_1")
        .withAlwaysHtml(true)
        .withEncodeAnyway(false)
).html();

// why is it bad?
// 1) the object is responsible for too many things, a big and non-cohesive object
//    too many logic in one single method
// 2) the code becomes less testable, less maintainable and less readable
//
// (good design: distribute the responsibilities by using composable decorators)
//
// the client code
Page page = new NeverEmptyPage(new DefaultPage("http://www.google.com"))
// DefaultPage reprsents a web page and is responsible for one thing, i.e. html(): read the content as html
//   the page is decorated with NeverEmptyPage which falls back to an empty html if the page is empty
String html = new AlwaysTextPage(new TextPage(page, "ISO_8859_1"), page).html();
//   the page is decorated with TextPage which sets the encoding to ISO_8859_1
//   the page is decorated with AlwaysTextPage which falls back the encoding to UTF-8 if unknown

// the core class: responsible for one simple thing (a cohesive object)
class DefaultPage implements Page {
    private final String uri;

    DefaultPage(final String address) {
        this.uri = address;
    }

    @Override
    public byte[] html() throws IOException {
        return IOUtils.toByteArray(new URL(this.uri).openStream());
    }
}

// a decorator class: extend the functionality of the html() method 
class NeverEmptyPage implements Page {
    private final Page origin;

    NeverEmptyPage(final Page page) {
        this.origin = page;
    }

    @Override
    public byte[] html() throws IOException { // enhance the html() method to handle empty page
        byte[] bytes = this.origin.html();
        if (bytes.length == 0) {
            bytes = "<html/>".getBytes();
        }
        return bytes;
    }
}

// the client code
Page page = new NeverEmptyPage(new DefaultPage("http://www.google.com"));

// a decorator class: has a single configuration field for encoding
class TextPage {
    private final Page origin;
    private final String encoding; // an additional parameter for encoding

    TextPage(final Page page, final String enc) {
        this.origin = page;
        this.encoding = enc;
    }

    public String html() throws IOException { // enhance the html() method to handle different encoding
        return new String(this.origin.html(), this.encoding);
    }
}

// the client code
Page page = new TextPage(Page, "ISO_8859_1");

// a decorator class
class AlwaysTextPage {
    private final TextPage origin;
    private final Page source;

    AlwaysTextPage(final TextPage origin, final Page source) {
        this.origin = origin;                 // an encoded page (the encoding may be unknown)
        this.source = source;                 // default original page
    }

    public String html() throws IOException { // enchance the html() method to handle unknown encoding
        String html;
        try {
            html = this.origin.html();
        } catch (UnsupportedEncodingException ex) {
            html = new TextPage(this.source, "UTF-8").html(); // this creates another HTTP request if
        }                                                     // the encoding is unknown
        return html;
    }
}

// the client code
Page page = new NeverEmptyPage(new DefaultPage("http://www.google.com"))
Page textpage = new AlwaysTextPage(new TextPage(page, "ISO_8859_1"), page)

// one more decorator class to avoid duplicate HTTP request (optional)
class OncePage implements Page {
    private final Page origin;
    private final AtomicReference<byte[]> cache = new AtomicReference<>;

    OncePage(final Page page) {
        this.origin = page;
    }

    @Override
    public byte[] html() throws IOException {
        if (this.cache.get() == null) {
            this.cache.set(this.origin.html());
        }
        return this.cache.get();
    }
}

// the final client code: create a multi-decorated page and then get its html content
Page page = new NeverEmptyPage(
    new OncePage(
        new DefaultPage("http://www.google.com")
    )
)
String html = new AlwaysTextPage(new TextPage(page, "ISO_8859_1"), page).html();

