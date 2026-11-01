# Changelog

---

## Version 4.0

### Breaking
- Java 25 is the minimum, up from 17: the family builds, tests and releases on JDK 25 (astrapi69/mystic-crypt#234), so this release is 4.0 instead of 3.7 (#14).

### Changed
- The tests cover 98.7% of the lines and 97.3% of the branches, up from 67.2% and 48.8%, and the build fails below 95% of the lines or 90% of the branches (`jacocoTestCoverageVerification` in `check`) (#20).
- Built with Gradle 9.8.2. The grgit and hierynomus license plugins are gone: a release is tagged through git (`gradle/tagging.gradle`), and Spotless writes the license header from `src/main/resources/license-header.txt` (#14).
- Releases go to Maven Central through the Central Portal, as in the rest of the family: the `com.gradleup.nmcp` plugin uploads, the maintainer releases each deployment in the Portal, and a `RELEASE-*` tag runs the new `publish.yml`, which signs with the signing subkey that the secret `GPG_KEY_ID` names. An upload leaves only from CI, a release only from its tag. The OSSRH staging endpoint this project published to is shut down; its settings are gone, among them the switch that held the checksums to md5 and sha1, and snapshots resolve from the Central Portal's snapshot repository (#13).
- A test class that leaves no result fails the build (`gradle/test-results-complete.gradle`, #10), and `gradle/` is under version control (#3). The Gradle dependency graph goes to GitHub (#1).
- Dependencies raised with versionCatalogUpdate, stable versions only: commons-io 2.22.0, junit-jupiter 6.1.3, silly-collection 28.2, and for the tests file-worker 19.1 and test-object 9.1 (#17).
- The README names the family's funding channels instead of cryptocurrency addresses, and its dead badges and links are replaced (#5, #7).

### Fixed
- `LinuxShellExecutor.execute` runs the command in the execution directory through `ProcessBuilder.directory`, no longer through `cd <path>` in the command string, so a directory name can no longer run as a command (#22).
- Only a leading `~`, alone or before a `/`, stands for the home directory (#31).
- `OS.getOperatingSystem` returns `UNIX` for AIX, HP-UX, IRIX, Solaris, SunOS, FreeBSD, OpenBSD and NetBSD (#32).
- `SuffixFileFilter`, `MultiplyExtensionsFileFilter` and `MultiplyExtensionsFilenameFilter` ignore the case of the extension and of the name (#30).
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
