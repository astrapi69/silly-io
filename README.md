# Overview

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-io/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-io/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-io/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-io)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/silly-io.svg?style=flat)](https://github.com/astrapi69/silly-io/issues)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-io/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-io)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-io.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-io)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)
[![Hits Of Code](https://hitsofcode.com/github/astrapi69/silly-io?branch=develop)](https://hitsofcode.com/github/astrapi69/silly-io/view?branch=develop)
[![Lines Of Code](https://tokei.rs/b1/github/astrapi69/silly-io)](https://github.com/astrapi69/silly-io)

</div>

Utility library that provides java classes for create In- and OuputStream and Reader or Writer for file operations

> Please support this project by simply putting a Github <!-- Place this tag where you want the button to render. -->
<a class="github-button" href="https://github.com/astrapi69/silly-io" data-icon="octicon-star" aria-label="Star astrapi69/silly-io on GitHub">Star ⭐</a>
>
> Share this library with friends on Twitter and everywhere else you can
>
> If you love this project [![donation](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

## gradle dependency

Replace the variable ${latestVersion} with the current latest version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-io/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-io)

You can first define the version in the ext section and add than the following gradle dependency
to your project `build.gradle` if you want to import the core functionality of silly-io:

define version in file gradle.properties

```
sillyIoVersion=${latestVersion}
```

or in build.gradle ext area

```
    sillyIoVersion = "${latestVersion}"
```

and then add the dependency to the dependencies area

```
    implementation("io.github.astrapi69:silly-io:$sillyIoVersion")
```

## Maven dependency

Maven dependency is now on sonatype.
Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~silly-io~~~) for latest snapshots and releases.

Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of silly-io:

Than you can add the dependency to your dependencies:

    <properties>
            ...
        <!-- SILLY-IO version -->
        <silly-io.version>${latestVersion}</silly-io.version>
            ...
    </properties>
            ...
        <dependencies>
            ...
            <!-- SILLY-IO DEPENDENCY -->
            <dependency>
                <groupId>io.github.astrapi69</groupId>
                <artifactId>silly-io</artifactId>
                <version>${silly-io.version}</version>
            </dependency>
            ...
        </dependencies>

## Support this project

If silly-io is useful to you, a star on [GitHub](https://github.com/astrapi69/silly-io) helps others find it.
Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button, which
reads [.github/FUNDING.yml](.github/FUNDING.yml): GitHub Sponsors, Liberapay, Ko-fi and PayPal.

## Note

No animals were harmed in the making of this library.

## License

The source code comes under the liberal MIT License, making silly-io great for all types of applications.

## Semantic Versioning

The versions of silly-io are maintained with the Simplified Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning for this project you can visit this
[wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning).

## Want to Help and improve it? ###

The source code for silly-io are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [astrapi69/silly-io/fork](https://github.com/astrapi69/silly-io/fork)

To share your changes, [submit a pull request](https://github.com/astrapi69/silly-io/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the silly-io developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/astrapi69/silly-io/issues).

## Credits

|**Nexus Sonatype repositories**|
|     :---:      |
|[![sonatype repository](https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.astrapi69/silly-io.svg?style=for-the-badge)](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~silly-io~~~)|
|Special thanks to [sonatype repository](https://www.sonatype.com) for providing a free maven repository service for open source projects|
|     <img width=1000/>     |

|**codecov.io**|
|     :---:      |
|[![Coverage Status](https://codecov.io/gh/astrapi69/silly-io/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-io)|
|Special thanks to [codecov.io](https://codecov.io) for providing a free code coverage for open source projects|
|     <img width=1000/>     |

|**javadoc.io**|
|     :---:      |
|[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-io.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-io)|
|Special thanks to [javadoc.io](http://www.javadoc.io) for providing a free javadoc documentation for open source projects|
|     <img width=1000/>     |
