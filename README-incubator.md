# JavaFX Incubator Modules

## Overview

This document includes instructions for adding a JavaFX incubator module to the JavaFX build.

## Adding an Incubator module

To add an incubator module, do the following:

1. Add an entry for your module in `settings-incubator.gradle` and `build-incubator.gradle` in the designated place
2. Create your module under `modules/jfx.incubator.YOURMODULENAME`, including your source code and test code as is done for other modules
3. Add the needed build logic in `modules/jfx.incubator.YOURMODULENAME/project.gradle`

Here is an example:

KCR: include diffs here
