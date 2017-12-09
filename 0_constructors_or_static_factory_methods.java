// use "Static Factory Methods" or "Constructors" to instantiate objects
// 1) static methods are pure evil, so use constructors
//
// (good design: constructors)
class Color {

    private final int hex;

    Color(String rgb) {                   // secondary constructor
        this(Integer.parseInt(rgb, 16));
    }

    Color(int red, int green, int blue) { // secondary constructor
        this(red << 16 + green << 8 + blue);
    }

    Color(int h) {                        // primary constructor
        this.hex = h;
    }
}

// (bad design: static factory methods)
class Color {

    private final int hex;

    static Color makeFromRGB(String rgb) {
        return new Color(Integer.parseInt(rgb, 16));
    }

    static Color makeFromPalette(int red, int green, int blue) {
        return new Color(red << 16 + green << 8 + blue);
    }

    static Color makeFromHex(int h) {
        return new Color(h);
    }

    private Color(int h) {
        this.hex = h;
    }
}

// three false advantages of using static factory methods
// 1) static factory methods have more descriptive names
// (problem: no name when constructing objects)
Color tomato = new Color(0xFF6347);    // use hex intger to construct object
Color tomato = new Color(255, 99, 71); // use rgb integers to construct object
// (bad design: use static factory methods)
Color tomato = Color.makeFromPalette(255, 99, 71); // static method has more descriptive names

// (good design: use constructors with polymorphism)
// use polymorphism and encapsulation to improve upon constructors
// i.e. decompose the problem into a few semantically rich classes
interface Color { }               // creates an interface

class HexColor implements Color { // creates a subclass for the primary constructor

    private final int hex;

    HexColor(int h) {
        this.hex = h;
    }
}

class RGBColor implements Color { // creates a subclass for the secondary constructor

    private final Color origin;   // an adapter class

    RGBColor(int red, int green, int blue) {
        this.origin = new HexColor(red << 16 + green << 8 + blue);
    }
}

Color tomato = new RGBColor(255, 99, 71);

// 2) static factory methods can cache
// (problem: inefficiently instantiate every objects)
Color tomato = new Color(255, 99, 71); // instantiate a first object
// ... sometime later
Color red = new Color(255, 99, 71);    // instantiate a secondary object 

// (bad design)
Color tomato = Color.makeFromPalette(255, 99, 71); // we can cache the first object inside class Color
// ... sometime later
Color red = Color.makeFromPalette(255, 99, 71);    // we don't need to instantiate another object 

class Color {

    private static final Map<Integer, Color> CACHE = new HashMap<>(); // cache objects in a Map
    private final int hex;

    static Color makeFromPalette(int red, int green, int blue) {
        final int hex = red << 16 + green << 8 + blue;
        return Color.CACHE.computeIfAbsent(hex, h -> new Color(h));   // instantiate object only if absent
    }

    private Color(int h) {
        return new Color(h);
    }
}

// (good design: introduce a new class Palette, which becomes a store of colors)
class Palette {

    private final Map<Integer, Color> colors = new HashMap<>();

    Color take(int red, int green, int blue) {
        final int hex = red << 16 + green << 8 + blue;
        return this.computerIfAbsent(hex, h -> new Color(h));
    }
}

Color tomato = palette.take(255, 99, 71);
// Later we will get the same instance:
Color red = palette.take(255, 99, 71);

// 3) static factory methods can subtype
// (bad design)
class Color {

    protected final int hex;

    Color(int h) {
        this.hex = h;
    }

    public Color lighter() {           // shift the color to the next available lighter one
        return new Color(hex + 0x111);
    }
}

class PantoneColor extends Color {     // a subtype class with a collaborator PantoneName
    // sometimes it's more desirable to pick the next lighter color through a set of available Pantone colors

    private final PantoneName pantone;

    PantoneColor(String name) {
        this(new PantoneName(name));
    }

    PantoneColor(PantoneName name) {
        this.pantone = name;
    }

    @Override
    public Color lighter() {           // shift the color to the next available lighter pantone color
        return new PantoneColor(this.pantone.up()); // use its collaborator PantoneName to get a lighter color
    }
}

class Color {

    private final String code;

    // the static method makes the decision of create what subtype objects
    static Color make(int h) {         // use a static factory method to create subtype objects
        if (h == 0xBF1932) {           // if the true red color is requested, return an instance of PantoneColor
            return new PantoneColor("19-1664 TPX");
        }
        return new RGBColor(h);        // in all other cases it's just a standard RGBColor
    }
}

Color color = Color.make(0xBF1932);    // this creates a PantoneColor
Color color = Color.make(0xFF6347);    // this creates a RGBColor

// (not so good design: create a new class for making the decision of create what subtype objects)
interface Color {
    Color lighter();
}

class Colors {          // a new class responsible for creating what subtype objects
    Color make(int h) {
        if (h == 0xBF1932) {
            return new PantoneColor("19-1664-TPX");
        }
        return new RGBColor(h);
    }
}

colors.make(0xBF1932);
// why is it not so good?
// 1) we're taking the decision-making away from the object it belongs to, i.e. class PantoneColor
// 2) we tear our objects into two pieces
//    the first piece is the object itself
//    the second one is the decision making algorithm that stays somewhere else

// (good design: put the logic into an object of class PantoneColor which decorates original class RGBColor)
class PantoneColor {

    private final Color origin;

    PantoneColor(Color color) {
        this.origin = color;
    }

    @Override
    public Color lighter() {
        final Color next;
        if (this.origin.hex() == 0xBF1932) {
            next = new RGBColor(0xD12631);
        } else {
            next = this.origin.lighter();
        }
        return new PantoneColor(next);
    }
}

// we make an instance of RGBColor and decorate it with PantoneColor
Color red = new PantoneColor(new RGBColor(0xBF1932));
// we ask red to return a lighter color and it returns the one from the Pantone palette
//   not the one that is merely lighter in RGB coordinates:
Color lighter = red.lighter(); // 0xD12631

// why is it good?
// 1) the logic must stay inside the class, i.e. class PantoneColor, not somewhere outside
//    not in static factory methods or even in some other supplementary class
// 2) however, if it's something related to the management of class instances, then
//    there can be containers and stores, ex. class Colors

// rule of thumb
// 1) never use static methods, especially when they are going to replace object constructors
// 2) giving birth to an object through its constructor
