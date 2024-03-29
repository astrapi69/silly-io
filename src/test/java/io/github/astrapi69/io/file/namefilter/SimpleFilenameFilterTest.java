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
package io.github.astrapi69.io.file.namefilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.evaluate.object.evaluator.ToStringEvaluator;
import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link SimpleFilenameFilter}
 */
public class SimpleFilenameFilterTest
{

	/**
	 * Test method for {@link SimpleFilenameFilter#accept(File, String)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public final void testAccept() throws URISyntaxException
	{
		boolean expected;
		boolean actual;
		boolean acceptDir;
		String filesuffix;
		FilenameFilter filenameFilter;
		String filename;
		String propertiesFilename;
		File file;
		File dir;

		filesuffix = ".properties";
		acceptDir = false;
		filenameFilter = SimpleFilenameFilter.of(filesuffix, acceptDir);
		assertNotNull(filenameFilter);

		filename = "resources.properties";

		propertiesFilename = "io/github/astrapi69/lang/" + filename;

		file = ClassExtensions.getResourceAsFile(propertiesFilename);
		dir = file.getParentFile();

		actual = filenameFilter.accept(dir, filename);
		expected = true;
		assertEquals(expected, actual);

		filesuffix = ".properties";
		acceptDir = true;
		filenameFilter = SimpleFilenameFilter.of(filesuffix, acceptDir);
		assertNotNull(filenameFilter);

		filename = "";

		actual = filenameFilter.accept(dir, filename);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link SimpleFilenameFilter} with argument of filesuffix as empty string
	 */
	@Test
	public final void testFileSuffixEmpty()
	{
		String filesuffix;
		boolean acceptDir;
		filesuffix = "";
		acceptDir = false;
		assertThrows(IllegalArgumentException.class,
			() -> SimpleFilenameFilter.of(filesuffix, acceptDir));
	}

	/**
	 * Test method for {@link SimpleFilenameFilter} with argument of filesuffix as null
	 */
	@Test
	public final void testFileSuffixNull()
	{
		String filesuffix;
		boolean acceptDir;
		filesuffix = "";
		acceptDir = false;
		assertThrows(IllegalArgumentException.class,
			() -> SimpleFilenameFilter.of(filesuffix, acceptDir));
	}

	/**
	 * Test method for {@link SimpleFilenameFilter#toString()}
	 */
	@Test
	public void testToString()
	{
		boolean expected;
		boolean actual;

		String filesuffix;
		boolean acceptDir;
		filesuffix = ".properties";
		acceptDir = false;

		actual = ToStringEvaluator
			.evaluateConsistency(SimpleFilenameFilter.of(filesuffix, acceptDir));
		expected = true;
		assertEquals(expected, actual);
	}

}
