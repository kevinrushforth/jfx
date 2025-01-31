# Release Notes for JavaFX 24

## Introduction

The following notes describe important changes and information about this release. In some cases, the descriptions provide links to additional detailed information about an issue or a change.

These release notes cover the standalone JavaFX 24 release. JavaFX 24 requires JDK 22 or later. JDK 24 is recommended.

## Important Changes

### JavaFX 24 Requires JDK 22 or Later

JavaFX 24 is compiled with `--release 22` and thus requires JDK 22 or later in order to run. If you attempt to run with an older JDK, the Java launcher will exit with an error message indicating that the `javafx.base` module cannot be read.

See [JDK-8340003](https://bugs.openjdk.org/browse/JDK-8340003) for more information.

### JavaFX Applications Must Use `--enable-native-access`

Running a JavaFX application on JDK 24 will produce a warning from each of the three JavaFX modules that rely on native access, due to the changes specified in [JEP 472](https://openjdk.org/jeps/472). Each warning will include the following message:

```
WARNING: Restricted methods will be blocked in a future release unless native access is enabled
```

In order to suppress the warning now, and to be able to run your application at all in a subsequent version of the JDK, you need to explicitly enable native access for all modules that need it. This is done by passing `--enable-native-access=<list-of-modules>` to `java` on the command line, listing the modules that you grant native access. This list of modules includes `javafx.graphics` and, optionally, `javafx.media` and `javafx.web`, if your application uses those modules.

For example:

```
java --enable-native-access=javafx.graphics,javafx.media,javafx.web
```

See [JDK-8347744](https://bugs.openjdk.org/browse/JDK-8347744) for more information.

### The `jdk.jsobject` Module is Now Included with JavaFX

The `jdk.jsobject` module, which is used by JavaFX WebView applications, is now included with JavaFX, replacing the JDK module of the same name. The `jdk.jsobject` module is deprecated as of JDK 24, and will be removed in a future release of the JDK.

To facilitate the transition, `jdk.jsobject` is now an upgradable module in the JDK. This means that the version of `jdk.jsobject` delivered with JavaFX can be used in place of the one in the JDK to avoid the compiler warning. This can be done as follows:

#### Applications using the SDK

When running with the JavaFX SDK, use the `--upgrade-module-path` argument. For example:

```
javac --upgrade-module-path=/path/to/javafx-sdk-24/lib
java --upgrade-module-path=/path/to/javafx-sdk-24/lib
```

NOTE: The above will fail if you run your application with JDK 23 or earlier. JDK 24 is recommended when running JavaFX 24, but if you choose to run JavaFX 24 with an earlier JDK, use the `--module-path` option instead.

#### Applications using `jlink` to create a custom Java runtime image:

When creating your custom Java runtime image, put the JavaFX jmods on the module path ahead of the JDK jmoods. For example:

```
jlink --output jdk-with-javafx \
    --module-path /path/to/javafx-jmods-24:/path/to/jdk-24/jmods \
    --add-modules ALL-MODULE-PATH
```

NOTE: The above will fail if you create a custom image using JDK 23 or earlier. JDK 24 is recommended with JavaFX 24, but if you choose to run JavaFX 24 with an earlier JDK, put the JDK jmods ahead of the JavaFX jmods on the module path (that is, reverse the order of `javafx-jmods-24` and `jdk-24/jmods`).

See [JDK-8337280](https://bugs.openjdk.org/browse/JDK-8337280) for more information.

### Pluggable Image Loading via javax.imageio

JavaFX 24 supports the Java Image I/O API, allowing applications to use third-party image loaders in addition to the built-in image loaders. This includes the ability to use variable-density image loaders for formats like SVG. When an image is loaded using a variable-density image loader, JavaFX rasterizes the image with the screen's DPI scaling.

Applications that want to use this feature can use existing open-source Image I/O extension libraries, or register a custom Image I/O service provider instance with the IIORegistry class. Refer to the Java Image I/O documentation for more information.

See [JDK-8306707](https://bugs.openjdk.org/browse/JDK-8306707) for more information.

## Removed Features and Options

### JavaFX No Longer Supports Running With a Security Manager

The Java Security Manager has been permanently disabled in JDK 24 via [JEP 486](https://openjdk.org/jeps/486).

Likewise, as of JavaFX 24, it is no longer possible to run JavaFX applications with a security manager enabled. This is true even if you run your application on an older JDK that still supports the security manager.

The following exception will be thrown when the JavaFX runtime is initialized if the Security Manager is enabled:

```
UnsupportedOperationException: JavaFX does not support running with the Security Manager
```

See [JDK-8341090](https://bugs.openjdk.org/browse/JDK-8341090) for more information.

## Known Issues

### JavaFX Warning Printed for Use of Terminally Deprecated Methods in sun.misc.Unsafe

Running a JavaFX application on JDK 24 will produce a warning the first time any UI Control or complex shape is rendered:

```
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
...
WARNING: sun.misc.Unsafe::allocateMemory will be removed in a future release
```

To disable this warning, pass `--sun-misc-unsafe-memory-access=allow` to `java` on the command line. For example:

```
java --sun-misc-unsafe-memory-access=allow
```

This will be fixed in a subsequent version of JavaFX, after which time this flag will no longer be needed.

See [JDK-8345121](https://bugs.openjdk.org/browse/JDK-8345121) for more information.

## List of New Features

Issue Key | Summary | Subcomponent
--------- | ------- | ------------
[JDK-8888888](https://bugs.openjdk.org/browse/JDK-8888888) | TITLE | SUBCOMPONENT

## List of Other Enhancements

Issue Key | Summary | Subcomponent
--------- | ------- | ------------
[JDK-8888888](https://bugs.openjdk.org/browse/JDK-8888888) | TITLE | SUBCOMPONENT

## List of Fixed Bugs

Issue Key | Summary | Subcomponent
--------- | ------- | ------------
[JDK-8888888](https://bugs.openjdk.org/browse/JDK-8888888) | TITLE | SUBCOMPONENT

## List of Security fixes

Issue Key | Summary | Subcomponent
--------- | ------- | ------------
JDK-8888888 (not public) | TITLE | SUBCOMPONENT
