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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.BaseTestCase;
import io.github.astrapi69.date.CreateDateExtensions;
import io.github.astrapi69.test.objects.Person;

/**
 * The unit test class for the class {@link SerializedObjectExtensions}
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class SerializedObjectExtensionsTest extends BaseTestCase
{

	/**
	 * Test method for {@link SerializedObjectExtensions#readSerializedObjectFromFile(File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	@Test
	public void testReadSerializedObjectFromFile() throws IOException, ClassNotFoundException
	{
		final Date birthdayFromNiko = CreateDateExtensions.newDate(2007, 11, 8);
		final File writeInMe = new File(".", "testWriteSerializedObjectToFile.dat");
		actual = SerializedObjectExtensions.writeSerializedObjectToFile(birthdayFromNiko,
			writeInMe);
		assertTrue(actual);
		final Object readedObjectFromFile = SerializedObjectExtensions
			.readSerializedObjectFromFile(writeInMe);
		final Date readedObj = (Date)readedObjectFromFile;
		actual = birthdayFromNiko.equals(readedObj);
		assertTrue(actual);
		try
		{
			writeInMe.deleteOnExit();
		}
		catch (final Exception e)
		{
			// ignore...
		}
	}

	/**
	 * Test method for {@link SerializedObjectExtensions#toByteArray(Serializable)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	@Test
	public void testToByteArray() throws IOException, ClassNotFoundException
	{
		Person expected;
		Person actual;
		expected = Person.builder().name("Albert").about("science is cool").build();
		byte[] byteArray = SerializedObjectExtensions.toByteArray(expected);
		assertNotNull(byteArray);
		actual = (Person)SerializedObjectExtensions.toObject(byteArray);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link SerializedObjectExtensions#toObject(byte[])}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	@Test
	public void testToObject() throws ClassNotFoundException, IOException
	{
		String expected;
		String actual;
		final byte[] byteArray = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		expected = "Foo bar";
		actual = (String)SerializedObjectExtensions.toObject(byteArray);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link SerializedObjectExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SerializedObjectExtensions.class);
	}

	/**
	 * Test method for {@link SerializedObjectExtensions#writeSerializedObjectToFile(Object, File)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	@Test
	public void testWriteSerializedObjectToFile() throws IOException, ClassNotFoundException
	{

		final Date birthdayFromNiko = CreateDateExtensions.newDate(2007, 11, 8);
		final File writeInMe = new File(".", "testWriteSerializedObjectToFile.dat");
		actual = SerializedObjectExtensions.writeSerializedObjectToFile(birthdayFromNiko,
			writeInMe);
		assertTrue(actual);
		final Object readedObjectFromFile = SerializedObjectExtensions
			.readSerializedObjectFromFile(writeInMe);
		final Date readedObj = (Date)readedObjectFromFile;
		actual = birthdayFromNiko.equals(readedObj);
		assertTrue(actual);

	}

}
