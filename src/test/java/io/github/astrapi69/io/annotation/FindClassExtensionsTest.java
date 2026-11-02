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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.io.annotation.ClassFixtures.RootKind;
import io.github.astrapi69.io.annotation.ClassFixtures.UrlStyle;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPage;

/**
 * {@link FindClassExtensions} loads a class by its name, first through its own class loader and
 * then through the current one, and finds the directories of a package on the class path
 */
class FindClassExtensionsTest
{

	@TempDir
	Path directory;

	@Test
	void forName_loadsAClassOfTheClassPath() throws ClassNotFoundException
	{
		assertSame(ScannedPage.class, FindClassExtensions.forName(ScannedPage.class.getName()));
	}

	@Test
	void forName_throwsClassNotFoundException_forAClassThatNoClassLoaderKnows()
	{
		assertThrows(ClassNotFoundException.class,
			() -> FindClassExtensions.forName("io.github.astrapi69.no.such.Clazz"));
	}

	@Test
	void forName_loadsAClassThatOnlyTheContextClassLoaderKnows() throws Exception
	{
		final Path jar = ClassFixtures.newJar(directory.resolve("context.jar"), "generated/",
			"generated/context/", "generated/context/OnlyInTheJar.class");
		try (URLClassLoader jarClassLoader = new URLClassLoader(new URL[] { jar.toUri().toURL() },
			ClassLoader.getSystemClassLoader()))
		{
			final Class<?> found = ClassFixtures.withContextClassLoader(jarClassLoader,
				() -> FindClassExtensions.forName("generated.context.OnlyInTheJar"));

			assertEquals("generated.context.OnlyInTheJar", found.getName());
			assertSame(jarClassLoader, found.getClassLoader());
		}
	}

	@ParameterizedTest(name = "{0}, as a package: {1}")
	@CsvSource({ "io.github.astrapi69.io.annotation.testdata.scan, true",
			"io/github/astrapi69/io/annotation/testdata/scan, false" })
	void getDirectoriesFromResources_findsThePackageDirectory(final String path,
		final boolean isPackage) throws Exception
	{
		final List<File> directories = FindClassExtensions.getDirectoriesFromResources(path,
			isPackage);

		assertEquals(List.of(ScanClassExtensionsTest.scanDirectory()),
			directories.stream().map(File::toPath).toList());
	}

	@ParameterizedTest(name = "{0}, as a package: {1}")
	@CsvSource({ "io.github.astrapi69.no.such.pkg, true",
			"io/github/astrapi69/no/such/pkg, false" })
	void getDirectoriesFromResources_givesNoEntry_forAPathThatIsNotOnTheClassPath(final String path,
		final boolean isPackage) throws Exception
	{
		assertEquals(List.of(), FindClassExtensions.getDirectoriesFromResources(path, isPackage));
	}

	@ParameterizedTest(name = "below \"{0}\", {1} URL")
	@MethodSource("io.github.astrapi69.io.annotation.ClassFixtures#urlEscapedDirectories")
	void getDirectoriesFromResources_findsAPackageDirectoryWhosePathHoldsACharacterThatAUrlEscapes(
		final String directoryName, final UrlStyle urlStyle) throws Exception
	{
		final Path root = ClassFixtures
			.newClassDirectory(directory.resolve(directoryName).resolve("root"), "plus.pkg.Plus");

		final List<File> directories = ClassFixtures.withClassPathRoots(
			() -> FindClassExtensions.getDirectoriesFromResources("plus.pkg", true),
			urlStyle.of(root));

		assertEquals(List.of(root.resolve("plus").resolve("pkg")),
			directories.stream().map(File::toPath).toList());
	}

	@Test
	void getDirectoriesFromResources_givesNoEntry_forAPackageInAJar() throws Exception
	{
		final Path jar = ClassFixtures.newJarOfClasses(directory.resolve("package.jar"),
			"plus.pkg.Plus");

		final List<File> directories = ClassFixtures.withClassPathRoots(
			() -> FindClassExtensions.getDirectoriesFromResources("plus.pkg", true),
			UrlStyle.ESCAPED.of(jar));

		assertEquals(List.of(), directories);
	}

	/**
	 * The class path roots of the mixed cases, by the name each is written under, in their order
	 *
	 * @return the names of the roots, for each case
	 */
	static Stream<Arguments> mixedClassPaths()
	{
		return Stream.of(
			arguments(named("the class directory first", List.of("classes", "lib.jar"))),
			arguments(named("the jar first", List.of("lib.jar", "classes"))));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("mixedClassPaths")
	void getDirectoriesFromResources_givesTheClassDirectoriesOnly_ofAClassPathWithAJar(
		final List<String> rootNames) throws Exception
	{
		final Path classDirectory = RootKind.DIRECTORY.write(directory.resolve("classes"),
			"mixed.pkg.InTheDirectory");
		RootKind.JAR.write(directory.resolve("lib"), "mixed.pkg.InTheJar");
		final URL[] roots = new URL[rootNames.size()];
		for (int index = 0; index < roots.length; index++)
		{
			roots[index] = UrlStyle.ESCAPED.of(directory.resolve(rootNames.get(index)));
		}

		final List<File> directories = ClassFixtures.withClassPathRoots(
			() -> FindClassExtensions.getDirectoriesFromResources("mixed.pkg", true), roots);

		assertEquals(List.of(classDirectory.resolve("mixed").resolve("pkg")),
			directories.stream().map(File::toPath).toList());
	}

	/**
	 * A resource of a protocol that is neither a file nor an archive, as the virtual file system of
	 * an application server gives one, gives the file of its URL path
	 *
	 * @throws Exception
	 *             if the lookup fails
	 */
	@Test
	void getDirectoriesFromResources_givesTheFileOfTheUrlPath_forAnotherProtocol() throws Exception
	{
		final File packageDirectory = directory.resolve("deployment").resolve("vfs").resolve("pkg")
			.toFile();
		final ClassLoader vfsClassLoader = new ClassFixtures.PackageUrlClassLoader("vfs/pkg",
			ClassFixtures.unopenedUrl("vfs:" + packageDirectory.getPath()));

		assertEquals(List.of(packageDirectory), ClassFixtures.withContextClassLoader(vfsClassLoader,
			() -> FindClassExtensions.getDirectoriesFromResources("vfs.pkg", true)));
	}

	@Test
	void getResources_isEmpty_forAPathThatIsNotOnTheClassPath() throws Exception
	{
		assertTrue(FindClassExtensions.getResources("io/github/astrapi69/no/such/path").isEmpty());
	}

	@Test
	void getResources_findsTheResourceOfAPackageDirectory() throws Exception
	{
		final List<URL> resources = FindClassExtensions
			.getResources("io/github/astrapi69/io/annotation/testdata/scan");

		assertEquals(List.of(ScanClassExtensionsTest.scanDirectory()),
			resources.stream().map(FindClassExtensionsTest::toPath).toList());
	}

	private static Path toPath(final URL url)
	{
		try
		{
			return Path.of(url.toURI());
		}
		catch (final URISyntaxException exception)
		{
			throw new IllegalArgumentException(url.toString(), exception);
		}
	}
}
