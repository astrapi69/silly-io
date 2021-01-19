package de.alpharogroup.io;

import de.alpharogroup.BaseTestCase;
import de.alpharogroup.date.CreateDateExtensions;
import de.alpharogroup.test.objects.Person;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

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
		assertTrue("", actual);
		final Object readedObjectFromFile = Serializer
			.readSerializedObjectFromFile(writeInMe);
		final Date readedObj = (Date)readedObjectFromFile;
		actual = birthdayFromNiko.equals(readedObj);
		assertTrue("", actual);
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
		assertTrue("", actual);
		final Object readedObjectFromFile = Serializer
			.readSerializedObjectFromFile(writeInMe);
		final Date readedObj = (Date)readedObjectFromFile;
		actual = birthdayFromNiko.equals(readedObj);
		assertTrue("", actual);
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
