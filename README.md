# Celcoin Integration Java

## How the packages were created
* The package is created using Apache CXF 
- https://mvnrepository.com/artifact/org.apache.cxf/cxf-codegen-plugin 
- http://cxf.apache.org/docs/using-cxf-with-maven.html

## What are the requirements
 - Java JDK 1.8 installed
 - IntelliJ
 - Apache CXF 

## How to install
- Tutorial to install JDK: https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html
- Tutorial to install Apache CXFs: http://cxf.apache.org/download.html / 
- Tutorial to install IntelliJ: https://www.jetbrains.com/idea/download/#section=windows


## Build Instructions in a terminal (CMD Windows)
- open the project folder 
- type `mvn generate-sources` and press enter 

the codes will be generated in ```target\cxf-codegen-plugin-makers\cxf```

## How to test
- open the file: `myartifact\test\java\mygroup\AppTest.java` in IntelliJ
- click in the icon `play` in the left side to `` public void testTopupRecharge(){``
- Look the answer and the tests result.
