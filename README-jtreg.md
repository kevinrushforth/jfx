## Running JavaFX tests using jtreg

### Introduction

This is a prelimiary exploration of what it would take to run the automated JavaFX unit tests using the [jtreg](https://openjdk.org/jtreg/) test harness. The goal is to do this in a way that preserves the ability to run the tests with gradle and doesn't require wholesale modification to individual test classes.

### Approach

jtreg includes support for running JUnit4 / JUnit5 tests without adding any specific jtreg tags to the test. This is well-suited for our current set of tests, so the plan is to take advantage of this. This is done by adding a `TEST.ROOT` file at the root of each test source tree, for example, [modules/javafx.base/src/test/java/TEST.ROOT](modules/javafx.base/src/test/java/TEST.ROOT), with the following line in it:

```
JUnit.dirs = .
```

### Current prototye

An initial proof-of-concept was done by building the JavaFX SDK + shims, and then running jtreg with the JavaFX shims on the classpath (this will need to change to use the module-path). I ran the `javafx.base` tests as follows:

```
jtreg \
    -cpa:build/shims/javafx.base:build/shims/javafx.controls:build/shims/javafx.fxml:build/shims/javafx.graphics:build/sdk/lib/javafx.media.jar:build/shims/javafx.swing:build/shims/javafx.web \
    -w:$ROOTDIR/build/test.workdir \
    -r:$ROOTDIR/build/test.report \
    -conc:1 \
    -agentvm \
    -v:default \
    modules/javafx.base/src/test/java/test
```

The [run-jtreg.sh](run-jtreg.sh) script can be used.

It then compiled and ran the tests with the following results:

|Description|Count|
|----|----|
|Tests that passed|239|
|Tests that failed|8|
|Total|247|

Two of the failures are unsurprising: one is a file with only comments in it (the only reason the gradle test runner doesn't fail is that it manages to ignore it), while the other is failing because it is testing for javafx.base being in a module, which was a deliberate short cut for this initial test. That leaves 6 failures that need to be analyzed.

The `javafx.graphics` tests do not run because they need a test utility class from `javafx.base`, and that isn't yet included.

# Future work

Some areas of future work include:

* Running the test harness in the modules
* Making test utilities in javafx.base and javafx.graphics available to tests in other modules
* Handling the test cases that need additional modular applications to be built (we had to write a bit of custom gradle code to do that in gradle, and will need to somehow replicate that here)
* Problem listing
* Test definition files (e.g., for running all headless tests, all headful tests, etc)
