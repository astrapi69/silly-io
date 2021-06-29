## Change log
----------------------

Version 1.5-SNAPSHOT
-------------

ADDED:

- new comparator for the annotation ImportResource
  
CHANGED:

- update of gradle to new version 6.9
- changed unit test framework from testng to junit5

Version 1.4
-------------

ADDED:

- new class PrefixFileFilter created that can find files with a given prefix recursive

CHANGED:

- update of gradle to new version 6.8
- to new package io.github.astrapi69
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.36.0
- update of test dependency silly-collections in new version 8.3
- all FileFilter and FilenameFilter classes have a recursive flag for recursive search

Version 1.3
-------------

ADDED:
 
- new class Serializer that reflects the SerializedObjectExtensions and throws only decorated RuntimeExceptions

CHANGED:

- update of gradle to new version 6.7
- update of test dependency jobj-core in new version 3.6
- update of test dependency silly-collections in new version 8.3
- update of test dependency commons-io in new version 2.8.0
- update of test dependency testng to new version 7.3.0

Version 1.2
-------------

ADDED:
 
- new method for get an InputStream from a file path 
- new build system gradle

CHANGED:

- removed maven build system and all related files
- removed all lombok dependent imports

Version 1.1
-------------

CHANGED:

-  moved file filter classes from obsolet jcommons-lang project to this project

Version 1
-------------

ADDED:
 
- this changelog file
- created PULL_REQUEST_TEMPLATE.md file
- created CODE_OF_CONDUCT.md file
- created CONTRIBUTING.md file
- provide package.html for the javadoc of packages
- moved classes from obsolet jcommons-lang project to this project


