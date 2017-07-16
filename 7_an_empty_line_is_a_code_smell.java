// An Empty Line is a Code Smell
// 1) an empty line in a method is used for separation of concerns
// 2) a method should always do one thing:
//    so refactor the code, so that there is no empty line in a method
//
// example:
//
// (bad design)
final class TextFile {

    private final File file;

    TextFile(File src) {
        this.file = src;
    }

    public int grep(Pattern regex) throws IOException {
        Collection<String> lines = new LinkedList<>();
        // in grep() method, we try to do two things: read lines from a file & count the number of lines matching a pattern
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } // here, we may use an empty line, because the method tries to do another thing

        int total = 0;
        for (String line : lines) {
            if (regex.matcher(line).matches()) {
                ++total;
            }
        }
        return total;
    }
}
// the grep() method first loads the content of the file
//   it next counts how many lines match the regular expression provided
//
// (good design)

final class TextFile {
    private final File file;

    TextFile(File src) {
        this.file = src;
    }

    public int grep(Pattern regex) throws IOException { // refactor the method so that it contains a single line
        return this.count(this.lines(), regex);         // the two things are refactored to two private methods, and 
    }                                                   // the empty line is removed

    private int count(Iterable<String> lines, Pattern regex) { // count the number of lines matching the pattern
        int total = 0;
        for (String line : lines) {
            if (regex.matcher(line).matches()) {
                ++total;
            }
        }
        return total;
    }

    private Iterable<String> lines() throws IOException { // loads the content of the file to lines[]
        Collection<String> lines = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
            return lines;
        }
    }
}

// CSS example:
//
// (bad design)

.container {
    width: 80%;
    margin-left: auto;
    margin-right: auto; // there is an empty line here

    font-size: 2em;
    font-weight: bold;
}
// .container class is too complex and can be decomposed into two classes

// (good design)

.wide {
    width: 80%;
    margin-left: auto;
    margin-right: auto;
}

.important {
    font-size: 2em;
    font-weight: bold;
}
