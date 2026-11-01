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
package io.github.astrapi69.io.file.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.evaluate.object.evaluator.ToStringEvaluator;
import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link MultiplyExtensionsFileFilter}
 */
public class MultiplyExtensionsFileFilterTest
{

	@TempDir
	Path directory;

	/**
	 * The extensions and the file name are compared without their case, as
	 * {@code SimpleFilenameFilter} compares them
	 *
	 * @param extension
	 *            the extension of the filter
	 * @param name
	 *            the name of the file
	 * @param expected
	 *            whether the filter accepts the file
	 * @throws IOException
	 *             if the file cannot be created
	 */
	@ParameterizedTest(name = "extension {0}, file {1}: {2}")
	@CsvSource({ ".TXT, NOTES.TXT, true", ".txt, NOTES.TXT, true", ".TXT, notes.txt, true",
			".Txt, notes.tXT, true", ".TXT, NOTES.MD, false" })
	void accept_ignoresTheCaseOfTheExtensionsAndOfTheName(final String extension, final String name,
		final boolean expected) throws IOException
	{
		final File file = Files.createFile(directory.resolve(name)).toFile();

		assertEquals(expected, new MultiplyExtensionsFileFilter(extension).accept(file));
	}

	/**
	 * Test method for {@link MultiplyExtensionsFileFilter#accept(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public final void testAccept() throws URISyntaxException, IOException
	{
		boolean expected;
		boolean actual;
		boolean acceptDir;
		String filesuffix;
		FileFilter fileFilter;
		String filename;
		String propertiesFilename;
		File file;
		File dir;
		Collection<String> fileExtensions;

		filesuffix = ".properties";
		fileExtensions = ListFactory.newArrayList(filesuffix, ".txt");
		acceptDir = false;
		fileFilter = new MultiplyExtensionsFileFilter(fileExtensions, acceptDir);
		assertNotNull(fileFilter);

		filename = "resources.properties";

		propertiesFilename = "io/github/astrapi69/lang/" + filename;

		file = ClassExtensions.getResourceAsFile(propertiesFilename);
		dir = file.getParentFile();

		actual = fileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		acceptDir = true;
		fileFilter = new MultiplyExtensionsFileFilter(fileExtensions, acceptDir);
		assertNotNull(fileFilter);

		actual = fileFilter.accept(dir);
		assertEquals(expected, actual);

		file = new File(".", "TestFind.class");
		FileUtils.writeStringToFile(file, "", Charset.defaultCharset());

		actual = fileFilter.accept(file);
		expected = false;
		assertEquals(expected, actual);
		try
		{
			file.deleteOnExit();
		}
		catch (final Exception e)
		{
			// ignore...
		}
	}

	/**
	 * Test method for {@link MultiplyExtensionsFileFilter} constructors
	 */
	@Test
	public final void testConstructors()
	{
		boolean acceptDir;
		String filesuffix;
		FileFilter fileFilter;
		Collection<String> fileExtensions;

		acceptDir = false;
		filesuffix = ".properties";
		fileExtensions = ListFactory.newArrayList(filesuffix, ".txt");

		fileFilter = new MultiplyExtensionsFileFilter(acceptDir,
			fileExtensions.toArray(new String[fileExtensions.size()]));
		assertNotNull(fileFilter);

		fileFilter = new MultiplyExtensionsFileFilter(fileExtensions);
		assertNotNull(fileFilter);

		fileFilter = new MultiplyExtensionsFileFilter(
			fileExtensions.toArray(new String[fileExtensions.size()]));
		assertNotNull(fileFilter);
	}

	/**
	 * Test method for {@link MultiplyExtensionsFileFilter} constructors with empty list
	 */
	@Test
	public final void testConstructorsWithEmptyList()
	{
		assertThrows(IllegalArgumentException.class,
			() -> new MultiplyExtensionsFileFilter(ListFactory.newArrayList()));
	}

	/**
	 * Test method for {@link MultiplyExtensionsFileFilter} constructors with null values
	 */
	@Test
	public final void testConstructorsWithNullValues()
	{
		Collection<String> fileExtensions = null;
		assertThrows(IllegalArgumentException.class,
			() -> new MultiplyExtensionsFileFilter(fileExtensions));
	}

	/**
	 * Test method for {@link MultiplyExtensionsFileFilter#toString()}
	 */
	@Test
	public final void testToString()
	{
		boolean expected;
		boolean actual;

		String filesuffix;
		boolean acceptDir;
		acceptDir = false;
		Collection<String> fileExtensions;

		filesuffix = ".properties";
		fileExtensions = ListFactory.newArrayList(filesuffix, ".txt");
		actual = ToStringEvaluator
			.evaluateConsistency(new MultiplyExtensionsFileFilter(fileExtensions, acceptDir));
		expected = true;
		assertEquals(expected, actual);
	}

}
