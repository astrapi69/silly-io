## Change log
----------------------

Version 3.3-SNAPSHOT
-------------


Version 3.2
-------------

ADDED:

- new enum class FileCharacter that holds file specific characters
- new enum class FileSize that holds file specific sizes
- new package-info.java files for all packages
- new file extension for pdf files in enum class FileExtension
- new method in enum class FileExtension for get only the extension without the dot

CHANGED:

- rename of module to new name 'io.github.astrapisixtynine.silly.io'
- update gradle to new version 8.10.2
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version 7.0.0.BETA2
- update of test dependency commons-io in new version 2.17.0
- update of test dependency jobj-core to new version 9
- tagged constant class FileConstants as deprecated
- removed obsolete package.html files
- update of test dependency file-worker to new version 17.4
- update of test dependency jobj-contract-verifier to new version 5.2
- update of test dependency silly-collection to new version 28
- update of test dependency junit-jupiter to new version 5.11.1

Version 3.1
-------------

ADDED:

- new libs.versions.toml file for new automatic catalog versions update

CHANGED:

- update gradle to new version 8.8
- update of gradle plugin dependency com.github.ben-manes.versions.gradle.plugin to new minor version 0.51.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.2
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version 7.0.0.BETA1
- update of test dependency commons-io in new version 2.16.1
- update of test dependency jobj-core to new version 8.2
- update of test dependency silly-collection to new version 27.1
- moved method LinuxShellExecutor#toString to StreamExtensions#toString

Version 3
-------------

ADDED:

- new method for writing from Reader to Writer

CHANGED:

- update of jdk to version 17
- update gradle to new version 8.4
- update of gradle plugin dependency com.github.ben-manes.versions.gradle.plugin to new minor version 0.49.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version 6.22.0
- update of test dependency commons-io in new version 2.14.0
- update of test dependency silly-collection to new version 27
- update of test dependency jobj-core to new version 8.1
- update of test dependency test-object in new version 8
- update of test dependency vintage-time in new version 6
- update of test dependency junit-jupiter-api and junit-jupiter-engine in new version 5.10.0
- rename of package annotations to annotation

Version 2.2
-------------

ADDED:

- new method that gets all known compression file extensions as a Set object
- new file extensions to enum FileExtension for yml and yaml files

CHANGED:

- update gradle to new version 7.6
- update of gradle plugin dependency com.github.ben-manes.versions.gradle.plugin to new minor version 0.45.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.14.0
- update of test dependency jobj-core to new version 7
- update of test dependency silly-collection to new version 20.4
- update of test dependency jobj-contract-verifier to new version 4
- update of test dependency 'com.github.meanbeanlib:meanbean' to new version 3.0.0-M9
- update of test dependency test-object in new minor version 7.2
- update of test dependency junit-jupiter-api to new version 5.9.2

Version 2.1
-------------

ADDED:

- new class StringOutputStream

Version 2
-------------

ADDED:

- new constructor in class SimpleFilenameFilter with only the file extension
- new factory methods in class SimpleFilenameFilter for both constructors created
- new factory methods for all classes that implements the FileFilter interface

CHANGED:

- update of jdk to version 11
- update of gradle to new version 7.5-rc-2
- update of gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.7.2

Version 1.9
-------------

ADDED:

- new method in class SerializedObjectExtensions for convert an object to a base64 encoded String object
- new method in class Serializer for convert an object to a base64 encoded String object
- new method in class SerializedObjectExtensions for convert a base64 encoded String object back to an object
- new method in class Serializer for convert a base64 encoded String object back to an object

Version 1.8
-------------

ADDED:

- new file extensions for several files in enum FileExtension
- new github-action workflow for 'Java CI with Gradle'
- new gradle plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' to minor version 6.3.0
- new method that checks if a given file extension is known from a file object

CHANGED:

- update gradle to new version 7.4
- update of gradle plugin dependency com.github.ben-manes.versions.gradle.plugin to new minor version 0.42.0
- update of test dependency test-objects in new major version 6
- update of test dependency jobj-core in new version 5.3

Version 1.7
-------------

ADDED:

- new method for get the file extension in class FilenameExtensions
- new file extensions for zip files in enum FileExtension
- new methods for check if a file is of given file extension

CHANGED:

- update gradle to new version 7.3.3
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.41.0
- update of test dependency commons-io in new version 2.11.0
- update of test dependency silly-collections in new version 18
- update of test dependency jobj-contract-verifier in new version 3.5
- update of test dependency jobj-core in new version 5.2
- update of test dependency test-objects in new version 5.7
- update of test dependency vintage-time in new version 5.4
- update of test dependency junit-jupiter-api and junit-jupiter-engine in new version 5.8.2

Version 1.6
-------------

ADDED:

- new class SuffixFileFilter for a generic FileFilter implementation
- new file extension '.mcrdb' in enum FileExtension

CHANGED:

- update gradle to new version 7.2

Version 1.5
-------------

ADDED:

- new comparator for the annotation ImportResource
- new extension class for the annotation ImportResource and ImportResources
- new extension class for execute commands on shell
- new package io.github.astrapi69.io.annotation created and moved from obsolet project 'de.alpharogroup:jcommons-lang' all related classes to it
- new package io.github.astrapi69.io.shell created and moved from obsolet project 'de.alpharogroup:jcommons-lang' all related classes to it

CHANGED:

- changed unit test framework from testng to junit5
- update gradle to new version 7.1
- update gradle-plugin dependency of gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin to new version 0.16.1
- update of test dependency silly-collections in new version 8.7
- update of test dependency jobj-core in new version 3.8
- update of test dependency test-objects in new version 5.5

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
