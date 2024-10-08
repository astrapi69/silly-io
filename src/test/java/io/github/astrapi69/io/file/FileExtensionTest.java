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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.lang.ClassExtensions;

class FileExtensionTest
{

	@Test
	void getExtensionOnly() throws URISyntaxException
	{
		String actual;
		String expected;
		actual = FileExtension.PROPERTIES.getExtensionOnly();
		expected = "properties";
		assertEquals(expected, actual);
	}

	@Test
	void is() throws URISyntaxException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		assertTrue(FileExtension.is(file, FileExtension.PROPERTIES));
	}

	@Test
	void isKnown() throws URISyntaxException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		assertTrue(FileExtension.isKnown(file));

		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File notKnown = new File(tmpDir, "foo.xxx");

		assertFalse(FileExtension.isKnown(notKnown));
	}

	@Test
	void isWithString() throws URISyntaxException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);

		assertTrue(FileExtension.is(file, ".properties"));
		assertTrue(FileExtension.is(file, "properties"));
	}

	@Test
	void getZipExtensions()
	{
		Set<String> actual;
		Set<String> expected;
		actual = FileExtension.getZipExtensions();
		expected = new HashSet<>(Arrays.asList(FileConstants.ZIP_EXTENSIONS));
		assertEquals(expected, actual);
	}
}
