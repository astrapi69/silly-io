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
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.github.astrapi69.io.file.CaseSensitivity;
import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link PrefixFileFilter}
 */
public class PrefixFileFilterTest
{

	@TempDir
	Path directory;

	/**
	 * Without a case sensitivity, and with {@link CaseSensitivity#SENSITIVE}, the name has to start
	 * with the prefix in the same case
	 *
	 * @param prefix
	 *            the prefix of the filter
	 * @param name
	 *            the name of the file
	 * @param expected
	 *            whether the filter accepts the file
	 * @throws IOException
	 *             if the file cannot be created
	 */
	@ParameterizedTest(name = "prefix {0}, file {1}: {2}")
	@CsvSource({ "report, report-1.txt, true", "report, Report-1.txt, false",
			"REPORT, report-1.txt, false", "report, 1-report.txt, false",
			"report, summary.txt, false" })
	void accept_takesThePrefixAtTheStartOfTheName_inTheSameCase(final String prefix,
		final String name, final boolean expected) throws IOException
	{
		final File file = Files.createFile(directory.resolve(name)).toFile();

		assertEquals(expected, new PrefixFileFilter(prefix).accept(file));
		assertEquals(expected, new PrefixFileFilter(prefix, false).accept(file));
		assertEquals(expected,
			new PrefixFileFilter(prefix, false, CaseSensitivity.SENSITIVE).accept(file));
		assertEquals(expected, PrefixFileFilter.of(prefix).accept(file));
		assertEquals(expected,
			PrefixFileFilter.of(prefix, true, CaseSensitivity.SENSITIVE).accept(file));
	}

	/**
	 * With {@link CaseSensitivity#INSENSITIVE} the name has to start with the prefix, in any case
	 *
	 * @param prefix
	 *            the prefix of the filter
	 * @param name
	 *            the name of the file
	 * @param expected
	 *            whether the filter accepts the file
	 * @throws IOException
	 *             if the file cannot be created
	 */
	@ParameterizedTest(name = "prefix {0}, file {1}: {2}")
	@CsvSource({ "report, report-1.txt, true", "report, Report-1.txt, true",
			"REPORT, report-1.txt, true", "rePort, REPORT-1.TXT, true",
			"report, 1-report.txt, false", "report, summary.txt, false" })
	void accept_takesThePrefixAtTheStartOfTheName_inAnyCase_whenInsensitive(final String prefix,
		final String name, final boolean expected) throws IOException
	{
		final File file = Files.createFile(directory.resolve(name)).toFile();

		assertEquals(expected,
			new PrefixFileFilter(prefix, false, CaseSensitivity.INSENSITIVE).accept(file));
		assertEquals(expected,
			PrefixFileFilter.of(prefix, true, CaseSensitivity.INSENSITIVE).accept(file));
	}

	@Test
	void new_refusesNoCaseSensitivity()
	{
		assertThrows(NullPointerException.class, () -> new PrefixFileFilter("report", true, null));
		assertThrows(NullPointerException.class, () -> PrefixFileFilter.of("report", false, null));
	}

	@Test
	public void testAccept() throws URISyntaxException
	{
		boolean expected;
		boolean actual;
		String prefix;
		boolean recursive;
		FileFilter prefixFileFilter;
		String filename;
		String filepath;
		File file;
		File dir;

		prefix = "resourc";
		prefixFileFilter = new PrefixFileFilter(prefix);
		assertNotNull(prefixFileFilter);

		filename = "resources.properties";

		filepath = "io/github/astrapi69/lang/" + filename;

		file = ClassExtensions.getResourceAsFile(filepath);
		dir = file.getParentFile();

		actual = prefixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		prefix = "resourc";
		recursive = true;
		prefixFileFilter = new PrefixFileFilter(prefix, true);
		assertNotNull(prefixFileFilter);

		actual = prefixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		actual = prefixFileFilter.accept(dir);
		expected = true;
		assertEquals(expected, actual);
	}
}
