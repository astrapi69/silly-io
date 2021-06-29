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

import io.github.astrapi69.BaseTestCase;
import io.github.astrapi69.collections.array.ArrayFactory;
import io.github.astrapi69.date.CreateDateExtensions;
import io.github.astrapi69.test.objects.Person;
import org.meanbean.test.BeanTester;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The unit test class for the class {@link Serializer}
 */
public class SerializerTest extends BaseTestCase
{

	/**
	 * Test method for {@link SerializedObjectExtensions#readSerializedObjectFromFile(File)}
	 */
	@Test public void testReadSerializedObjectFromFile()
	{
		final Date birthdayFromNiko = CreateDateExtensions.newDate(2007, 11, 8);
		final File writeInMe = new File(".", "testWriteSerializedObjectToFile.dat");
		actual = Serializer.writeSerializedObjectToFile(birthdayFromNiko,
			writeInMe);
		assertTrue(actual);
		final Object readedObjectFromFile = Serializer
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
	 * Test method for {@link Serializer#toByteArray(Serializable)}
	 */
	@Test public void testToByteArray()
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
	@Test public void testToObject()
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
	@Test public void testWriteSerializedObjectToFile()
	{
		final Date birthdayFromNiko = CreateDateExtensions.newDate(2007, 11, 8);
		final File writeInMe = new File(".", "testWriteSerializedObjectToFile.dat");
		actual = Serializer.writeSerializedObjectToFile(birthdayFromNiko,
			writeInMe);
		assertTrue(actual);
		final Object readedObjectFromFile = Serializer
			.readSerializedObjectFromFile(writeInMe);
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

}
