arbel_rivitz
toozig


=============================
=      File description     =
=============================
Sjavac.java

Line.java

Facade.java

Method.java

Parser.java

ParsingException.java

Mscope.java

TypeFactory.java

Variables.java



=============================
=          Design           =
=============================

The program we were asked to write is about checking if a Sjava file is valid or not.
It required to fully understand the format of sJava file and how to work with text files.


The design of the program and the class division is based on the aspects which we marked as the main
components of the program:

Parser & exceptions classes:
Since the basic of understanding the file is to read it, we had to make a  class that it's main
focus is about reading and interpret the file. Since the parsing is common to all the classes, and does not
changes over time, we chose to make it a singleton.


The parser handles the reading, starts with processing the file and turning it to list of string
 and going to  see if it's lines follows the Sjava logic. which means it is the part which throws
the Exceptions. We found the exception mechanism very useful to do that, since it allow us to immediately
stop all processes and climb all they way up to the main method and print the right message.


The main aspects of the parser:
-define each line in the file
-parse a line and check if it have sense.


Line and LineTypeFactory classes:

A text file is basically A lot of Line. after understanding the Sjava format we saw that there are only ten
valid types of lines. At first we thought to mput them all in an enum class.
That design did not allow us to save a lot of important information about tit and forced us to go over the
whole text file twice.
we solved the issue by implanting Line class which holds all the needed information.
every Line object has a LineTypeFactory object with a method that can interpret the line by it's type.
by that we gave each Line object a specific method for it's type.

Scope class:

Since the logic of the Format is based on Scopes,it was mandatory to make a class of Scope which
contains list of the scope's information (such as variables). this division allowed as to monitor very easily
the location of a variables and follow the roles of Sjava.

Method & Variables:
Method is a scope that have a lines inside of it which describe it's behaviour, since it's a scope it extends it
with more method such as RunMethod which tell's the program to use it.
Variables class represents a variable  and save all the information about it.

----
this structure allowed us to scan the file in a modular way. create a global scope which holds
all the method and the global variables, and after creating the global scope- get inside the method and
check it's logic

Facade -
eventually, the first design turned out to be a very procedural and not modular. to solve that we had to
divide it into the classes as mentioned below.
we have used the facade design to encapsulate all the classes into a single Static class which combines all the others.




=============================
=  Implementation details   =
=============================

This design, apart of being easy to understand, is also very easy to modify and add new type of variable-
all that is required is to a modify the parser to learn how it looks (regex, allowed in boolean exp or not).
after  this modification, the rest of the code won't have to be changed.

two Features to add-

Inheritance of files -
This modification will be quit easy to implement with the current design, by adding a file of "inheritedFiles"
to the global scope, which contains the global scope of other method, it will be easy to get method from the
other file and use them.
We might consider to make a class called "GlobalScope" so it will be easy to understand which Scopes
enter that array and to make the method array more easy to get

Different methods’ types-
by adding a "returnType" field to the method class we can check if the return statement af the method
contains the right variable type.

