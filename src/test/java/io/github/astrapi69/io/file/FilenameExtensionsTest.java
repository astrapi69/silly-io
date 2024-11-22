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
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link FilenameExtensions}
 */
public class FilenameExtensionsTest
{

	/**
	 * Test for the {@link FilenameExtensions#getName(File)}
	 */
	@Test
	void testGetName()
	{

		String expected;
		String actual;
		String fileName;

		fileName = "example.txt";
		// Given: a file with a specific name
		File file = new File(PathFinder.getSrcTestResourcesDir(), "example.txt");

		// When: getting the name of the file
		actual = FilenameExtensions.getName(file);

		expected = fileName;
		// Then: the name should match the expected value
		assertEquals(expected, actual);
	}

	/**
	 * Test for the {@link FilenameExtensions#getName(File)} method with edge cases
	 */
	@Test
	void testGetNameWithEdgeCases()
	{
		// Given: files with different names
		File emptyNameFile = new File("");
		File hiddenFile = new File(".hiddenfile");

		// When: getting the names of the files
		String emptyFileName = FilenameExtensions.getName(emptyNameFile);
		String hiddenFileName = FilenameExtensions.getName(hiddenFile);

		// Then: names should match expected values
		assertEquals("", emptyFileName, "File with no name should return an empty string");
		assertEquals(".hiddenfile", hiddenFileName,
			"Hidden file name should be returned correctly");
	}

	/**
	 * Tests the {@link FilenameExtensions#sanitizeFilename(String, Map)} method with various
	 * filenames and a replacement map that replaces spaces with underscores and periods with
	 * hyphens.
	 *
	 * <p>
	 * Scenarios covered:
	 * <ul>
	 * <li>Filename with spaces</li>
	 * <li>Filename with multiple periods</li>
	 * <li>Filename without special characters</li>
	 * </ul>
	 *
	 * <p>
	 * For example, given the input <code>"CamScanner 21.11.2024 17.46.pdf"</code> and the
	 * replacement map:
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Map&lt;Character, String&gt; replacementMap = new HashMap&lt;&gt;();
	 * 	replacementMap.put(' ', "_");
	 * 	replacementMap.put('.', "-");
	 * }
	 * </pre>
	 * 
	 * The expected output is <code>"CamScanner_21-11-2024_17-46.pdf"</code>.
	 */
	@Test
	public void testSanitizeFilename()
	{
		// Create a replacement map
		Map<Character, String> replacementMap = new HashMap<>();
		replacementMap.put(' ', "_");
		replacementMap.put('.', "-");

		// Test cases
		String input1 = "CamScanner 21.11.2024 17.46.pdf";
		String expected1 = "CamScanner_21-11-2024_17-46.pdf";
		assertEquals(expected1, FilenameExtensions.sanitizeFilename(input1, replacementMap));

		String input2 = "File with spaces.txt";
		String expected2 = "File_with_spaces.txt";
		assertEquals(expected2, FilenameExtensions.sanitizeFilename(input2, replacementMap));

		String input3 = "Special.characters.in.name.doc";
		String expected3 = "Special-characters-in-name.doc";
		assertEquals(expected3, FilenameExtensions.sanitizeFilename(input3, replacementMap));

		String input4 = "NoSpecialCharacters";
		String expected4 = "NoSpecialCharacters";
		assertEquals(expected4, FilenameExtensions.sanitizeFilename(input4, replacementMap));

		String input5 = "Multiple..dots..in..name.pdf";
		String expected5 = "Multiple--dots--in--name.pdf";
		assertEquals(expected5, FilenameExtensions.sanitizeFilename(input5, replacementMap));
	}

	/**
	 * Tests the {@link FilenameExtensions#sanitizeFilename(String, Map)} method with an empty
	 * replacement map, ensuring that no changes are made to the filename.
	 *
	 * <p>
	 * Scenario covered:
	 * <ul>
	 * <li>Filename with spaces and a file extension, but no replacements specified</li>
	 * </ul>
	 *
	 * <p>
	 * For example, given the input <code>"Test File.txt"</code> and an empty replacement map:
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Map&lt;Character, String&gt; emptyMap = new HashMap&lt;&gt;();
	 * }
	 * </pre>
	 * 
	 * The expected output is <code>"Test File.txt"</code>.
	 */
	@Test
	public void testSanitizeFilenameWithEmptyMap()
	{
		// Empty replacement map
		Map<Character, String> emptyMap = new HashMap<>();

		String input = "Test File.txt";
		String expected = "Test File.txt"; // No changes expected
		assertEquals(expected, FilenameExtensions.sanitizeFilename(input, emptyMap));
	}

	/**
	 * Tests the {@link FilenameExtensions#sanitizeFilename(String, Map)} method with a custom
	 * replacement map that replaces spaces with hyphens and the character 'a' (case-insensitive)
	 * with '@'.
	 *
	 * <p>
	 * Scenario covered:
	 * <ul>
	 * <li>Filename with spaces and specific characters to replace</li>
	 * </ul>
	 *
	 * <p>
	 * For example, given the input <code>"A fancy filename.txt"</code> and the replacement map:
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Map&lt;Character, String&gt; replacementMap = new HashMap&lt;&gt;();
	 * 	replacementMap.put(' ', "-");
	 * 	replacementMap.put('a', "@");
	 * }
	 * </pre>
	 * 
	 * The expected output is <code>"@-f@ncy-filen@me.txt"</code>.
	 */
	@Test
	public void testSanitizeFilenameWithCustomMap()
	{
		// Custom replacement map
		Map<Character, String> replacementMap = new HashMap<>();
		replacementMap.put(' ', "-");
		replacementMap.put('a', "@");

		String input = "A fancy filename.txt";
		String expected = "@-f@ncy-filen@me.txt";
		assertEquals(expected, FilenameExtensions.sanitizeFilename(input, replacementMap));
	}

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
