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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.date.CreateDateExtensions;
import io.github.astrapi69.lang.ClassExtensions;
import io.github.astrapi69.test.base.BaseTestCase;
import io.github.astrapi69.test.object.Person;

/**
 * The unit test class for the class {@link StreamExtensions}.
 */
public class StreamExtensionsTest extends BaseTestCase
{
	/** The file. */
	final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

	@TempDir
	Path directory;

	/**
	 * Test get byte array input stream.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found.
	 */
	@Test
	public void testGetByteArrayInputStream() throws IOException, ClassNotFoundException
	{
		final Date birthdayFromLeonardo = CreateDateExtensions.newDate(2012, 4, 19);
		final File writeInMe = new File(directory.toFile(), "testGetByteArrayInputStream.dat");
		SerializedObjectExtensions.writeSerializedObjectToFile(birthdayFromLeonardo, writeInMe);
		final byte[] ba;
		try (InputStream is = writeInMe.toURI().toURL().openStream())
		{
			ba = StreamExtensions.getByteArray(is);
		}
		assertTrue(ba.length > 0);
		final Object obj = SerializedObjectExtensions.toObject(ba);
		final Date readedObj = (Date)obj;
		assertEquals(birthdayFromLeonardo, readedObj);
	}

	/**
	 * Test get byte array input stream byte array output stream.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             Signals that an ClassNotFoundException has occurred. if a class was not found.
	 */
	@Test
	public void testGetByteArrayInputStreamByteArrayOutputStream()
		throws IOException, ClassNotFoundException
	{
		final Date birthdayFromLeonardo = CreateDateExtensions.newDate(2012, 4, 19);
		final File writeInMe = new File(directory.toFile(),
			"testGetByteArrayInputStreamByteArrayOutputStream.dat");
		SerializedObjectExtensions.writeSerializedObjectToFile(birthdayFromLeonardo, writeInMe);
		final byte[] ba;
		try (InputStream is = writeInMe.toURI().toURL().openStream())
		{
			ba = StreamExtensions.getByteArray(is, new ByteArrayOutputStream(is.available()));
		}
		assertTrue(ba.length > 0);
		final Object obj = SerializedObjectExtensions.toObject(ba);
		final Date readedObj = (Date)obj;
		assertEquals(birthdayFromLeonardo, readedObj);
	}

	/**
	 * Test method for {@link StreamExtensions#getInputStream(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetInputStreamFile() throws IOException
	{
		final Date birthdayFromLeonardo = CreateDateExtensions.newDate(2012, 4, 19);
		final File writeInMe = new File(directory.toFile(), "testGetInputStreamFile.dat");
		SerializedObjectExtensions.writeSerializedObjectToFile(birthdayFromLeonardo, writeInMe);
		try (InputStream inputStream = StreamExtensions.getInputStream(writeInMe))
		{
			assertNotNull(inputStream);
		}
	}

	/**
	 * Test method for {@link StreamExtensions#getInputStream(File, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetInputStreamFileBoolean() throws IOException
	{
		final File writeInMe = new File(directory.toFile(), "testGetInputStreamFileBoolean.dat");

		try (InputStream inputStream = StreamExtensions.getInputStream(writeInMe, true))
		{
			assertNotNull(inputStream);
		}
	}


	/**
	 * Test method for {@link StreamExtensions#getInputStream(File, boolean)} in case of the file
	 * not exists and the flag createFile is false.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetInputStreamFileBooleanFalse() throws IOException
	{
		assertThrows(FileNotFoundException.class, () -> StreamExtensions.getInputStream(
			new File(directory.toFile(), "testGetInputStreamFileBooleanFalse.dat"), false));
	}

	/**
	 * Test method for {@link StreamExtensions#getOutputStream(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetOutputStreamFile() throws IOException
	{
		File fileout = new File(directory.toFile(), "testGetOutputStreamFile.out");
		try (OutputStream outputStream = StreamExtensions.getOutputStream(fileout, true))
		{
			assertNotNull(outputStream);
		}
	}

	/**
	 * Test method for {@link StreamExtensions#getOutputStream(File, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetOutputStreamFileBooleanFalse() throws IOException
	{
		assertThrows(FileNotFoundException.class, () -> StreamExtensions.getOutputStream(
			new File(directory.toFile(), "testGetOutputStreamFileBooleanFalse.dat"), false));
	}

	/**
	 * Test method for {@link StreamExtensions#getReader(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetReaderFile() throws IOException
	{
		File inputFile = new File(directory.toFile(), "testGetReaderFile.in");
		try (Reader reader = StreamExtensions.getReader(inputFile, null, true))
		{
			assertNotNull(reader);
		}
		try (Reader reader = StreamExtensions.getReader(inputFile, "UTF-8", true))
		{
			assertNotNull(reader);
		}
	}

	/**
	 * Test method for {@link StreamExtensions#getReader(File, String, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetReaderFileStringBooleanFalse() throws IOException
	{
		assertThrows(FileNotFoundException.class,
			() -> StreamExtensions.getReader(
				new File(directory.toFile(), "testGetReaderFileStringBooleanFalse.out"), null,
				false));
	}

	/**
	 * Test get serial version uid.
	 */
	@Test
	public void testGetSerialVersionUID()
	{
		Class<Person> personClass;

		personClass = Person.class;
		final long serialVersionUID = StreamExtensions.getSerialVersionUID(personClass);
		assertEquals(1L, serialVersionUID);
	}

