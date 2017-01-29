// primary and secondary constructors
// 1) primary constructor:
//    the one that constructs an object and encapsulates other objects inside it
// 2) secondary constructor:
//    a preparation step before calling a primary constructor
//
// example
//
// (good design)

final class Cash {

    private final int cents;
    private final String currency;

    public Cash() {                    // secondary constructor (uses primary constructor)
        this(0);
    }

    public Cash(int cts) {             // secondary constructor (uses primary constructor)
        this(cts, "USD");
    }

    public Cash(int cts, String crn) { // primary constructor
        this.cents = cts;
        this.currency = crn;
    }
    // methods here
}

// rule of thumb:
//   only one primary constructor, which should be declared after all secondary ones
//   all classes should have a single entry point (point of construction)
//     i.e. one primary constructor: this helps eliminate code duplication
//
// (bad design)

final class Cash {

    private final int cents;
    private final String currency;

    public Cash() {                    // primary constructor
        this.cents = 0;
        this.currency = "USD";
    }

    public Cash(int cts) {             // primary constructor
        this.cents = cts;
        this.currency = "USD";
    }

    public Cash(int cts, String crn) { // primary constructor
        this.cents = cts;
        this.currency = crn;
    }
    // methods here
}

// in python, you cannot define multiple constructors
//   however, you can define a default value if one is not passed
//
// example:
//
// class Cash(object):
//
//     def __init__(self, cents=0, currency="USD"):
//         self.cents = cents
//         self.currency = currency

