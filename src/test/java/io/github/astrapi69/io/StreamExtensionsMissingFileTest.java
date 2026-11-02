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
package io.github.astrapi69.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * What each overload of {@link StreamExtensions} does with a file that does not exist, and with a
 * file whose parent directory does not exist (#56). An expected exception of {@code null} means
 * that the overload creates the file. The files are created in a temporary directory
 */
class StreamExtensionsMissingFileTest
{

	@TempDir
	Path directory;

	private static Arguments overload(final String name, final Opener opener,
		final Class<? extends IOException> expected)
	{
		return arguments(named(name, opener),
			expected == null ? null : named(expected.getSimpleName(), expected));
	}

	static Stream<Arguments> inputOverloads()
	{
		return Stream.of(
			overload("getInputStream(File)", file -> StreamExtensions.getInputStream(file.toFile()),
				FileNotFoundException.class),
			overload("getInputStream(String)",
				file -> StreamExtensions.getInputStream(file.toString()),
				NoSuchFileException.class),
			overload("getInputStream(Path)", StreamExtensions::getInputStream,
				NoSuchFileException.class),
			overload("getReader(File)", file -> StreamExtensions.getReader(file.toFile()),
				FileNotFoundException.class));
	}

	static Stream<Arguments> outputOverloadsOnAMissingFile()
	{
		return Stream.of(overload("getOutputStream(File)",
			file -> StreamExtensions.getOutputStream(file.toFile()), FileNotFoundException.class),
			overload("getOutputStream(String)",
				file -> StreamExtensions.getOutputStream(file.toString()), null),
			overload("getOutputStream(Path)", StreamExtensions::getOutputStream, null),
			overload("getWriter(File)", file -> StreamExtensions.getWriter(file.toFile()),
				FileNotFoundException.class));
	}

	static Stream<Arguments> outputOverloadsInAMissingDirectory()
	{
		return Stream.of(overload("getOutputStream(File)",
			file -> StreamExtensions.getOutputStream(file.toFile()), FileNotFoundException.class),
			overload("getOutputStream(String)",
				file -> StreamExtensions.getOutputStream(file.toString()),
				NoSuchFileException.class),
			overload("getOutputStream(Path)", StreamExtensions::getOutputStream,
				NoSuchFileException.class),
			overload("getWriter(File)", file -> StreamExtensions.getWriter(file.toFile()),
				FileNotFoundException.class));
	}

	/**
	 * The overloads that take a flag whether to create a missing file do what the flag says
	 *
	 * @return the overloads with their flag
	 */
	static Stream<Arguments> overloadsWithACreateFlag()
	{
		return Stream.of(
			overload("getInputStream(File, false)",
				file -> StreamExtensions.getInputStream(file.toFile(), false),
				FileNotFoundException.class),
			overload("getInputStream(File, true)",
				file -> StreamExtensions.getInputStream(file.toFile(), true), null),
			overload("getOutputStream(File, false)",
				file -> StreamExtensions.getOutputStream(file.toFile(), false),
				FileNotFoundException.class),
			overload("getOutputStream(File, true)",
				file -> StreamExtensions.getOutputStream(file.toFile(), true), null),
			overload("getReader(File, null, false)",
				file -> StreamExtensions.getReader(file.toFile(), null, false),
				FileNotFoundException.class),
			overload("getReader(File, null, true)",
				file -> StreamExtensions.getReader(file.toFile(), null, true), null),
			overload("getWriter(File, null, false)",
				file -> StreamExtensions.getWriter(file.toFile(), null, false),
				FileNotFoundException.class),
			overload("getWriter(File, null, true)",
				file -> StreamExtensions.getWriter(file.toFile(), null, true), null));
	}

	/**
	 * Opens the given file through the given overload, and checks that it either throws exactly the
	 * expected exception and leaves no file behind, or creates the file
	 */
	private static void assertOpening(final Opener opener, final Path file,
		final Class<? extends IOException> expected) throws IOException
	{
		if (expected == null)
		{
			opener.open(file).close();
			assertTrue(Files.isRegularFile(file), () -> file + " was not created");
		}
		else
		{
			final IOException thrown = assertThrows(IOException.class, () -> opener.open(file));
			assertEquals(expected, thrown.getClass(), thrown::toString);
			assertFalse(Files.exists(file), () -> file + " was created");
		}
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("inputOverloads")
	void anInputOverload_onAMissingFile(final Opener opener,
		final Class<? extends IOException> expected) throws IOException
	{
		assertOpening(opener, directory.resolve("missing.txt"), expected);
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("outputOverloadsOnAMissingFile")
	void anOutputOverload_onAMissingFile(final Opener opener,
		final Class<? extends IOException> expected) throws IOException
	{
		assertOpening(opener, directory.resolve("missing.txt"), expected);
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("outputOverloadsInAMissingDirectory")
	void anOutputOverload_inAMissingDirectory(final Opener opener,
		final Class<? extends IOException> expected) throws IOException
	{
		assertOpening(opener, directory.resolve("missing").resolve("file.txt"), expected);
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("overloadsWithACreateFlag")
	void anOverloadWithACreateFlag_onAMissingFile(final Opener opener,
		final Class<? extends IOException> expected) throws IOException
	{
		assertOpening(opener, directory.resolve("missing.txt"), expected);
	}

	/**
	 * Opens a stream, reader or writer on a file through one of the overloads
	 */
	@FunctionalInterface
	interface Opener
	{
		/**
		 * Opens the given file
		 *
		 * @param file
		 *            the file
		 * @return what the overload opened
		 * @throws IOException
		 *             if the overload throws one
		 */
		Closeable open(Path file) throws IOException;
	}
}
