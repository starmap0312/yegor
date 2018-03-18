// Stream
// problem: how to print only the first 10 of the following iterable while ignoring zeros and ones?
Iterable<Double> probes;

// 1) procedural way (imperative)
int pos = 0;
for (Double probe : probes) {
    if (probe == 0.0d || probe == 1.0d) {
        continue;
    }
    if (++pos > 10) {
        break;
    }
    System.out.printf("Probe #%d: %f", pos, probe * 100.0d);
}

// 2) functional way (declarative)
StreamSupport.
    stream(probes.spliterator(), false).
    filter(p -> p == 0.0d || p == 1.0d).
    limit(10L).
    forEach(
        probe -> System.out.printf("Probe #%d: %f", 0, probe * 100.0d)
    );
// but there is no forEachWithIndex() in Java Stream API, so the above will print Probe #0 for all probes
// the following is a workaround with an atomic counter
AtomicInteger index = new AtomicInteger(); // use a global AtomicInteger in the forEach method
StreamSupport.
    stream(probes.spliterator(), false).
    filter(probe -> probe != 0.0d && probe != 1.0d).
    limit(10L).
    forEach(
        probe -> System.out.printf("Probe #%d: %f", index.getAndIncrement(), probe * 100.0d)
    );
// why is it bad?
// we got back to the good old procedural global variable (the counter)
// we don't really see what's going on inside those filter(), limit(), and forEach() methods
//   i.e. each method in the Stream interface returns an instance of some class
//        but we have no idea by just looking at this code

// the biggest issue with the Stream API is that it's huge (it has 43 methods)

// 3) OOP way
new And(
    new Mapped<Double, Scalar<Boolean>>(
        new Limited<Double>(
            new Filtered<Double>(
                probes,
                probe -> probe != 0.0d && probe != 1.0d
            ),
            10
        ),
        probe -> () -> {
            System.out.printf("Probe #%d: %f", 0, probe * 100.0d);
            return true;
        }
    )
).value();
// why is it good?
// we know what exactly is going on
//    first,  Filtered decorates our iterable probes to take certain items out of it (Filtered implements Iterable)
//    second, Limited takes only the first ten items out                             (Limited  implements Iterable)
//    third,  Mapped converts each probe into an instance of Scalar<Boolean>, which does the line printing
//    fourth, And goes through the list of "scalars" and ask each of them to return boolean
//      they print the line and return true
//      Since it's true, And makes the next attempt with the next scalar
//      Finally, its method value() returns true
// all classes and interfaces are small, so they're very composable (reusable and testable)
//   to do something else we create a new decorator and use it
//   ex. to make an iterable of probes limited we decorate it with Limited
//       to make it filtered we decorate it with Filtered

// but there are no indexes, so in order to do that we just use another class, called AndWithIndex
new AndWithIndex(
    new Mapped<Double, Func<Integer, Boolean>>(
        new Limited<Double>(
            new Filtered<Double>(
                probes,
                probe -> probe != 0.0d && probe != 1.0d
            ),
            10
        ),
        probe -> index -> {
            System.out.printf("Probe #%d: %f", index, probe * 100.0d);
            return true;
        }
    )
).value();
// instead of Scalar<Boolean> we now map our probes to Func<Integer, Boolean> to let them accept the index  
