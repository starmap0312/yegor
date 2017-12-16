import datetime
# vertical vs. horizontal decomposition of responsibility
#   vertical decomposition is better!
#
# example:
#
# (bad design: before decomposition)
# a class responsible for two things: formatting the text and writing the formated text to a file
class FileTimedLog(object):

    def __init__(self, path):
        self.file = open(path, 'a')

    def put(self, text):
        line = datetime.datetime.now().strftime("%B %d, %Y: ") + text
        self.file.write(line)

FileTimedLog('/tmp/log.txt').put('Message\n')

# why is it bad?
# 1) it is not re-usable
# 2) it is not extensible
#
# (not so good design: horizontal decomposition of responsibilities)
# horizontal decomposition increases complexity as client has more dependencies and points of contact
#
# class Script --> class Log
#              --> class Line
#
# the class is responsible for only writing the line to a file
class FileLog(object):

    def __init__(self, path):
        self.file = open(path, 'a')

    def put(self, line):
        self.file.write(str(line))

# an additional class that is responsible for formatting the text
class TimedLine(object):

    def __init__(self, text):
        self.line = text

    def __str__(self):
        return datetime.datetime.now().strftime("%B %d, %Y: ") + self.line

# the client class needs to know both classes (collaborators) to provide its service
# i.e. Script --> TimedLine
#             --> FileLog
class Script(object):

    def write(self, text, filepath):
        line = TimedLine(text)
        log = FileLog(filepath)
        log.put(line)

script = Script().write('Message\n', '/tmp/log.txt')

# why is it not so good?
# 1) the Scirpt class depends on two collaborators, i.e. class TimedLine and class FileLog
#
#
# (good design: vertical decomposition of responsibilities)
# vertical decomposition decreases complexity
#
# Script --> TimedLog --> Log
#
# the class is responsible for only writing the line to a file (no change)
class FileLog(object):

    def __init__(self, path):
        self.file = open(path, 'a')

    def put(self, line):
        self.file.write(str(line))

# a decorator class that decorates Log's put() method by prepending the current time string
class TimedLog(object):

    def __init__(self, log):
        self.origin = log

    def put(self, text):
        self.origin.put(datetime.datetime.now().strftime("%B %d, %Y: ") + text)

# the client class has only one entry point to Log object
# i.e. Script --> TimedLog --> Log
class Script(object):

    def write(self, text, log):
        # the Log object "consists" of two objects, one wrapped into another
        log = TimedLog(log)
        log.put(text)

script = Script().write('Message\n', FileLog('/tmp/log.txt'))

# why is it good?
# 1) the reponsibilities are still well separated, so it is easier to reuse and extend
# 2) the client class depends on one class, i.e. class TimedLog, one single entry point

