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

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link SuffixFileFilter}
 */
public class SuffixFileFilterTest
{

	@Test
	public void testFactoryMethods() throws URISyntaxException{

		boolean expected;
		boolean actual;
		String suffix;
		boolean recursive;
		FileFilter suffixFileFilter;
		String filename;
		String filepath;
		File file;
		File dir;

		suffix = ".properties";
		suffixFileFilter = SuffixFileFilter.of(suffix);
		assertNotNull(suffixFileFilter);

		filename = "resources.properties";

		filepath = "io/github/astrapi69/lang/" + filename;

		file = ClassExtensions.getResourceAsFile(filepath);
		dir = file.getParentFile();

		actual = suffixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		suffix = ".properties";
		recursive = true;
		suffixFileFilter = SuffixFileFilter.of(suffix, true);
		assertNotNull(suffixFileFilter);

		actual = suffixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		actual = suffixFileFilter.accept(dir);
		expected = true;
		assertEquals(expected, actual);
	}

	@Test
	public void testAccept() throws URISyntaxException
	{
		boolean expected;
		boolean actual;
		String suffix;
		boolean recursive;
		FileFilter suffixFileFilter;
		String filename;
		String filepath;
		File file;
		File dir;

		suffix = ".properties";
		suffixFileFilter = new SuffixFileFilter(suffix);
		assertNotNull(suffixFileFilter);

		filename = "resources.properties";

		filepath = "io/github/astrapi69/lang/" + filename;

		file = ClassExtensions.getResourceAsFile(filepath);
		dir = file.getParentFile();

		actual = suffixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		suffix = ".properties";
		recursive = true;
		suffixFileFilter = new SuffixFileFilter(suffix, true);
		assertNotNull(suffixFileFilter);

		actual = suffixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		actual = suffixFileFilter.accept(dir);
		expected = true;
		assertEquals(expected, actual);
	}
}
