/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.io.annotation;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.io.annotation.ClassFixtures.RootKind;
import io.github.astrapi69.io.annotation.ClassFixtures.UrlStyle;

/**
 * {@link ScanPackageExtensions} lists the names of the classes of a package, from a directory of
 * the class path or from an archive. The directory cases scan the fixtures in {@code testdata.scan}
 * on the test class path; the archive cases scan a jar built in a temporary directory, reached
 * through a context class loader of the test's own
 */
class ScanPackageExtensionsTest
{

	static final String SCAN_PACKAGE = "io.github.astrapi69.io.annotation.testdata.scan";

	static final String JAR_PACKAGE = "generated.scan";

	static final String DEFECT_PACKAGE = "gen.pkg";

	@TempDir
	Path directory;

	static Stream<Arguments> directoryScans()
	{
		return Stream.of(
			arguments(false, true,
				Set.of(SCAN_PACKAGE + ".ScannedPage", SCAN_PACKAGE + ".ScannedPanel",
					SCAN_PACKAGE + ".ScannedPlain")),
			arguments(true, true,
				Set.of(SCAN_PACKAGE + ".ScannedPage", SCAN_PACKAGE + ".ScannedPanel",
					SCAN_PACKAGE + ".ScannedPlain", SCAN_PACKAGE + ".sub.ScannedSubPage")),
			arguments(false, false, Set.of("ScannedPage", "ScannedPanel", "ScannedPlain")),
			arguments(true, false,
				Set.of("ScannedPage", "ScannedPanel", "ScannedPlain", "ScannedSubPage")));
	}

	static Stream<Arguments> jarScans()
	{
		return Stream.of(
			arguments(false, true, Set.of(JAR_PACKAGE + ".First", JAR_PACKAGE + ".Second")),
			arguments(true, true,
				Set.of(JAR_PACKAGE + ".First", JAR_PACKAGE + ".Second", JAR_PACKAGE + ".sub.Deep")),
			arguments(false, false, Set.of("First", "Second")),
			arguments(true, false, Set.of("First", "Second", "Deep")));
	}

	/**
	 * Builds the jar that the archive cases scan: two classes and a nested class in the package, a
	 * class in a sub package and one in an unrelated package
	 *
	 * @param directory
	 *            the directory to write the jar into
	 * @return the jar file
	 * @throws IOException
	 *             if the jar cannot be written
	 */
	static Path newScanJar(final Path directory) throws IOException
	{
		return ClassFixtures.newJar(directory.resolve("scan.jar"), "generated/", "generated/scan/",
			"generated/scan/First.class", "generated/scan/Second.class",
			"generated/scan/Second$Inner.class", "generated/scan/sub/",
			"generated/scan/sub/Deep.class", "other/", "other/Elsewhere.class");
	}

	/**
	 * Scans the given package in a jar of the given entries, written into a file of its own and
	 * reached through a context class loader of the test's own
	 *
	 * @param packageName
	 *            the package to scan
	 * @param recursive
	 *            the recursive flag of the scan
	 * @param qualifiedClassnames
	 *            the flag of the scan for qualified class names
	 * @param entryNames
	 *            the entries of the jar, as {@link ClassFixtures#newJar} reads them
	 * @return the class names the scan lists
	 * @throws Exception
	 *             if the jar cannot be written or the scan fails
	 */
	private Set<String> scanJar(final String packageName, final boolean recursive,
		final boolean qualifiedClassnames, final String... entryNames) throws Exception
	{
		final Path jar = ClassFixtures.newJar(Files.createTempFile(directory, "scan", ".jar"),
			entryNames);
		return scanRoots(packageName, recursive, qualifiedClassnames, jar);
	}

	/**
	 * Scans the given package through a context class loader of the test's own that reads the given
	 * class path roots, in the given order
	 *
	 * @param packageName
	 *            the package to scan
	 * @param recursive
	 *            the recursive flag of the scan
	 * @param qualifiedClassnames
	 *            the flag of the scan for qualified class names
	 * @param roots
	 *            the class directories and jars of the class path
	 * @return the class names the scan lists
	 * @throws Exception
	 *             if the scan fails
	 */
	static Set<String> scanRoots(final String packageName, final boolean recursive,
		final boolean qualifiedClassnames, final Path... roots) throws Exception
	{
		final URL[] rootUrls = new URL[roots.length];
		for (int index = 0; index < roots.length; index++)
		{
			rootUrls[index] = UrlStyle.ESCAPED.of(roots[index]);
		}
		return ClassFixtures.withClassPathRoots(
			() -> ScanPackageExtensions.scanClassNames(packageName, recursive, qualifiedClassnames),
			rootUrls);
	}

