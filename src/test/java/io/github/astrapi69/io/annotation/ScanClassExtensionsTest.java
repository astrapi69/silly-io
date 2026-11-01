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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.io.annotation.ClassFixtures.UrlStyle;
import io.github.astrapi69.io.annotation.testdata.info.InfoPlain;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPage;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPanel;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPlain;
import io.github.astrapi69.io.annotation.testdata.scan.sub.ScannedSubPage;

/**
 * {@link ScanClassExtensions} compares classes by their normalized names and loads the classes of a
 * package, from a directory or through the package scan of {@link ScanPackageExtensions}. The scans
 * read the fixtures in {@code testdata.scan} and {@code testdata.info} on the test class path and a
 * jar built in a temporary directory
 */
class ScanClassExtensionsTest
{

	private static final Set<Class<?>> TOP_LEVEL_CLASSES = Set.of(ScannedPage.class,
		ScannedPanel.class, ScannedPlain.class);

	private static final Set<Class<?>> ALL_CLASSES = Set.of(ScannedPage.class, ScannedPanel.class,
		ScannedPlain.class, ScannedSubPage.class);

	private static final String NEST_PACKAGE = "nest.pkg";

	@TempDir
	Path directory;

	static Stream<Arguments> classPairs()
	{
		final Class<?> anonymousClass = new Object()
		{
		}.getClass();
		return Stream.of(
			arguments(named("the same class", ScannedPage.class), ScannedPage.class, true),
			arguments(named("two different classes", ScannedPage.class), ScannedPanel.class, false),
			arguments(named("an anonymous class and its enclosing class", anonymousClass),
				ScanClassExtensionsTest.class, true),
			arguments(named("a named nested class and its enclosing class", Nested.class),
				ScanClassExtensionsTest.class, false));
	}

	/**
	 * The scans of {@link ScanClassExtensions} over a class directory with a nested and an
	 * anonymous class in the package and a nested class in a sub package: the top-level scans load
	 * no class whose name holds a {@code $}, the scans for all classes load them too
	 *
	 * @return the scan and the binary names of the classes it loads, for each case
	 */
	static Stream<Arguments> classKindScans()
	{
		return Stream.of(
			arguments(
				named("scanTopLevelClasses",
					(RootScan)root -> ScanClassExtensions.scanTopLevelClasses(NEST_PACKAGE)),
				Set.of("nest.pkg.Outer")),
			arguments(
				named("scanTopLevelClasses, recursive",
					(RootScan)root -> ScanClassExtensions.scanTopLevelClasses(NEST_PACKAGE, true)),
				Set.of("nest.pkg.Outer", "nest.pkg.sub.Deep")),
			arguments(
				named("scanAllClassesFromPackage",
					(RootScan)root -> ScanClassExtensions.scanAllClassesFromPackage(
						root.resolve("nest/pkg").toFile(), NEST_PACKAGE)),
				Set.of("nest.pkg.Outer", "nest.pkg.Outer$Inner", "nest.pkg.Outer$1")),
			arguments(
				named("scanAllClassesFromPackage, recursive",
					(RootScan)root -> ScanClassExtensions.scanAllClassesFromPackage(
						root.resolve("nest/pkg").toFile(), NEST_PACKAGE, true)),
				Set.of("nest.pkg.Outer", "nest.pkg.Outer$Inner", "nest.pkg.Outer$1",
					"nest.pkg.sub.Deep", "nest.pkg.sub.Deep$Inner")));
	}

	static Stream<Arguments> infoFileScans()
	{
		return Stream.of(arguments(false, Set.of("info.pkg.Info")),
			arguments(true, Set.of("info.pkg.Info", "info.pkg.sub.Deep")));
	}

	static Path scanDirectory() throws Exception
	{
		return Path.of(ScannedPage.class.getResource("").toURI());
	}

	@ParameterizedTest(name = "{0}: {2}")
	@MethodSource("classPairs")
	void equalsByClassName_comparesTheNamesWithoutCompilerGeneratedSuffixes(final Class<?> oneClass,
		final Class<?> otherClass, final boolean expected)
	{
		assertEquals(expected, ScanClassExtensions.equalsByClassName(oneClass, otherClass));
	}

	@ParameterizedTest(name = "{0} -> {1}")
	@CsvSource({ "io/github/Foo.class, io.github.Foo", "io/github/Foo$1.class, io.github.Foo",
			"io.github.Foo$12, io.github.Foo", "io.github.Foo$Inner, io.github.Foo$Inner",
			"io.github.Foo$, io.github.Foo$", "io.github.Foo, io.github.Foo" })
	void normalizeQualifiedClassName_dropsTheClassSuffixAndAnonymousClassNumbers(
		final String qualifiedClassname, final String expected)
	{
		assertEquals(expected, ScanClassExtensions.normalizeQualifiedClassName(qualifiedClassname));
	}

	@ParameterizedTest(name = "{0} -> {1}")
	@CsvSource({ "Foo.class, Foo", "Foo$3.class, Foo", "Foo$Bar.class, Foo$Bar", "Foo$1a, Foo$1a",
			"Foo$, Foo$", "Foo, Foo" })
	void normalizeSimpleClassName_dropsTheClassSuffixAndAnonymousClassNumbers(
		final String className, final String expected)
	{
		assertEquals(expected, ScanClassExtensions.normalizeSimpleClassName(className));
	}

