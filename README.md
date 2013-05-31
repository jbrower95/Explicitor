Explicitor by Justin Brower
==========

Explicitor allows a user to scan through the lyrics of his entire iTunes library (without having lyrics within the mp3 files) and search for profanity or other words. This is useful in the event that a DJ or Music Provider should need a means of searching for profanity-free songs. Explicitor provides many features for searching and organizing the data it produces, including a save and load feature. See "User Guide.pdf", or the GitHub wiki for more details.


Explicitor makes use of the Apache Lang library for some of its HTML parsing code. 
The Apache lang library can be found at -- 

http://commons.apache.org/proper/commons-lang/

When executing Explicitor, it is important that the jar for the Apache Commons Language library be included in the classpath

for example:

javac -cp .:commons-lang3-3.1.jar Frame.java

java -cp .:commons-lang3-3.1.jar EmptyFrame1


