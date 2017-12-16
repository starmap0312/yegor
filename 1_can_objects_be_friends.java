// How can objects interact if they can't exchange data?
// 1) use decorators
// 2) give all decorators access to all private attributes of an object
//
// example:
interface Temperature {
    public String toString();
}

class TempCelsius implements Temperature { // the class represents a temperature object
    private int t;  // make it accessible by its decorator

    public String toString() {
        return String.format("%d C", this.t);
    }
} // its only responsibility is to print temperature in Celsius

// if we want to have another object to print temperature in Fahrenheit, create decorators
class TempFahrenheit implements Temperature {
    private TempCelsius origin;            // the class decorates a TempCelsius

    public String toString() {
      return String.format("%d F", this.origin.t * 1.8 + 32); // we should allow decorators to access the decoratee
    }
} // we should allow decorators to access decoratee's private fields

