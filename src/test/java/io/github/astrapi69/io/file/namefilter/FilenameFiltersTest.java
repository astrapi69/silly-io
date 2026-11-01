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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Every factory method and constructor of the {@link FilenameFilter} classes builds a filter that
 * accepts the names it is given, rejects the others, and accepts a directory only when it is
 * recursive. The files are created in a temporary directory
 */
class FilenameFiltersTest
{

	@TempDir
	Path directory;

	static Stream<Arguments> filenameFilters()
	{
		final List<String> extensions = List.of(".txt", ".xml");
		return Stream.of(
			arguments(named("SimpleFilenameFilter.of(suffix)", SimpleFilenameFilter.of(".txt")),
				"notes.txt", "notes.xml", false),
			arguments(named("SimpleFilenameFilter.of(suffix, true)",
				SimpleFilenameFilter.of(".txt", true)), "notes.txt", "notes.xml", true),
			arguments(named("new SimpleFilenameFilter(suffix)", new SimpleFilenameFilter(".txt")),
				"notes.txt", "notes.xml", false),
			arguments(
				named("MultiplyExtensionsFilenameFilter.of(collection)",
					MultiplyExtensionsFilenameFilter.of(extensions)),
				"notes.xml", "notes.bin", false),
			arguments(
				named("MultiplyExtensionsFilenameFilter.of(collection, true)",
					MultiplyExtensionsFilenameFilter.of(extensions, true)),
				"notes.txt", "notes.bin", true));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("filenameFilters")
	void accept_takesTheNamedFiles_andDirectoriesOnlyWhenRecursive(
		final FilenameFilter filenameFilter, final String acceptedName, final String rejectedName,
		final boolean acceptsDirectories) throws IOException
	{
		Files.createFile(directory.resolve(acceptedName));
		Files.createFile(directory.resolve(rejectedName));
		Files.createDirectory(directory.resolve("folder"));
		final File parent = directory.toFile();

		assertTrue(filenameFilter.accept(parent, acceptedName));
		assertFalse(filenameFilter.accept(parent, rejectedName));
		assertEquals(acceptsDirectories, filenameFilter.accept(parent, "folder"));
	}
}
