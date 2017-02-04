# fund-research

Software tools:
    - Java 8.
    - JUnit 4.0.

Packages:
    - enums - Enumerated indexes used to extract values from each row.
    - models - Data model beans used to store data.
    - util - Utility classes used to perform common operations.
    - analyser - Analyser classes to perform analysis on fund and benchmark data.

'testData' directory: We should place 4 CSV files into this directory while testing. (for testing purpose only).

This application has built on Java 8 APIs such as, lambda expressions, streams, Files, Paths.
Please execute on Java 8 Runtime Environment.

Steps to execute:
    - Download/clone project.
    - Build with mvn clean install
    - Run FunAnalyserTest.java
    - User need to create an object of FundAnalyser by passing 4 files path to constructor.
    - Then invoke generateMonthlyOutPerformance.