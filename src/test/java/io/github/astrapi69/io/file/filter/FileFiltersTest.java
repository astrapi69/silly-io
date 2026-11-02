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
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Named;
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

	/**
	 * A named factory of the {@link FileFilter} under test. The test calls it, not the argument
	 * source: PIT counts what an argument source runs towards the first invocation alone, so a
	 * factory called there would count as tested by an invocation that never uses its result
	 *
	 * @param name
	 *            the name of the invocation
	 * @param factory
	 *            the factory of the {@link FileFilter}
	 * @return the named factory
	 */
	private static Named<Supplier<FileFilter>> fileFilter(final String name,
		final Supplier<FileFilter> factory)
	{
		return named(name, factory);
	}

	static Stream<Arguments> fileFilters()
	{
		final List<String> extensions = List.of(".txt", ".xml");
		return Stream.of(
			arguments(fileFilter("ClassFileFilter.of()", () -> ClassFileFilter.of()), "Foo.class",
				"Foo.txt", true),
			arguments(fileFilter("ClassFileFilter.of(false)", () -> ClassFileFilter.of(false)),
				"Foo.class", "Foo.txt", false),
			arguments(fileFilter("TxtFileFilter.of()", () -> TxtFileFilter.of()), "notes.txt",
				"notes.class", true),
			arguments(fileFilter("TxtFileFilter.of(false)", () -> TxtFileFilter.of(false)),
				"notes.txt", "notes.class", false),
			arguments(fileFilter("SuffixFileFilter.of(suffix)", () -> SuffixFileFilter.of(".xml")),
				"data.xml", "data.json", true),
			arguments(fileFilter("SuffixFileFilter.of(suffix, false)",
				() -> SuffixFileFilter.of(".xml", false)), "data.xml", "data.json", false),
			arguments(
				fileFilter("new SuffixFileFilter(suffix, description, false)",
					() -> new SuffixFileFilter(".xml", "XML files", false)),
				"data.xml", "data.json", false),
			arguments(
				fileFilter("PrefixFileFilter.of(prefix)", () -> PrefixFileFilter.of("report")),
				"report-1.txt", "summary.txt", true),
			arguments(
				fileFilter("PrefixFileFilter.of(prefix, false)",
					() -> PrefixFileFilter.of("report", false)),
				"report-1.txt", "summary.txt", false),
			arguments(
				fileFilter("MultiplyExtensionsFileFilter.of(collection)",
					() -> MultiplyExtensionsFileFilter.of(extensions)),
				"notes.xml", "notes.bin", false),
			arguments(
				fileFilter("MultiplyExtensionsFileFilter.of(collection, true)",
					() -> MultiplyExtensionsFileFilter.of(extensions, true)),
				"notes.xml", "notes.bin", true),
			arguments(
				fileFilter("MultiplyExtensionsFileFilter.of(true, extensions...)",
					() -> MultiplyExtensionsFileFilter.of(true, ".txt", ".xml")),
				"notes.txt", "notes.bin", true),
			arguments(
				fileFilter("MultiplyExtensionsFileFilter.of(extensions...)",
					() -> MultiplyExtensionsFileFilter.of(".txt", ".xml")),
				"notes.txt", "notes.bin", false));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("fileFilters")
	void accept_takesTheNamedFiles_andDirectoriesOnlyWhenRecursive(
		final Supplier<FileFilter> fileFilterFactory, final String acceptedName,
		final String rejectedName, final boolean acceptsDirectories) throws IOException
	{
		final FileFilter fileFilter = fileFilterFactory.get();
		final File accepted = Files.createFile(directory.resolve(acceptedName)).toFile();
		final File rejected = Files.createFile(directory.resolve(rejectedName)).toFile();
		final File folder = Files.createDirectory(directory.resolve("folder")).toFile();

		assertTrue(fileFilter.accept(accepted));
		assertFalse(fileFilter.accept(rejected));
		assertEquals(acceptsDirectories, fileFilter.accept(folder));
	}

	static Stream<Arguments> descriptions()
	{
		return Stream.of(
			arguments(fileFilter("new ClassFileFilter()", ClassFileFilter::new),
				"[ClassFileFilter:.class]"),
			arguments(fileFilter("new TxtFileFilter()", TxtFileFilter::new),
				"[TxtFileFilter:.txt]"),
			arguments(
				fileFilter("new MultiplyExtensionsFileFilter(true, extensions...)",
					() -> new MultiplyExtensionsFileFilter(true, ".txt", ".XML")),
				"[MultiplyExtensionsFileFilter: fileExtensions: [.txt, .xml] recursive: true]"));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("descriptions")
	void toString_namesTheFilterAndWhatItAccepts(final Supplier<FileFilter> fileFilterFactory,
		final String expected)
	{
		assertEquals(expected, fileFilterFactory.get().toString());
	}

	@Test
	void getDescription_isTheGivenDescription_orEmpty()
	{
		assertEquals("XML files",
			new SuffixFileFilter(".xml", "XML files", false).getDescription());
		assertEquals("", new SuffixFileFilter(".xml", false).getDescription());
	}
}
