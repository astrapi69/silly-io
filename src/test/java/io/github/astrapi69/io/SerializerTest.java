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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.NotSerializableException;
import java.io.Serial;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.file.Path;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.date.CreateDateExtensions;
import io.github.astrapi69.test.base.BaseTestCase;
import io.github.astrapi69.test.object.Person;

/**
 * The unit test class for the class {@link Serializer}
 */
public class SerializerTest extends BaseTestCase
{

	@TempDir
	static Path directory;

	/**
	 * Test method for {@link Serializer#toObject(String)}
	 */
	@Test
	public void testToObjectFromBase64EncodedString()
	{
		Person expected;
		Person actual;
		String base64EncodedString;

		base64EncodedString = "rO0ABXNyACZpby5naXRodWIuYXN0cmFwaTY5LnRlc3Qub2JqZWN0LlBlcnNvbgAAAAAAAAABAgAFTAAFYWJvdXR0ABJMamF2YS9sYW5nL1N0cmluZztMAAZnZW5kZXJ0ADRMaW8vZ2l0aHViL2FzdHJhcGk2OS90ZXN0L29iamVjdC9lbnVtZXJhdGlvbi9HZW5kZXI7TAAHbWFycmllZHQAE0xqYXZhL2xhbmcvQm9vbGVhbjtMAARuYW1lcQB+AAFMAAhuaWNrbmFtZXEAfgABeHB0AA9zY2llbmNlIGlzIGNvb2x+cgAyaW8uZ2l0aHViLmFzdHJhcGk2OS50ZXN0Lm9iamVjdC5lbnVtZXJhdGlvbi5HZW5kZXIAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAlVTkRFRklORURzcgARamF2YS5sYW5nLkJvb2xlYW7NIHKA1Zz67gIAAVoABXZhbHVleHAAdAAGQWxiZXJ0dAAA";

		expected = Person.builder().name("Albert").about("science is cool").build();
		actual = (Person)Serializer.toObject(base64EncodedString);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Serializer#toBase64EncodedString(Serializable)}
	 */
	@Test
	public void testToBase64EncodedString()
	{
		String expected;
		String actual;
		Person person;
		expected = "rO0ABXNyACZpby5naXRodWIuYXN0cmFwaTY5LnRlc3Qub2JqZWN0LlBlcnNvbgAAAAAAAAABAgAFTAAFYWJvdXR0ABJMamF2YS9sYW5nL1N0cmluZztMAAZnZW5kZXJ0ADRMaW8vZ2l0aHViL2FzdHJhcGk2OS90ZXN0L29iamVjdC9lbnVtZXJhdGlvbi9HZW5kZXI7TAAHbWFycmllZHQAE0xqYXZhL2xhbmcvQm9vbGVhbjtMAARuYW1lcQB+AAFMAAhuaWNrbmFtZXEAfgABeHB0AA9zY2llbmNlIGlzIGNvb2x+cgAyaW8uZ2l0aHViLmFzdHJhcGk2OS50ZXN0Lm9iamVjdC5lbnVtZXJhdGlvbi5HZW5kZXIAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAlVTkRFRklORURzcgARamF2YS5sYW5nLkJvb2xlYW7NIHKA1Zz67gIAAVoABXZhbHVleHAAdAAGQWxiZXJ0dAAA";

		person = Person.builder().name("Albert").about("science is cool").build();
		actual = Serializer.toBase64EncodedString(person);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Serializer#readSerializedObjectFromFile(File)}
	 */
	@Test
	public void testReadSerializedObjectFromFile()
	{
		final Date birthdayFromNiko = CreateDateExtensions.newDate(2007, 11, 8);
		final File writeInMe = new File(directory.toFile(), "testWriteSerializedObjectToFile.dat");
		Serializer.writeSerializedObjectToFile(birthdayFromNiko, writeInMe);
		final Object readedObjectFromFile = Serializer.readSerializedObjectFromFile(writeInMe);
		final Date readedObj = (Date)readedObjectFromFile;
		actual = birthdayFromNiko.equals(readedObj);
		assertTrue(actual);
	}

	/**
	 * Test method for {@link Serializer#toByteArray(Serializable)}
	 */
	@Test
	public void testToByteArray()
	{
		Person expected;
		Person actual;
		expected = Person.builder().name("Albert").about("science is cool").build();
		byte[] byteArray = Serializer.toByteArray(expected);
		assertNotNull(byteArray);
		actual = (Person)Serializer.toObject(byteArray);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Serializer#toObject(byte[])}
	 */
	@Test
	public void testToObject()
	{
		String expected;
		String actual;
		final byte[] byteArray = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		expected = "Foo bar";
		actual = (String)Serializer.toObject(byteArray);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Serializer#writeSerializedObjectToFile(Object, File)}
	 */
	@Test
	public void testWriteSerializedObjectToFile()
	{
		final Date birthdayFromNiko = CreateDateExtensions.newDate(2007, 11, 8);
		final File writeInMe = new File(directory.toFile(), "testWriteSerializedObjectToFile.dat");
		Serializer.writeSerializedObjectToFile(birthdayFromNiko, writeInMe);
		final Object readedObjectFromFile = Serializer.readSerializedObjectFromFile(writeInMe);
		final Date readedObj = (Date)readedObjectFromFile;
		actual = birthdayFromNiko.equals(readedObj);
		assertTrue(actual);
	}

	/**
	 * Test method for {@link Serializer}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Serializer.class);
	}

	static Stream<Arguments> failingCalls()
	{
		final Executable readMissingFile = () -> Serializer
			.readSerializedObjectFromFile(directory.resolve("missing.ser").toFile());
		final Executable writeIntoADirectory = () -> Serializer.writeSerializedObjectToFile("text",
			directory.toFile());
		final Executable writeAnObjectThatIsNotSerializable = () -> Serializer
			.writeSerializedObjectToFile(new Object(), directory.resolve("object.ser").toFile());
		final Executable serializeNotSerializableContent = () -> Serializer
			.toByteArray(new NotSerializableContent());
		final Executable encodeNotSerializableContent = () -> Serializer
			.toBase64EncodedString(new NotSerializableContent());
		final Executable readBytesThatAreNoStream = () -> Serializer
			.toObject(new byte[] { 1, 2, 3, 4 });
		final Executable readBase64ThatIsNoStream = () -> Serializer.toObject("AQIDBA==");
		final Executable readTextThatIsNoBase64 = () -> Serializer.toObject("not base64!");
		final Executable readAnEmptyByteArray = () -> Serializer.toObject(new byte[0]);
		final Executable readAnEmptyBase64String = () -> Serializer.toObject("");
		return Stream.of(
			arguments(named("reading a file that does not exist", readMissingFile),
				FileNotFoundException.class),
			arguments(named("writing into a directory", writeIntoADirectory),
				FileNotFoundException.class),
			arguments(named("writing an object that is not serializable",
				writeAnObjectThatIsNotSerializable), NotSerializableException.class),
			arguments(named("serializing content that is not serializable",
				serializeNotSerializableContent), NotSerializableException.class),
			arguments(
				named("encoding content that is not serializable", encodeNotSerializableContent),
				NotSerializableException.class),
			arguments(named("reading bytes that are no object stream", readBytesThatAreNoStream),
				StreamCorruptedException.class),
			arguments(named("reading Base64 that is no object stream", readBase64ThatIsNoStream),
				StreamCorruptedException.class),
			arguments(named("reading text that is no Base64", readTextThatIsNoBase64),
				IllegalArgumentException.class),
			arguments(named("reading an empty byte array", readAnEmptyByteArray),
				EOFException.class),
			arguments(named("reading an empty Base64 string", readAnEmptyBase64String),
				EOFException.class));
	}

	/**
	 * Every method of {@link Serializer} turns the exception of {@link SerializedObjectExtensions}
	 * into a {@link RuntimeException} that carries it as its cause
	 *
	 * @param call
	 *            the call that fails
	 * @param expectedCause
	 *            the type of the exception the call fails with
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("failingCalls")
	void everyMethod_wrapsTheFailureIntoARuntimeException(final Executable call,
		final Class<? extends Exception> expectedCause)
	{
		final RuntimeException exception = assertThrows(RuntimeException.class, call);

		assertEquals(RuntimeException.class, exception.getClass());
		assertInstanceOf(expectedCause, exception.getCause());
	}

	/**
	 * A serializable class whose content is not serializable
	 */
	private static final class NotSerializableContent implements Serializable
	{
		@Serial
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unused")
		private final Object content = new Object();
	}
}
