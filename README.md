# silly-io

**Small Java helpers for files and streams: streams, readers and writers that treat a missing file
as the JDK does, file and filename filters, file names, sizes and extensions, object serialization,
class and annotation scanning, and the operating system it runs on. No runtime dependencies.**

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-io/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-io/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-io/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-io)
[![Mutation Score](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fastrapi69%2Fsilly-io%2Fgh-pages%2Fbadges%2Fmutation-score-develop.json)](https://pitest.org/)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/silly-io.svg?style=flat)](https://github.com/astrapi69/silly-io/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-io.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-io)
[![Javadoc](https://www.javadoc.io/badge/io.github.astrapi69/silly-io.svg)](https://www.javadoc.io/doc/io.github.astrapi69/silly-io)
[![MIT license](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/projects/jdk/25/)

</div>

silly-io collects the file and stream code that otherwise gets written again in every project, and
keeps it tested: every line and every branch is covered, and PIT checks that the tests notice when
the code changes.

## Why silly-io

It brings no runtime dependency: its jar is all a project adds, built for Java 25 and needing
nothing beyond the JDK. Every line and branch of it is tested, and a mutation score close to 100%
says that the tests check what the code does, not only that they run it. Copying, moving and
deleting files and directory trees are in [file-worker](https://github.com/astrapi69/file-worker),
which builds on silly-io. If you need much more, such as watching or tailing files or handling byte
order marks, or if you are on a JDK older than 25, Apache Commons IO is the better choice: it is
far larger, runs on Java 8 and is used almost everywhere.

## Table of contents

- [Why silly-io](#why-silly-io)
- [Requirements](#requirements)
- [Installation](#installation)
- [Upgrading from 4.x](#upgrading-from-4x)
- [Quick start](#quick-start)
- [Features](#features)
- [Quality](#quality)
- [Verifying the release signatures](#verifying-the-release-signatures)
- [Releasing](#releasing)
- [Contributing](#contributing)
- [How this project is made](#how-this-project-is-made)
- [License](#license)
- [Support this project](#support-this-project)
- [Credits](#credits)

## Requirements

**JDK 25 or newer**, at build and at runtime: the published jar holds Java 25 bytecode and does not
load on an older JVM. The library has no runtime dependencies; its jar is all it needs.

## Installation

Latest release on Maven Central: 5.0

[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-io.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-io)

**Gradle** (version catalog, `gradle/libs.versions.toml`):

```toml
[versions]
silly-io-version = "5.0"

[libraries]
silly-io = { module = "io.github.astrapi69:silly-io", version.ref = "silly-io-version" }
```

```groovy
dependencies {
    implementation libs.silly.io
}
```

**Gradle** (plain):

```groovy
dependencies {
    implementation "io.github.astrapi69:silly-io:5.0"
}
```

**Maven**:

```xml
<dependency>
    <groupId>io.github.astrapi69</groupId>
    <artifactId>silly-io</artifactId>
    <version>5.0</version>
</dependency>
```

## Upgrading from 4.x

5.0 has four breaking changes. The CHANGELOG's
[Migration from 4.x](CHANGELOG.md#migration-from-4x) gives for each what it was, what it is and what
to change:

- six utility classes of static methods have a private constructor (`AnnotationExtensions`,
  `ClassloaderExtensions`, `FindClassExtensions`, `ProcessBuilderFactory`, `ScanClassExtensions`,
  `ScanPackageExtensions`);
- `writeSerializedObjectToFile` returns nothing; a failure throws, as it did;
- the stream overloads treat a missing file as the JDK does: an input overload throws
  `FileNotFoundException`, an output overload creates the file;
- `OS.getOperatingSystem(String)` matches Linux and Windows in any case.

## Quick start

The examples are checked: each block runs, with the values its comments name as assertions, in
[`ReadmeExamplesTest`](src/test/java/io/github/astrapi69/io/ReadmeExamplesTest.java), and the
example types of `com.example.model` are in the test sources.

**Streams, readers and writers.** An input overload throws `FileNotFoundException` for a missing
file; an output overload creates it, and throws `FileNotFoundException` when its directory is
missing. The overloads with a `createFile` flag do what the flag says.

```java
File report = new File(directory, "report.txt");
try (Writer writer = StreamExtensions.getWriter(report)) // creates the file
{
    writer.write("hello");
}
byte[] content = StreamExtensions.getByteArray(report); // "hello", and closes the file

try (InputStream in = StreamExtensions.getInputStream(new File(directory, "missing.txt").toPath()))
{
    // ...
}
catch (FileNotFoundException missing)
{
    // the same exception for a File, a Path or a path name
}
```

**File and filename filters.**

```java
// directory holds Report-2026.txt, notes.xml and Main.java
File[] texts   = directory.listFiles(SuffixFileFilter.of(".txt", false));              // Report-2026.txt
File[] sources = directory.listFiles(MultiplyExtensionsFileFilter.of(".java", ".xml")); // Main.java, notes.xml
File[] reports = directory.listFiles(
    PrefixFileFilter.of("report", false, CaseSensitivity.INSENSITIVE));                // Report-2026.txt
String[] xml   = directory.list(SimpleFilenameFilter.of(".xml"));                      // notes.xml
```

The suffix and extension filters ignore case; `PrefixFileFilter` compares with case unless it gets
`CaseSensitivity.INSENSITIVE`. With `true` as the recursive flag a filter also accepts directories,
so that a walk can descend into them.

**File names.**

```java
File report = new File("Report-2026.txt");
FilenameExtensions.getFileExtension(report);                    // "txt"
FilenameExtensions.getFilenameWithoutExtension(report);         // "Report-2026"
FilenameExtensions.getFilenameWithNewExtension(report, ".bak"); // "Report-2026.bak"
```

**Serialization.**

```java
ArrayList<String> names = new ArrayList<>(List.of("Ada", "Grace"));
SerializedObjectExtensions.writeSerializedObjectToFile(names, file); // throws if it cannot
Object read = SerializedObjectExtensions.readSerializedObjectFromFile(file); // [Ada, Grace]
String base64 = Serializer.toBase64EncodedString("hi"); // "rO0ABXQAAmhp"
```

`Serializer` has the same methods, with unchecked exceptions.

**Classes and annotations.**

```java
// com.example.model holds the annotation Entity, the class Customer annotated with it, and Order
Set<Class<?>> classes  = ScanClassExtensions.scanTopLevelClasses("com.example.model");  // Customer, Entity, Order
Set<Class<?>> entities = AnnotationExtensions.getAllAnnotatedClasses("com.example.model",
    Entity.class);                                                                    // Customer
Entity entity = AnnotationExtensions.getAnnotation(Customer.class, Entity.class);
```

`getAnnotation` searches the class, its interfaces, the types of its annotations and its
superclasses, in that order, so it also finds an annotation that sits on an interface or a
superclass.

**The operating system.**

```java
OS os = OS.get();         // LINUX, MAC, UNIX, WINDOWS or OTHER
boolean linux = OS.isLinux();
```

## Features

| Package `io.github.astrapi69.io...` | What it holds |
|---|---|
| (root) | `StreamExtensions`: input and output streams, readers and writers for a `File`, a `Path` or a path name, byte arrays, copying between streams. `SerializedObjectExtensions` and `Serializer`: objects to files, byte arrays and Base64, and back; `Serializer` throws unchecked exceptions. `StringOutputStream` |
| `file` | `FilenameExtensions` (extensions, prefixes and suffixes, a name with a new extension, sanitized names), `FileExtension` and `FileCharacter` constants, `FileSize`, `CaseSensitivity` |
| `file.filter` | `FileFilter`s: `SuffixFileFilter`, `PrefixFileFilter`, `MultiplyExtensionsFileFilter`, `TxtFileFilter`, `ClassFileFilter` |
| `file.namefilter` | `FilenameFilter`s: `SimpleFilenameFilter`, `MultiplyExtensionsFilenameFilter` |
| `annotation` | `ScanClassExtensions` and `ScanPackageExtensions` (the classes of a package, from directories and jars), `AnnotationExtensions` (annotated classes, annotations through the hierarchy), `ImportResource`, `ImportResources` and `ImportResourcesExtensions`, `ClassloaderExtensions`, `FindClassExtensions` |
| `shell` | `OS`, `CommandLineExecutor`, `LinuxShellExecutor`, `ProcessBuilderFactory`, `TeeOutputStream` |

The [Javadoc](https://www.javadoc.io/doc/io.github.astrapi69/silly-io) has the details.

## Quality

As of 5.0, measured by its release gate (JDK 25, the tree of `RELEASE-5.0`); each release takes
the numbers of its own gate:

| | |
|---|---|
| tests | 560 in 34 test classes, none skipped |
| line and branch coverage (JaCoCo) | 946 of 946 lines, 344 of 344 branches; the build fails below 99% |
| mutation score (PIT) | 373 of 374 mutants killed; the build fails below 97%. The survivor, the call of `ByteArrayOutputStream.close`, which does nothing, is equivalent |

The CI build runs both checks on every pull request, and publishes the mutation score of `develop`
as the badge above.

## Verifying the release signatures

The releases on Maven Central are signed with the OpenPGP key

```text
5B2F A6B1 1E29 8BC0 9287  F423 D8C4 0351 8C49 CA75
```

Since 4.0 they are signed by its signing subkey

```text
6D67 F844 2A6F CC96 BD7C  1B5E 9FCF 7C97 10E2 BD8D
```

valid until 2028-10-09; up to 3.6 they were signed by the primary key itself. The key, with the
subkey, is on keys.openpgp.org. Fetch it once, then verify the signature next to each file:

```sh
gpg --keyserver hkps://keys.openpgp.org --recv-keys 5B2FA6B11E298BC09287F423D8C403518C49CA75
gpg --verify silly-io-5.0.jar.asc silly-io-5.0.jar
```

gpg answers `Good signature` and prints the `Primary key fingerprint`, and for a release signed by
the subkey the `Subkey fingerprint`; both have to be the ones above. The warning that the key "is not
certified with a trusted signature" only says that you have not marked it as trusted in your own
keyring.

## Releasing

What a release consists of, in the order it is run:

1. **One pull request** with the CHANGELOG entry and `projectVersion` without `-SNAPSHOT`
   (`docs: changelog for X.Y, and the version that carries it`), the full build green on it.
2. **The release gate** on that pull request's tree: the tests, `pitest` with its floor and
   `jacocoTestCoverageVerification` in one run, on JDK 25. Its numbers go into the CHANGELOG entry
   and into the README's Quality section, in the same pull request, before it is merged.
3. **Tag the merge commit** with `./gradlew tagRelease`, which names it `RELEASE-X.Y`, and push the
   tag. `publish.yml` uploads to the Central Portal, where the maintainer releases it. A pushed tag is
   never moved or deleted; a failed run is fixed on the branch and re-run.
4. **Verify on Central**: the jar, pom, `.module`, `-sources.jar` and `-javadoc.jar`, their sha1, and
   `gpg --verify` on the `.asc`.
5. **GitHub release** from the CHANGELOG entry.
6. **`master` follows the release** by a merge of the tag: on `master`,
   `git merge --no-ff "RELEASE-X.Y^{commit}" -m "Merge tag RELEASE-X.Y into master"`. Before
   `master` is pushed, `git diff RELEASE-X.Y master` has to be empty; if it is not, nothing is
   pushed. Never a force push: `master` is always the latest release, nothing else.
7. **Open the next cycle**: `projectVersion` to the next `-SNAPSHOT`.

## Contributing

Pull requests are welcome: fork [astrapi69/silly-io](https://github.com/astrapi69/silly-io/fork) and
[open a pull request](https://github.com/astrapi69/silly-io/pull/new/develop) against `develop`.
[CONTRIBUTING.md](.github/CONTRIBUTING.md) says how a stack of pull requests is merged.

Please add tests for your change; the coverage and mutation floors above hold for it too. The
`Makefile` runs the usual tasks (`make help` lists them):

```shell
make build      # clean build: tests, coverage check, formatting check
make spotless   # format, and write the license headers
make pitest     # mutation testing, fails below the floor
```

The versions follow the
[Simplified Semantic Versioning](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning)
guidelines, `<major>.<minor>.<patch>`. Questions, bug reports and feature requests belong on the
[issues page](https://github.com/astrapi69/silly-io/issues).

## How this project is made

silly-io was written by its maintainer over many years, before there were AI tools; release 5.0
was implemented mostly by AI agents, under the maintainer's review. What they did, and what the
maintainer decides: [AI_USAGE.md](AI_USAGE.md).

## License

MIT - see [LICENSE](LICENSE).

## Support this project

If silly-io is useful to you, a star on [GitHub](https://github.com/astrapi69/silly-io) helps others
find it. Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button,
which reads [.github/FUNDING.yml](.github/FUNDING.yml): GitHub Sponsors, Liberapay, Ko-fi and PayPal.

## Credits

| | |
|---|---|
| [Maven Central](https://central.sonatype.com/artifact/io.github.astrapi69/silly-io) | for hosting the artifacts of this open source project |
| [codecov.io](https://codecov.io) | for free code coverage reporting |
| [javadoc.io](https://www.javadoc.io) | for free javadoc hosting |
| [PIT](https://pitest.org/) | for mutation testing |

No animals were harmed in the making of this library.