	/**
	 * Test method for {@link StreamExtensions#getWriter(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws URISyntaxException
	 *             if this URL is not formatted strictly according to to RFC2396 and cannot be
	 *             converted to a URI.
	 */
	@Test
	public void testGetWriterFile() throws IOException, URISyntaxException
	{
		final URL url = ClassExtensions.getResource(propertiesFilename);
		// a copy: a writer truncates its file, and this one is on the class path
		final File properties = Files
			.copy(Path.of(url.toURI()), directory.resolve("resources.properties")).toFile();
		try (Writer writer = StreamExtensions.getWriter(properties))
		{
			assertNotNull(writer);
		}

		final File inputFile = new File(directory.toFile(), "testGetWriterFile.in");
		try (Writer writer = StreamExtensions.getWriter(inputFile, "UTF-8", true))
		{
			assertNotNull(writer);
		}
	}

	/**
	 * Test method for {@link StreamExtensions#getWriter(File, String, boolean)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetWriterFileStringBooleanFalse() throws IOException
	{
		assertThrows(FileNotFoundException.class,
			() -> StreamExtensions.getWriter(
				new File(directory.toFile(), "testGetWriterFileStringBooleanFalse.out"), null,
				false));
	}


	/**
	 * Test method for {@link StreamExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(StreamExtensions.class);
	}

	/**
	 * Test method for
	 * {@link StreamExtensions#writeInputStreamToOutputStream(InputStream, OutputStream)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found.
	 */
	@Test
	public void testWriteInputStreamToOutputStreamInputStreamOutputStream()
		throws IOException, ClassNotFoundException
	{
		final Date birthdayFromLeonardo = CreateDateExtensions.newDate(2012, 4, 19);
		final File writeInMe = new File(directory.toFile(),
			"testWriteInputStreamToOutputStreamInputStreamOutputStream.in");
		SerializedObjectExtensions.writeSerializedObjectToFile(birthdayFromLeonardo, writeInMe);
		File fileout = new File(directory.toFile(),
			"testWriteInputStreamToOutputStreamInputStreamOutputStream.out");
		try (InputStream inputStream = StreamExtensions.getInputStream(writeInMe, true);
			OutputStream outputStream = StreamExtensions.getOutputStream(fileout, true))
		{
			StreamExtensions.writeInputStreamToOutputStream(inputStream, outputStream);
		}

		final Object readedObjectFromFile = SerializedObjectExtensions
			.readSerializedObjectFromFile(fileout);
		assertEquals(readedObjectFromFile, birthdayFromLeonardo);
	}

