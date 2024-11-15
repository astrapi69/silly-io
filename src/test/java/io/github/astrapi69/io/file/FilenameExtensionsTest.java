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
package io.github.astrapi69.io.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link FilenameExtensions}
 */
public class FilenameExtensionsTest
{

	@ParameterizedTest
	@CsvSource({ "test.txt,.jpg,test.jpg", "document.pdf,.doc,document.doc",
			"archive.zip,.tar,archive.tar", "image.png,.jpeg,image.jpeg" })
	void testGetFilenameWithNewExtension_String(String inputFilename, String newExtension,
		String expectedOutput)
	{
		File file = new File(inputFilename);
		String result = FilenameExtensions.getFilenameWithNewExtension(file, newExtension);
		assertEquals(expectedOutput, result);
	}

	@ParameterizedTest
	@CsvSource({ "test.txt,.jpg,test.jpg", "document.pdf,.doc,document.doc",
			"archive.zip,.tar,archive.tar", "image.png,.jpeg,image.jpeg" })
	void testGetFilenameWithNewExtension_FileExtension(String inputFilename, String newExtension,
		String expectedOutput)
	{
		File file = new File(inputFilename);
		FileExtension fileExtension = FileExtension.resolve(newExtension);
		String result = FilenameExtensions.getFilenameWithNewExtension(file, fileExtension);
		assertEquals(expectedOutput, result);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenamePrefix(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFilenamePrefix() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		expected = "io/github/astrapi69/lang/resources";
		actual = FilenameExtensions.getFilenamePrefix(file);
		assertTrue(actual.endsWith(expected));
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenamePrefix(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFilenamePrefixWithDir() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		File dir = file.getParentFile();

		expected = "io/github/astrapi69/lang";
		actual = FilenameExtensions.getFilenamePrefix(dir);
		assertTrue(actual.endsWith(expected));
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenameSuffix(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFilenameSuffix() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		expected = ".properties";
		actual = FilenameExtensions.getFilenameSuffix(file);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFileExtension(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFileExtension() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		expected = "properties";
		actual = FilenameExtensions.getFileExtension(file);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFileExtension(File, boolean)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFileExtensionWithDot() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		expected = ".properties";
		actual = FilenameExtensions.getFileExtension(file, true);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFileExtension(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFileExtensionEmptyStringCase() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		File dir = file.getParentFile();
		expected = "";
		actual = FilenameExtensions.getFileExtension(dir);
		assertEquals(expected, actual);

		file = new File(dir, "noextensionfile");
		expected = "";
		actual = FilenameExtensions.getFileExtension(file);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenameSuffix(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFilenameSuffixNullCase() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		File dir = file.getParentFile();
		expected = null;
		actual = FilenameExtensions.getFilenameSuffix(dir);
		assertEquals(expected, actual);

		file = new File(dir, "noextensionfile");
		expected = null;
		actual = FilenameExtensions.getFilenameSuffix(file);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenameWithoutExtension(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFilenameWithoutExtensionFile() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		expected = "resources";
		actual = FilenameExtensions.getFilenameWithoutExtension(file);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenameWithoutExtension(File)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetFilenameWithoutExtensionFileNullCase() throws URISyntaxException
	{
		String expected;
		String actual;

		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);
		File dir = file.getParentFile();
		expected = null;
		actual = FilenameExtensions.getFilenameWithoutExtension(dir);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions#getFilenameWithoutExtension(String)}
	 */
	@Test
	public void testGetFilenameWithoutExtensionString()
	{
		String expected;
		String actual;
		final String fileName = "io/github/astrapi69/lang/resources";
		actual = FilenameExtensions.getFilenameWithoutExtension(fileName);
		expected = fileName;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FilenameExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FilenameExtensions.class);
	}


}