	@Test
	void scanAllClassesFromPackage_loadsTheClassesOfTheDirectoryOnly_byDefault() throws Exception
	{
		assertEquals(TOP_LEVEL_CLASSES, ScanClassExtensions.scanAllClassesFromPackage(
			scanDirectory().toFile(), ScanPackageExtensionsTest.SCAN_PACKAGE));
	}

	@Test
	void scanAllClassesFromPackage_loadsTheClassesOfTheSubDirectories_whenRecursive()
		throws Exception
	{
		assertEquals(ALL_CLASSES, ScanClassExtensions.scanAllClassesFromPackage(
			scanDirectory().toFile(), ScanPackageExtensionsTest.SCAN_PACKAGE, true));
	}

	/**
	 * A {@code package-info.class} holds the annotations of its package and a
	 * {@code module-info.class} the declaration of a module: neither is a class, so neither is
	 * loaded, in the directory or in a sub directory
	 *
	 * @param recursive
	 *            the recursive flag
	 * @param expected
	 *            the binary names of the classes the scan loads
	 * @throws Exception
	 *             if the class files cannot be written or the scan fails
	 */
	@ParameterizedTest(name = "recursive: {0}")
	@MethodSource("infoFileScans")
	void scanAllClassesFromPackage_skipsPackageInfoAndModuleInfo(final boolean recursive,
		final Set<String> expected) throws Exception
	{
		final Path root = ClassFixtures.newClassDirectory(directory.resolve("root"),
			"info.pkg.Info", "info.pkg.package-info", "info.pkg.module-info", "info.pkg.sub.Deep",
			"info.pkg.sub.package-info");

		final Set<Class<?>> classes = ClassFixtures
			.withClassPathRoots(
				() -> ScanClassExtensions.scanAllClassesFromPackage(
					root.resolve("info/pkg").toFile(), "info.pkg", recursive),
				UrlStyle.ESCAPED.of(root));

		assertEquals(expected, Set.copyOf(classes.stream().map(Class::getName).toList()));
	}

	@Test
	void scanAllClassesFromPackage_skipsThePackageInfoOfAnAnnotatedPackage() throws Exception
	{
		assertEquals(Set.of(InfoPlain.class),
			ScanClassExtensions.scanAllClassesFromPackage(
				Path.of(InfoPlain.class.getResource("").toURI()).toFile(),
				InfoPlain.class.getPackageName()));
	}

	@Test
	void scanAllClassesFromPackage_isEmpty_forADirectoryThatDoesNotExist() throws Exception
	{
		assertTrue(ScanClassExtensions
			.scanAllClassesFromPackage(directory.resolve("missing").toFile(), "missing", true)
			.isEmpty());
	}

	@Test
	void scanTopLevelClasses_loadsTheClassesOfThePackage() throws Exception
	{
		assertEquals(TOP_LEVEL_CLASSES,
			ScanClassExtensions.scanTopLevelClasses(ScanPackageExtensionsTest.SCAN_PACKAGE));
	}

	@Test
	void scanTopLevelClasses_loadsTheClassesOfTheSubPackages_whenRecursive() throws Exception
	{
		assertEquals(ALL_CLASSES,
			ScanClassExtensions.scanTopLevelClasses(ScanPackageExtensionsTest.SCAN_PACKAGE, true));
	}

	@Test
	void scanTopLevelClasses_loadsTheClassesOfAJarPackage_throughTheContextClassLoader()
		throws Exception
	{
		final Path jar = ScanPackageExtensionsTest.newScanJar(directory);
		try (URLClassLoader jarClassLoader = new URLClassLoader(new URL[] { jar.toUri().toURL() },
			ClassLoader.getSystemClassLoader()))
		{
			final Set<Class<?>> classes = ClassFixtures.withContextClassLoader(jarClassLoader,
				() -> ScanClassExtensions
					.scanTopLevelClasses(ScanPackageExtensionsTest.JAR_PACKAGE));

			assertEquals(Set.of("generated.scan.First", "generated.scan.Second"),
				Set.copyOf(classes.stream().map(Class::getName).toList()));
			assertEquals(List.of(jarClassLoader, jarClassLoader),
				classes.stream().map(Class::getClassLoader).toList());
		}
	}

	/**
	 * A named nested class, whose name keeps its suffix when normalized
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("classKindScans")
	void scanTopLevelClassesAndScanAllClassesFromPackage_loadTheKindOfClassesTheirNamesSay(
		final RootScan scan, final Set<String> expected) throws Exception
	{
		final Path root = ClassFixtures.newClassDirectory(directory.resolve("root"),
			"nest.pkg.Outer", "nest.pkg.Outer$Inner", "nest.pkg.Outer$1", "nest.pkg.sub.Deep",
			"nest.pkg.sub.Deep$Inner");

		final Set<Class<?>> classes = ClassFixtures.withClassPathRoots(() -> scan.scan(root),
			UrlStyle.ESCAPED.of(root));

		assertEquals(expected, Set.copyOf(classes.stream().map(Class::getName).toList()));
	}

	/**
	 * One of the class scans of {@link ScanClassExtensions} over a class path root
	 */
	@FunctionalInterface
	interface RootScan
	{
		/**
		 * Scans the given class path root
		 *
		 * @param root
		 *            the class directory the scan reads
		 * @return the classes the scan loads
		 * @throws Exception
		 *             if the scan fails
		 */
		Set<Class<?>> scan(Path root) throws Exception;
	}

	static final class Nested
	{
	}
}
