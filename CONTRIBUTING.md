Contributing to OpenJFX 11 Updates
==================================

The OpenJFX 11 Updates repository is _not_ accepting new contributions. If you are looking to contribute to OpenJFX, please visit [openjdk/jfx](https://github.com/openjdk/jfx) instead.

Important bug fixes that have already been fixed in the mainline can be backported with approval. New fixes should first go into the mainline, and then can be considered for backporting. Approval from one of the project leads is required prior to integrating the fix.

To backport a fix, you need to import the patch from mainline into a branch of your personal fork of this repo. Then, you can create the pull request. We strongly recommend creating a backport-style pull request, by creating the PR with the title:

```
Backport LONG-COMMIT-HASH
```

where `LONG-COMMIT-HASH` is the long (40 char) commit hash of the fix as found in the main jfx repo. The Skara tooling will then note that it is a backport, and replace the title with the correct issue title.