	/**
	 * Test method for {@link StreamExtensions#toString(java.io.InputStream)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testToString() throws IOException
	{
		String expected;
		String actual;
		expected = "";
		actual = StreamExtensions.toString(null);
		assertEquals(actual, expected);
	}

	static Stream<Arguments> inputStreamOpeners()
	{
		return Stream.of(
			arguments(named("a File",
				(InputStreamOpener)file -> StreamExtensions.getInputStream(file.toFile()))),
			arguments(named("a path name",
				(InputStreamOpener)file -> StreamExtensions.getInputStream(file.toString()))),
			arguments(named("a Path", (InputStreamOpener)StreamExtensions::getInputStream)));
	}

	static Stream<Arguments> outputStreamOpeners()
	{
		return Stream.of(
			arguments(named("an existing File",
				(OutputStreamOpener)file -> StreamExtensions.getOutputStream(file.toFile()))),
			arguments(named("a path name",
				(OutputStreamOpener)file -> StreamExtensions.getOutputStream(file.toString()))),
			arguments(named("a Path", (OutputStreamOpener)StreamExtensions::getOutputStream)),
			arguments(named("an existing File, createFile false",
				(OutputStreamOpener)file -> StreamExtensions.getOutputStream(file.toFile(),
					false))),
			arguments(named("an existing File, createFile true",
				(OutputStreamOpener)file -> StreamExtensions.getOutputStream(file.toFile(),
					true))));
	}

	static Stream<Arguments> writerOpeners()
	{
		return Stream.of(
			arguments(named("getWriter(File)",
				(WriterOpener)file -> StreamExtensions.getWriter(file.toFile()))),
			arguments(named("getWriter(File, null, false)",
				(WriterOpener)file -> StreamExtensions.getWriter(file.toFile(), null, false))),
			arguments(named("getWriter(File, UTF-8, true)",
				(WriterOpener)file -> StreamExtensions.getWriter(file.toFile(), "UTF-8", true))));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("inputStreamOpeners")
	void getInputStream_readsTheBytesOfTheFile(final InputStreamOpener opener) throws IOException
	{
		final Path file = Files.write(directory.resolve("input.bin"), new byte[] { 0, 1, -2, 127 });

		try (InputStream inputStream = opener.open(file))
		{
			assertArrayEquals(new byte[] { 0, 1, -2, 127 }, inputStream.readAllBytes());
		}
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("inputStreamOpeners")
	void getInputStream_throwsAnIOException_forAFileThatDoesNotExist(final InputStreamOpener opener)
	{
		final Path missing = directory.resolve("missing.bin");

		assertThrows(IOException.class, () -> opener.open(missing).close());
		assertFalse(Files.exists(missing));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("outputStreamOpeners")
	void getOutputStream_replacesTheContentOfTheFile(final OutputStreamOpener opener)
		throws IOException
	{
		final Path file = Files.writeString(directory.resolve("output.txt"),
			"an older and longer content");

		try (OutputStream outputStream = opener.open(file))
		{
			outputStream.write("new".getBytes(StandardCharsets.UTF_8));
		}

		assertEquals("new", Files.readString(file));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("writerOpeners")
	void getWriter_replacesTheContentOfAnExistingFile(final WriterOpener opener) throws IOException
	{
		final Path file = Files.writeString(directory.resolve("output.txt"),
			"an older and longer content");

		try (Writer writer = opener.open(file))
		{
			writer.write("neu");
		}

		assertEquals("neu", Files.readString(file));
	}

	@Test
	void getByteArray_readsTheBytesOfAFile() throws IOException
	{
		final Path file = Files.write(directory.resolve("bytes.bin"), new byte[] { 9, 8, 7 });

		assertArrayEquals(new byte[] { 9, 8, 7 }, StreamExtensions.getByteArray(file.toFile()));
	}

	/**
	 * {@code getByteArray(File)} opens the stream itself, so it has to close it: once it returns,
	 * no file descriptor of this process points at the file any more. Linux lists the descriptors
	 * of the process under {@code /proc/self/fd}, so the test runs on Linux only
	 *
	 * @throws IOException
	 *             if the file cannot be written or the descriptors cannot be listed
	 */
	@Test
	@EnabledOnOs(OS.LINUX)
	void getByteArray_closesTheStreamItOpens() throws IOException
	{
		final Path descriptors = Path.of("/proc/self/fd");
		final Path file = Files.write(directory.resolve("bytes.bin"), new byte[] { 9, 8, 7 })
			.toRealPath();

		StreamExtensions.getByteArray(file.toFile());

		assertEquals(0, descriptorsOf(descriptors, file));
	}

	private static long descriptorsOf(final Path descriptors, final Path file) throws IOException
	{
		try (Stream<Path> links = Files.list(descriptors))
		{
			return links.filter(link -> {
				try
				{
					return Files.readSymbolicLink(link).equals(file);
				}
				catch (final IOException exception)
				{
					// the descriptor of the listing itself is gone once it is read
					return false;
				}
			}).count();
		}
	}

	@Test
	void getReader_readsTheTextOfAFile() throws IOException
	{
		final Path file = Files.writeString(directory.resolve("text.txt"), "first line\nsecond");

		try (Reader reader = StreamExtensions.getReader(file.toFile()))
		{
			final StringWriter content = new StringWriter();
			reader.transferTo(content);
			assertEquals("first line\nsecond", content.toString());
		}
	}

	@Test
	void writeReaderToWriter_copiesEveryCharacter() throws IOException
	{
		final StringWriter writer = new StringWriter();

		StreamExtensions.writeReaderToWriter(new StringReader("copied\ttext\n"), writer);

		assertEquals("copied\ttext\n", writer.toString());
	}

	/**
	 * Opens an input stream on a file
	 */
	interface InputStreamOpener
	{
		InputStream open(Path file) throws IOException;
	}

	/**
	 * Opens an output stream on a file
	 */
	interface OutputStreamOpener
	{
		OutputStream open(Path file) throws IOException;
	}

	/**
	 * Opens a writer on a file
	 */
	interface WriterOpener
	{
		Writer open(Path file) throws IOException;
	}
}
