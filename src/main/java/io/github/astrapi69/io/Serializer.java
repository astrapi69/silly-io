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

import java.io.*;

/**
 * Utility class for read from and write to serialized objects without checked exceptions
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class Serializer
{
	/**
	 * Reads the object from the given file
	 *
	 * @param file
	 *            In that file is the object saved
	 * @return The object in the file or null
	 */
	public static Object readSerializedObjectFromFile(final File file)
	{
		try
		{
			return SerializedObjectExtensions.readSerializedObjectFromFile(file);
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Copies(serialize) the given object to a byte array
	 *
	 * @param <T>
	 *            the generic type of the given object
	 * @param object
	 *            The Object to convert into a byte array
	 * @return The byte array from the Object
	 */
	public static <T extends Serializable> byte[] toByteArray(final T object)
	{
		try
		{
			return SerializedObjectExtensions.toByteArray(object);
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}

	/**
	 * The Method toObject() converts the given byte array into an Object
	 *
	 * @param byteArray
	 *            The byte array to convert into an Object
	 * @return The Object the was converted from the byte array
	 */
	public static Object toObject(final byte[] byteArray)
	{
		try
		{
			return SerializedObjectExtensions.toObject(byteArray);
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Writes the given object to the given File
	 *
	 * @param obj
	 *            The object to write to the File
	 * @param file
	 *            In this file will be the object saved
	 * @return true if the object was written to the file otherwise false
	 */
	public static boolean writeSerializedObjectToFile(final Object obj, final File file)
	{
		try
		{
			return SerializedObjectExtensions.writeSerializedObjectToFile(obj, file);
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}

}
