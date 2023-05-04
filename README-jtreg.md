## Running JavaFX test using jtreg

### Introduction

This is a prelimiary exploration of what it would take to run the automated JavaFX unit tests using the jtreg test harness.

### Approach

jtreg includes support for running JUnit4 / JUnit5 tests without adding any specific jtreg tags to the test. This is well-suited for our current set of tests, so the plan is to take advantage of this. This is done by adding a `TEST.ROOT` file at the root of the test source tree, for example, `modules/javafx.base/src/test/java/TEST.ROOT`, with the following line in it:

```
JUnit.dirs = .
```