	static Stream<Arguments> urlEscapedRoots()
	{
		return ClassFixtures.urlEscapedDirectories().flatMap(
			escapedDirectory -> Stream.of(RootKind.values()).map(rootKind -> arguments(rootKind,
				escapedDirectory.get()[0], escapedDirectory.get()[1])));
	}

	@ParameterizedTest(name = "a {0} below \"{1}\", {2} URL")
	@MethodSource("urlEscapedRoots")
	void scanClassNames_readsARootWhosePathHoldsACharacterThatAUrlEscapes(final RootKind rootKind,
		final String directoryName, final UrlStyle urlStyle) throws Exception
	{
		final Path root = rootKind.write(directory.resolve(directoryName).resolve("root"),
			"plus.pkg.Plus");

		assertEquals(Set.of("plus.pkg.Plus"), ClassFixtures.withClassPathRoots(
			() -> ScanPackageExtensions.scanClassNames("plus.pkg"), urlStyle.of(root)));
	}

	@ParameterizedTest(name = "recursive {0}, qualified names {1}")
	@MethodSource("directoryScans")
	void scanClassNames_listsTheClassesOfADirectoryPackage(final boolean recursive,
		final boolean qualifiedClassnames, final Set<String> expected) throws IOException
	{
		assertEquals(expected,
			ScanPackageExtensions.scanClassNames(SCAN_PACKAGE, recursive, qualifiedClassnames));
	}

	@Test
	void scanClassNames_listsTheQualifiedNamesOfThePackageOnly_byDefault() throws IOException
	{
		assertEquals(
			Set.of(SCAN_PACKAGE + ".ScannedPage", SCAN_PACKAGE + ".ScannedPanel",
				SCAN_PACKAGE + ".ScannedPlain"),
			ScanPackageExtensions.scanClassNames(SCAN_PACKAGE));
	}

	@Test
	void scanClassNames_isEmpty_forAPackageThatIsNotOnTheClassPath() throws IOException
	{
		assertTrue(
			ScanPackageExtensions.scanClassNames("io.github.astrapi69.no.such.pkg").isEmpty());
	}

