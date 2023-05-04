#! /bin/bash

# This assumes that jtreg 7.2 is in your PATH
#
# Usage:
#    bash run-jtreg.sh modules/javafx.base/src/test/java/test

ROOTDIR=$(dirname $0)

set -x

# NOTE: This is a hack that runs the JavaFX modules on the classpath.
jtreg \
    -cpa:$ROOTDIR/build/shims/javafx.base:$ROOTDIR/build/shims/javafx.controls:$ROOTDIR/build/shims/javafx.fxml:$ROOTDIR/build/shims/javafx.graphics:$ROOTDIR/build/sdk/lib/javafx.media.jar:$ROOTDIR/build/shims/javafx.swing:$ROOTDIR/build/shims/javafx.web \
    -w:$ROOTDIR/build/test.workdir \
    -r:$ROOTDIR/build/test.report \
    -conc:1 \
    -agentvm \
    -v:default \
    $*

# use AgentVM mode: -agentvm
# force OtherVM mode:    -othervm
