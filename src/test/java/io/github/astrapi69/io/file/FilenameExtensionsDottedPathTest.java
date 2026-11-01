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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * The extension of a file is the part of its name after the last dot, whatever its directories are
 * called (astrapi69/silly-io#9): a dot in a directory name, as in {@code ~/.cache} or
 * {@code ~/.config}, is not an extension. Every path is built in a temporary directory, so the
 * result does not depend on where the build runs.
 */
class FilenameExtensionsDottedPathTest
{

	@TempDir
	Path root;

	@ParameterizedTest(name = "{0}: suffix {1}, prefix {2}")
	@CsvSource(nullValues = "null", value = {
			".cache/x/noextensionfile, null, .cache/x/noextensionfile", "a.b/c, null, a.b/c",
			"a.b/c.txt, .txt, a.b/c", ".config/app/data.tar.gz, .gz, .config/app/data.tar",
			"x.y/name., ., x.y/name", "dir.d/.bashrc, .bashrc, dir.d/" })
	void suffixAndPrefix_lookAtTheFileNameOnly(final String relative, final String suffix,
		final String prefix) throws IOException
	{
		Path file = root.resolve(relative);
		Files.createDirectories(file.getParent());
		Files.createFile(file);

		assertEquals(suffix, FilenameExtensions.getFilenameSuffix(file.toFile()));
		assertEquals(root.resolve(prefix).toString() + (prefix.endsWith("/") ? File.separator : ""),
			FilenameExtensions.getFilenamePrefix(file.toFile()));
	}

	@Test
	void suffix_isNullForADirectoryWithADotInItsName() throws IOException
	{
		Path directory = Files.createDirectories(root.resolve("a.b"));

		assertNull(FilenameExtensions.getFilenameSuffix(directory.toFile()));
	}
}