	@ParameterizedTest(name = "recursive {0}, qualified names {1}")
	@MethodSource("jarScans")
	void scanClassNames_listsTheTopLevelClassesOfAJarPackage(final boolean recursive,
		final boolean qualifiedClassnames, final Set<String> expected) throws Exception
	{
		final Path jar = newScanJar(directory);
		try (URLClassLoader jarClassLoader = new URLClassLoader(new URL[] { jar.toUri().toURL() },
			ClassLoader.getSystemClassLoader()))
		{
			assertEquals(expected,
				ClassFixtures.withContextClassLoader(jarClassLoader, () -> ScanPackageExtensions
					.scanClassNames(JAR_PACKAGE, recursive, qualifiedClassnames)));
		}
	}

	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "gen/pkg/readme.txt", "gen/pkg/messages.properties", "gen/pkg/LICENSE",
			"gen/pkg/Old.class.bak" })
	void scanClassNames_listsOnlyTheClassFilesOfAJarPackage(final String otherEntry)
		throws Exception
	{
		final String[] entryNames = { "gen/", "gen/pkg/", "gen/pkg/A.class", otherEntry };

		assertEquals(Set.of(DEFECT_PACKAGE + ".A"),
			scanJar(DEFECT_PACKAGE, false, true, entryNames));
		assertEquals(Set.of("A"), scanJar(DEFECT_PACKAGE, false, false, entryNames));
	}

	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "gen/pkgX.class", "gen/pkg.class", "gen/pkg_1.class" })
	void scanClassNames_skipsTheClassesOfTheParentPackageOfAJarPackage(final String parentEntry)
		throws Exception
	{
		final String[] entryNames = { "gen/", "gen/pkg/", "gen/pkg/A.class", parentEntry };

		assertAll(
			() -> assertEquals(Set.of(DEFECT_PACKAGE + ".A"),
				scanJar(DEFECT_PACKAGE, false, true, entryNames)),
			() -> assertEquals(Set.of("A"), scanJar(DEFECT_PACKAGE, false, false, entryNames)));
	}

	@ParameterizedTest(name = "{0}")
	@CsvSource({ "gen/pkgother/, gen/pkgother/B.class", "gen/pkg_old/, gen/pkg_old/B.class",
			"gen/pkgX/, gen/pkgX/B.class" })
	void scanClassNames_skipsTheSiblingPackagesThatShareThePrefix_whenRecursive(
		final String siblingDirectory, final String siblingClass) throws Exception
	{
		final String[] entryNames = { "gen/", "gen/pkg/", "gen/pkg/A.class", "gen/pkg/sub/",
				"gen/pkg/sub/C.class", siblingDirectory, siblingClass };

		assertAll(
			() -> assertEquals(Set.of(DEFECT_PACKAGE + ".A", DEFECT_PACKAGE + ".sub.C"),
				scanJar(DEFECT_PACKAGE, true, true, entryNames)),
			() -> assertEquals(Set.of("A", "C"), scanJar(DEFECT_PACKAGE, true, false, entryNames)));
	}

	@ParameterizedTest(name = "a {0} and a {1}")
	@CsvSource({ "DIRECTORY, DIRECTORY", "JAR, JAR", "DIRECTORY, JAR", "JAR, DIRECTORY" })
	void scanClassNames_readsThePackageInEveryRootOfTheClassPath(final RootKind firstRoot,
		final RootKind secondRoot) throws Exception
	{
		final Path first = firstRoot.write(directory.resolve("first"), DEFECT_PACKAGE + ".A");
		final Path second = secondRoot.write(directory.resolve("second"), DEFECT_PACKAGE + ".B",
			DEFECT_PACKAGE + ".sub.C");

		assertAll(
			() -> assertEquals(Set.of(DEFECT_PACKAGE + ".A", DEFECT_PACKAGE + ".B"),
				scanRoots(DEFECT_PACKAGE, false, true, first, second)),
			() -> assertEquals(
				Set.of(DEFECT_PACKAGE + ".A", DEFECT_PACKAGE + ".B", DEFECT_PACKAGE + ".sub.C"),
				scanRoots(DEFECT_PACKAGE, true, true, first, second)));
	}

	@Test
	void scanClassNames_readsAPackageThatIsInTheMainAndInTheTestClasses() throws IOException
	{
		final Set<String> classNames = ScanPackageExtensions
			.scanClassNames(ScanPackageExtensions.class.getPackageName());

		assertTrue(
			classNames.containsAll(Set.of(ScanPackageExtensions.class.getName(),
				ScanPackageExtensionsTest.class.getName())),
			() -> "the classes found: " + classNames);
	}

	@ParameterizedTest(name = "from a {0}")
	@EnumSource(RootKind.class)
	void scanClassNames_listsTheTopLevelClassesOnly_fromEveryKindOfRoot(final RootKind rootKind)
		throws Exception
	{
		final Path root = rootKind.write(directory.resolve("root"), "nest.pkg.Outer",
			"nest.pkg.Outer$Inner", "nest.pkg.Outer$1", "nest.pkg.sub.Deep",
			"nest.pkg.sub.Deep$Inner");

		assertAll(
			() -> assertEquals(Set.of("nest.pkg.Outer"), scanRoots("nest.pkg", false, true, root)),
			() -> assertEquals(Set.of("nest.pkg.Outer", "nest.pkg.sub.Deep"),
				scanRoots("nest.pkg", true, true, root)),
			() -> assertEquals(Set.of("Outer", "Deep"), scanRoots("nest.pkg", true, false, root)));
	}

	@ParameterizedTest(name = "{0} archive")
	@ValueSource(strings = { "war", "ear" })
	void scanClassNames_readsAPackageInAWarOrEarArchiveLikeAJar(final String protocol)
		throws Exception
	{
		final Path jar = newScanJar(directory);
		final URL packageUrl = URL.of(
			URI.create(protocol + ":" + jar.toUri().toURL() + "!/generated/scan"),
			new UnopenedStreamHandler());
		final ClassLoader archiveClassLoader = new ArchiveClassLoader("generated/scan", packageUrl);

		assertEquals(Set.of(JAR_PACKAGE + ".First", JAR_PACKAGE + ".Second"),
			ClassFixtures.withContextClassLoader(archiveClassLoader,
				() -> ScanPackageExtensions.scanClassNames(JAR_PACKAGE)));
	}

	/**
	 * Answers the lookup of one package with an archive URL, as the class loader of an application
	 * server does for a package in a war or an ear
	 */
	private static final class ArchiveClassLoader extends ClassLoader
	{
		private final String packagePath;

		private final URL packageUrl;

		ArchiveClassLoader(final String packagePath, final URL packageUrl)
		{
			super(ClassLoader.getSystemClassLoader());
			this.packagePath = packagePath;
			this.packageUrl = packageUrl;
		}

		@Override
		protected Enumeration<URL> findResources(final String name)
		{
			return Collections
				.enumeration(packagePath.equals(name) ? List.of(packageUrl) : List.of());
		}
	}

	/**
	 * A stream handler for archive URLs that are only parsed, never opened
	 */
	private static final class UnopenedStreamHandler extends URLStreamHandler
	{
		@Override
		protected URLConnection openConnection(final URL url) throws IOException
		{
			throw new IOException("the archive URL " + url + " is not opened in this test");
		}
	}
}
