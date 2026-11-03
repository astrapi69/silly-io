# Changelog

---

## Version 5.0-SNAPSHOT

### Breaking
- `AnnotationExtensions`, `ClassloaderExtensions`, `FindClassExtensions`, `ProcessBuilderFactory`, `ScanClassExtensions` and `ScanPackageExtensions` have a private constructor. Every member of them is static, and their implicit public constructor was the last line the tests did not reach. It is binary incompatible: code compiled against 4.0 that calls one of these constructors fails with an `IllegalAccessError`, and a subclass of `ClassloaderExtensions`, `FindClassExtensions` or `ProcessBuilderFactory` no longer compiles. No project of the family does either. So this release is 5.0 instead of 4.1 (#59).
- The overloads of `StreamExtensions` treat a missing file as the JDK's streams do: an input overload throws `FileNotFoundException`, an output overload creates the file and throws `FileNotFoundException` when its parent directory is missing. Changed are `getInputStream(String)` and `getInputStream(Path)`, which threw `NoSuchFileException` for a missing file; `getOutputStream(File)` and `getWriter(File)`, which threw `FileNotFoundException` for a missing file instead of creating it; and `getOutputStream(String)` and `getOutputStream(Path)`, which threw `NoSuchFileException` for a missing parent directory. Where a `FileNotFoundException` replaces a `NoSuchFileException`, it carries it as its cause. Code that catches `NoSuchFileException` from these overloads no longer gets it; code that relied on `getOutputStream(File)` or `getWriter(File)` refusing a missing file now gets a new file. The overloads with a `createFile` flag are unchanged. Of the family, gradle-migration-data calls `getOutputStream(File)` (#56).
- `OS.getOperatingSystem(String)` matches Linux and Windows in any case, as it matched Mac OS and the Unix systems: `LINUX` and `WINDOWS 11` give `LINUX` and `WINDOWS`, where they gave `OTHER`; `|inux` and `|indows` give `OTHER`, where the character classes `[L|l]` and `[W|w]` let the `|` through. The JDKs report `Linux`, `Windows 11` and `Mac OS X`, so `OS.get()`, `isLinux()` and the other queries of the running system give what they gave; only a caller that passes its own name can see a difference. No project of the family does (#57).

### Added
- `PrefixFileFilter` can compare the prefix without its case: `new PrefixFileFilter(prefix, recursive, CaseSensitivity.INSENSITIVE)` or `PrefixFileFilter.of(prefix, recursive, CaseSensitivity.INSENSITIVE)`. The new enum `io.github.astrapi69.io.file.CaseSensitivity`, after Apache Commons IO's `IOCase`, has `SENSITIVE` and `INSENSITIVE`; without it the filter compares with case, as before, unlike the suffix filters, which always ignore it. `INSENSITIVE` lower-cases with `Locale.ROOT`, as the suffix filters do since #41 (#58).

### Changed
- Mutation testing with PIT 1.30.1 (`./gradlew pitest`, `gradle/mutation-testing.gradle`): the tests kill 371 of the 374 mutants, 99%, and the CI build fails below 97% (`mutationThreshold` in `build.gradle`; PIT rounds the score half up to a whole percent before it compares). The three survivors are equivalent: the `writeSerializedObjectToFile` methods return `true` or throw (#55), and `ByteArrayOutputStream.close` does nothing. The README's mutation score badge shows the score of `develop`, which CI publishes on the `gh-pages` branch from each push to it, also when the score fails the floor; a push to `master` publishes master's (#54).
- The tests that PIT found weak check the `toString` of the filters, a file of another extension in `FileExtension.is`, the line separators `CommandLineExecutor` mirrors to the additional output stream, and that the scans give an empty set the caller can add to for a directory that does not exist. The parameterized filter tests build their filter in the test, so that every `of` factory counts as tested by the invocation that uses it, and the parameterized tests gained the negative, boundary and empty inputs they lacked (#54).
- The tests write their files into a temporary directory, no longer into the working directory, where a run of PIT left them behind and the next build failed on them (#54).
- The test runtime class path no longer holds this project's own jar: file-worker and jobj-copy depend on an older silly-io, which Gradle resolved to this project, beside the compiled classes that come first on the class path, so `test` and now `pitest` needed `mustRunAfter(jar)`. The workaround is gone with its cause (#54).
- Unreachable code is gone, without a change in behaviour: `FindClassExtensions.forName` checked `Class.forName` for `null`, which it never returns; `ScanClassExtensions.normalizeSimpleClassName` handled a missing `.class` it had just checked for; `SerializedObjectExtensions.writeSerializedObjectToFile` flushed and closed a stream its try-with-resources closes (#54).
- `ImportResourcesExtensions.join` is an implementation of this project (`Arrays.copyOf`, `System.arraycopy`), no longer the block taken word for word from Apache Commons Lang's `ArrayUtils.addAll`. Its behaviour is unchanged; the `ArrayStoreException` its Javadoc named never came out of it, as any one became an `IllegalArgumentException`, and the Javadoc says that now (#59).
- `AnnotationExtensions.getAnnotation` and the private check of `ScanClassExtensions.normalizeSimpleClassName` for an anonymous class number are implementations of this project, no longer code taken from Spring's `AnnotationUtils.findAnnotation` and Apache Commons Lang's `StringUtils.isNumeric`. `getAnnotation` searches with a stack instead of recursion. Their behaviour is unchanged, pinned by characterization tests written against the code they replace: the search order of `getAnnotation`, its meta-annotations one level deep, its `null` handling; the digits `normalizeSimpleClassName` drops (#60).
- `ScanClassExtensions.normalizeSimpleClassName` no longer checks for `null` the part after the last `$` it passes to its private `isNumeric`: `String.substring` never returns `null` (#59).
- The coverage floor is 99% of the lines and 99% of the branches, up from 95% and 90%: the tests reach all 932 lines and all 346 branches (`jacocoTestCoverageVerification` in `gradle/testing.gradle`, #59).

### Fixed
- `StreamExtensions.getByteArray(File)` closes the stream it opens. It left it open, so every call held a file descriptor until the garbage collector got to the stream, and on Windows the file stayed locked as long. The overloads that take an `InputStream` still leave it open, as it belongs to the caller (#61).

---

## Version 4.0

### Breaking
- Java 25 is the minimum, up from 17: the family builds, tests and releases on JDK 25 (astrapi69/mystic-crypt#234), so this release is 4.0 instead of 3.7 (#14).
- `AnnotationExtensions.setAnnotationValue` is removed, without a deprecation first. Annotations are immutable by contract, and since JEP 403 (Java 17) the JDK internals it changed are strongly encapsulated: for every real annotation it threw `InaccessibleObjectException` unless the JVM ran with `--add-opens java.base/sun.reflect.annotation=ALL-UNNAMED`. No project of the family calls it (#29).
- `ScanPackageExtensions.scanClassNames` is renamed `scanTopLevelClassNames`, both overloads: it lists top-level classes only, which the old name did not tell, after Guava's `ClassPath.getTopLevelClasses`. Its behaviour is unchanged (#47).
- `ScanClassExtensions.scanClassNames` is renamed `scanTopLevelClasses`, both overloads: it loads top-level classes, not class names (#47).
- `ScanClassExtensions.scanClassesFromPackage` is renamed `scanAllClassesFromPackage`, both overloads: it loads all classes of the directory, nested and anonymous ones included, after Guava's `ClassPath.getAllClasses` (#47).
- `AnnotationExtensions.scanForAnnotatedClasses` is renamed `scanForAllAnnotatedClasses`: it finds all annotated classes of the directory, nested and anonymous ones included, as `getAllAnnotatedClasses` beside it does, whose name stays (#47).
- `AnnotationExtensions.scanForAnnotatedClassesFromSet` is renamed `scanForAllAnnotatedClassesFromSet`, for the same reason (#47).
- `AnnotationExtensions.scanForClasses` is renamed `scanForAllClasses`: it finds all classes of the directory, nested and anonymous ones included, as `getAllClasses` does (#47).

### Changed
- The tests cover 98.7% of the lines and 97.3% of the branches, up from 67.2% and 48.8%, and the build fails below 95% of the lines or 90% of the branches (`jacocoTestCoverageVerification` in `check`) (#20).
- Built with Gradle 9.8.2 (#37). The grgit and hierynomus license plugins are gone: a release is tagged through git (`gradle/tagging.gradle`), and Spotless writes the license header from `src/main/resources/license-header.txt` (#14).
- Releases go to Maven Central through the Central Portal, as in the rest of the family: the `com.gradleup.nmcp` plugin uploads, the maintainer releases each deployment in the Portal, and a `RELEASE-*` tag runs the new `publish.yml`, which signs with the signing subkey that the secret `GPG_KEY_ID` names. An upload leaves only from CI, a release only from its tag. The OSSRH staging endpoint this project published to is shut down; its settings are gone, among them the switch that held the checksums to md5 and sha1, and snapshots resolve from the Central Portal's snapshot repository (#13).
- A test class that leaves no result fails the build (`gradle/test-results-complete.gradle`, #10), and `gradle/` is under version control (#3). The Gradle dependency graph goes to GitHub (#1).
- Dependencies raised with versionCatalogUpdate, stable versions only: commons-io 2.22.0, junit-jupiter 6.1.3, silly-collection 28.2, and for the tests file-worker 19.1 and test-object 9.1 (#17).
- The tests resolve jackson 2.22.4 through a constraint to jobj-copy 4.3, no longer the release candidate 2.16.0-rc1 that jobj-contract-verifier 5.2 brought, and the unused `hamcrest-all` is gone (#34). The 15 Dependabot alerts on that test-only jackson are closed as false alarms for users (#36).
- The README names the family's funding channels instead of cryptocurrency addresses, and its dead badges and links are replaced (#5, #7).

### Security
- `LinuxShellExecutor.execute` runs the command in the execution directory through `ProcessBuilder.directory`, no longer through `cd <path>` in the command string, so a directory name can no longer run as a command (#22).

### Fixed
- `ScanPackageExtensions.scanTopLevelClassNames` lists the class files of the package only: no other jar entry (#23), no class of the parent package (#24), no sibling package that shares the prefix (#25), every root of the class path (#26), and top-level classes from a directory as from a jar (#33).
- A class path whose directory holds a `+` or another character a URL escapes is read correctly; `URLDecoder` turned `+` into a space (#27).
- `AnnotationExtensions.getAllClasses` and `getAllAnnotatedClasses` find the classes of a jar too (#28).
- `ScanPackageExtensions.scanTopLevelClassNames` and the `AnnotationExtensions` scans read a jar below a directory whose name holds a `!` (`/home/u/x!y/lib.jar`): the archive is split from the entry at the first `!/`, as `JarURLConnection` splits it, no longer at the first `!` (#43).
- `ScanPackageExtensions.scanTopLevelClassNames` no longer lists `package-info` and `module-info` as class names, from a directory as from a jar: neither class file holds a class a caller can load as a type (#45).
- `AnnotationExtensions.getAllClasses`, `getAllAnnotatedClasses`, `getAllAnnotatedClassesFromSet`, `scanForAllClasses`, `scanForAllAnnotatedClasses`, `scanForAllAnnotatedClassesFromSet` and `ScanClassExtensions.scanAllClassesFromPackage` no longer load `package-info` and `module-info` as classes, from a class directory, and the `getAll` scans from a jar too, as the class-name scan of #45 lists neither. With `@Deprecated` on a package, `getAllAnnotatedClasses(.., Deprecated.class)` returned `[interface <package>.package-info]`; the annotations of a package are read with `Package.getAnnotation` (#50).
- A recursive `ScanPackageExtensions.scanTopLevelClassNames` reads a jar once, for the package and every sub package below it, no longer once more for every package above a sub package (a tree four packages deep was read eight times), and it finds the classes of a sub package whose directory entry the jar lacks (#46).
- `FindClassExtensions.getDirectoriesFromResources` gives no entry for a package inside a jar, a war or an ear, instead of a `File` built from the archive URL that never exists, so a package that lies in archives only gives an empty list. The Javadoc says so (#48).
- Only a leading `~`, alone or before a `/`, stands for the home directory (#31).
- `OS.getOperatingSystem` returns `UNIX` for AIX, HP-UX, IRIX, Solaris, SunOS, FreeBSD, OpenBSD and NetBSD (#32).
- `CommandLineExecutor.execute` and `getCommandList` run `/bin/sh -c` for `UNIX`, the one shell POSIX guarantees, instead of `/bin/bash`, which a BSD keeps below `/usr/local/bin` or `/usr/pkg/bin` if at all; Linux and macOS keep `/bin/bash`. The Javadoc says that the command is a POSIX shell command on every system but Windows (#40).
- `SuffixFileFilter`, `MultiplyExtensionsFileFilter` and `MultiplyExtensionsFilenameFilter` ignore the case of the extension and of the name (#30).
- `SuffixFileFilter`, `MultiplyExtensionsFileFilter`, `MultiplyExtensionsFilenameFilter` and `SimpleFilenameFilter` lower-case with `Locale.ROOT` instead of the default locale: in the Turkish locale `.INI` lower-cased to `.ını`, with a dotless i, and matched no `config.ini` (#41).
- `FilenameExtensions.getFilenamePrefix(File)` and `getFilenameSuffix(File)` look for the extension's dot in the file name only, no longer in the whole path. Below a directory whose name holds a dot (`~/.cache/x/data`), a file without an extension got the rest of the path as its suffix and a prefix that ended at that directory, so a rename built from the prefix moved the file out of its directory (#9).

---

## Version 3.6

## [Unreleased]
### Added
- Introduced the `getCharacterFileReplacementMap` method in `FilenameExtensions` to provide a map of character replacements for sanitizing filenames. The map includes replacements for spaces, periods, slashes, colons, and other problematic characters.
- Added detailed Javadoc for the `getCharacterFileReplacementMap` method, including examples and usage scenarios.
- Created a JUnit test `testGetCharacterFileReplacementMap` to verify the correctness of the replacement map, ensuring expected character replacements and negative tests for undefined characters.
- new delegate method getName in FilenameExtensions

### Updated
- Upgraded `commons-io:commons-io` from `2.17.0` to `2.18.0`. See [Commons IO](https://commons.apache.org/proper/commons-io/) for details.

### Gradle
- Updated Gradle version from `8.11` to `8.11.1` (release candidate). For more information, see [Gradle releases](https://docs.gradle.org/current/release-notes.html).


---

## Version 3.5

### Added
- Support for new file extensions: `.ott`, `.fodt`, `.uot`, `.dotx`, and `.rtf`.
- Enhanced Javadoc for the `FilenameExtensionsTest` methods:
  - `testSanitizeFilename`: Added detailed documentation for test cases with examples.
  - `testSanitizeFilenameWithEmptyMap`: Added Javadoc for testing the method with an empty replacement map.
  - `testSanitizeFilenameWithCustomMap`: Included comprehensive Javadoc explaining custom replacement scenarios.

### Improved
- Improved readability and clarity of test method documentation with links to the `sanitizeFilename` method and specific examples.

### Changed
- Reorganized `FileExtension` enum constants in alphabetical order.
- Enhanced Javadoc to include descriptions for the newly added file extensions.

---

## Version 3.4

#### Added
- **File Extensions**:
  - Document formats: `DOC`, `DOCX`, `XLS`, `XLSX`, `PPT`, `PPTX`
  - Media files: `MP4`, `AVI`, `MKV`, `MOV`, `FLV`, `WMV`, `M4A`, `FLAC`
  - Image formats: `WEBP`, `HEIC`, `RAW`
  - Programming files: `HTML`, `CSS`, `TS`, `JAVA`, `KT`, `CPP`, `H`
  - Compressed and disk image files: `7Z`, `ISO`, `DMG`
  - Configuration files: `YAML`, `YML`, `ENV`, `CONFIG`, `INI`
- **Filename Extensions Methods**:
  - `getFilenameWithNewExtension(File, String)`: Creates a filename with a new string-based extension.
  - `getFilenameWithNewExtension(File, FileExtension)`: Creates a filename with a new enum-based `FileExtension`.

#### Changed
- Enhanced the `resolve` method in `FileExtension` to dynamically resolve file extensions by string input.

---

## Version 3.3

#### Added
- `CommandLineExecutor`: Execute shell commands across operating systems.
- `OS` Enum: Enables OS detection for appropriate shell paths and flags.
- Factory method for `ProcessBuilder` for simplified process configuration.

#### Changed
- Removed the deprecated `FileConstants` class.

---

## Version 3.2

#### Added
- Enum classes for `FileCharacter` and `FileSize` to handle file-specific characters and sizes.
- Package-level `package-info.java` files for all packages.
- `FileExtension` additions: `PDF` extension and method to retrieve extensions without a dot.

#### Changed
- Renamed the module to `io.github.astrapisixtynine.silly.io`.
- Updated Gradle to version `8.10.2`.
- Updated dependencies for better compatibility:
  - Spotless plugin: `7.0.0.BETA2`
  - Commons-IO: `2.17.0`
  - Various test dependencies.

---

## Version 3.1

#### Added
- `libs.versions.toml` for automatic catalog version updates.

#### Changed
- Updated Gradle to version `8.8`.
- Dependency updates:
  - Gradle plugins: Spotless, Grgit, and Versions plugin.
  - Test dependencies: `commons-io`, `jobj-core`, `silly-collection`.
- Refactored: Moved `LinuxShellExecutor#toString` to `StreamExtensions#toString`.

---

## Version 3

#### Added
- New method to write data from `Reader` to `Writer`.

#### Changed
- Updated JDK to version `17`.
- Updated Gradle to version `8.4`.
- Dependency updates:
  - Spotless plugin: `6.22.0`
  - Test dependencies: `commons-io`, `jobj-core`, `silly-collection`, `vintage-time`, and others.
- Renamed `annotations` package to `annotation`.

---

## Version 2.2

#### Added
- Method to retrieve known compression file extensions as a `Set`.
- Added `yml` and `yaml` file extensions to `FileExtension`.

#### Changed
- Updated Gradle to version `7.6`.
- Updated dependencies for plugins and test libraries.

---

## Version 2.1

#### Added
- New class `StringOutputStream`.

---

## Version 2

#### Added
- Constructor and factory methods for `SimpleFilenameFilter`.
- Factory methods for classes implementing `FileFilter`.

#### Changed
- Updated JDK to version `11`.
- Updated Gradle to version `7.5-rc-2`.

---

## Version 1.9

#### Added
- Methods in `SerializedObjectExtensions` and `Serializer` for Base64 conversion.

---

## Version 1.8

#### Added
- New file extensions in `FileExtension`.
- GitHub Actions workflow for Java CI with Gradle.
- Spotless plugin for code formatting.

---

## Version 1.7

#### Added
- Methods in `FilenameExtensions` for file extension handling.
- File extensions for ZIP files in `FileExtension`.

#### Changed
- Updated dependencies and Gradle to version `7.3.3`.

---

## Version 1.6

#### Added
- `SuffixFileFilter` for generic file filtering.

#### Changed
- Updated Gradle to version `7.2`.

---

## Version 1.5

#### Added
- Classes for shell command execution and annotations.

#### Changed
- Migrated from TestNG to JUnit5.
- Updated Gradle and dependencies.

---

## Version 1.4

#### Added
- `PrefixFileFilter` for recursive file searches.

#### Changed
- Updated dependencies and package structures.

---

## Version 1.3

#### Added
- `Serializer` class for runtime exception handling.

---

## Version 1.2

#### Added
- Method to retrieve an `InputStream` from a file path.

#### Changed
- Migrated from Maven to Gradle.
- Removed Lombok dependencies.

---

## Version 1.1

#### Changed
- Migrated file filter classes from the obsolete `jcommons-lang` project.

---

## Version 1

#### Added
- Initial project setup.
- Created changelog, contribution guidelines, and pull request templates.
