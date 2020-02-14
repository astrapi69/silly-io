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
package de.alpharogroup.io.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import de.alpharogroup.collections.array.ArrayFactory;

/**
 * The unit test class for the class {@link FileConstants}
 */
public class FileConstantsTest
{

	/**
	 * Test method for {@link FileConstants#BACKSLASH}
	 */
	@Test
	public void testConstantBACKSLASH()
	{
		String actual;
		String expected;

		actual = FileConstants.BACKSLASH;
		expected = "\\";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileConstants#BLOCKSIZE}
	 */
	@Test
	public void testConstantBLOCKSIZE()
	{
		int actual;
		int expected;

		actual = FileConstants.BLOCKSIZE;
		expected = 8192;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileConstants#DOT}
	 */
	@Test
	public void testConstantDOT()
	{
		String actual;
		String expected;

		actual = FileConstants.DOT;
		expected = ".";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileConstants#DOUBLEDOT}
	 */
	@Test
	public void testConstantDOUBLEDOT()
	{
		String actual;
		String expected;

		actual = FileConstants.DOUBLEDOT;
		expected = ":";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileConstants#INVALID_CHARS_IN_FILENAME}
	 */
	@Test
	public void testConstantINVALID_CHARS_IN_FILENAME()
	{
		String[] actual;
		String[] expected;

		actual = FileConstants.INVALID_CHARS_IN_FILENAME;
		expected = ArrayFactory.newArray("\\", "/", ":", "*", "\"", "<", ">", "|");
		assertThat(actual, equalTo(expected));
	}

	/**
	 * Test method for {@link FileConstants#KILOBYTE}
	 */
	@Test
	public void testConstantKILOBYTE()
	{
		int actual;
		int expected;

		actual = FileConstants.KILOBYTE;
		expected = 1024;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileConstants#SLASH}
	 */
	@Test
	public void testConstantSLASH()
	{
		String actual;
		String expected;

		actual = FileConstants.SLASH;
		expected = "/";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link FileConstants#ZIP_EXTENSIONS}
	 */
	@Test
	public void testConstantZIP_EXTENSIONS()
	{
		String[] actual;
		String[] expected;

		actual = FileConstants.ZIP_EXTENSIONS;
		expected = ArrayFactory.newArray(".zip", ".jar", ".war", ".ear", ".tar", ".rar", ".7z",
			".bz2", ".gz");
		assertThat(actual, equalTo(expected));
	}

}
