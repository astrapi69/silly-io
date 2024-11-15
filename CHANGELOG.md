# Changelog

---

## Version 3.5-SNAPSHOT

### [Unreleased]

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
