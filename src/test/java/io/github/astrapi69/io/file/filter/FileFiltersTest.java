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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Every factory method and constructor of the {@link FileFilter} classes builds a filter that
 * accepts the files it names, rejects the others, and accepts a directory only when it is
 * recursive. The files are created in a temporary directory
 */
class FileFiltersTest
{

	@TempDir
	Path directory;

	static Stream<Arguments> fileFilters()
	{
		final List<String> extensions = List.of(".txt", ".xml");
		return Stream.of(
			arguments(named("ClassFileFilter.of()", ClassFileFilter.of()), "Foo.class", "Foo.txt",
				true),
			arguments(named("ClassFileFilter.of(false)", ClassFileFilter.of(false)), "Foo.class",
				"Foo.txt", false),
			arguments(named("TxtFileFilter.of()", TxtFileFilter.of()), "notes.txt", "notes.class",
				true),
			arguments(named("TxtFileFilter.of(false)", TxtFileFilter.of(false)), "notes.txt",
				"notes.class", false),
			arguments(named("SuffixFileFilter.of(suffix)", SuffixFileFilter.of(".xml")), "data.xml",
				"data.json", true),
			arguments(
				named("SuffixFileFilter.of(suffix, false)", SuffixFileFilter.of(".xml", false)),
				"data.xml", "data.json", false),
			arguments(
				named("new SuffixFileFilter(suffix, description, false)",
					new SuffixFileFilter(".xml", "XML files", false)),
				"data.xml", "data.json", false),
			arguments(named("PrefixFileFilter.of(prefix)", PrefixFileFilter.of("report")),
				"report-1.txt", "summary.txt", true),
			arguments(
				named("PrefixFileFilter.of(prefix, false)", PrefixFileFilter.of("report", false)),
				"report-1.txt", "summary.txt", false),
			arguments(named("MultiplyExtensionsFileFilter.of(collection)",
				MultiplyExtensionsFileFilter.of(extensions)), "notes.xml", "notes.bin", false),
			arguments(
				named("MultiplyExtensionsFileFilter.of(collection, true)",
					MultiplyExtensionsFileFilter.of(extensions, true)),
				"notes.xml", "notes.bin", true),
			arguments(
				named("MultiplyExtensionsFileFilter.of(true, extensions...)",
					MultiplyExtensionsFileFilter.of(true, ".txt", ".xml")),
				"notes.txt", "notes.bin", true),
			arguments(
				named("MultiplyExtensionsFileFilter.of(extensions...)",
					MultiplyExtensionsFileFilter.of(".txt", ".xml")),
				"notes.txt", "notes.bin", false));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("fileFilters")
	void accept_takesTheNamedFiles_andDirectoriesOnlyWhenRecursive(final FileFilter fileFilter,
		final String acceptedName, final String rejectedName, final boolean acceptsDirectories)
		throws IOException
	{
		final File accepted = Files.createFile(directory.resolve(acceptedName)).toFile();
		final File rejected = Files.createFile(directory.resolve(rejectedName)).toFile();
		final File folder = Files.createDirectory(directory.resolve("folder")).toFile();

		assertTrue(fileFilter.accept(accepted));
		assertFalse(fileFilter.accept(rejected));
		assertEquals(acceptsDirectories, fileFilter.accept(folder));
	}

	@Test
	void getDescription_isTheGivenDescription_orEmpty()
	{
		assertEquals("XML files",
			new SuffixFileFilter(".xml", "XML files", false).getDescription());
		assertEquals("", new SuffixFileFilter(".xml", false).getDescription());
	}
}
