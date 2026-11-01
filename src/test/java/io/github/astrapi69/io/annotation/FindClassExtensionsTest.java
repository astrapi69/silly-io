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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
