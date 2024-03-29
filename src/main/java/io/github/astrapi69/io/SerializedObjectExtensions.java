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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Helper-class for read from and write to serialized objects
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class SerializedObjectExtensions
{
	private SerializedObjectExtensions()
	{
	}

	/**
	 * Reads the object from the given file
	 *
	 * @param file
	 *            In that file is the object saved
	 * @return The object in the file or null
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	public static Object readSerializedObjectFromFile(final File file)
		throws IOException, ClassNotFoundException
	{
		Object object;
		try (FileInputStream fis = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fis))
		{
			object = in.readObject();
		}
		return object;
	}

	/**
	 * Copies(serialize) the given object to a byte array
	 *
	 * @param <T>
	 *            the generic type of the given object
	 * @param object
	 *            The Object to convert into a byte array
	 * @return The byte array from the Object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static <T extends Serializable> byte[] toByteArray(final T object) throws IOException
	{
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream))
		{
			objectOutputStream.writeObject(object);
			return byteArrayOutputStream.toByteArray();
		}
	}

	/**
	 * Copies(serialize) the given object to a base64 encoded {@link String} object.
	 *
	 * @param <T>
	 *            the generic type of the given object that have to extend {@link Serializable}
	 * @param object
	 *            The Object to convert into a base64 encoded {@link String} object
	 * @return The base64 encoded {@link String} object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static <T extends Serializable> String toBase64EncodedString(final T object)
		throws IOException
	{
		return Base64.getEncoder().encodeToString(toByteArray(object));
	}

	/**
	 * Transforms the given base64 encoded {@link String} object into an object
	 *
	 * @param base64EncodedString
	 *            The base64 encoded {@link String} object
	 * @return The object the was converted from the base64 encoded {@link String} object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	public static Object toObject(String base64EncodedString)
		throws IOException, ClassNotFoundException
	{
		return toObject(Base64.getDecoder().decode(base64EncodedString));
	}

	/**
	 * The Method toObject() converts the given byte array into an Object
	 *
	 * @param byteArray
	 *            The byte array to convert into an Object
	 * @return The Object the was converted from the byte array
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class is not found in the classloader or no definition for the
	 *             class with the specified name could be found
	 */
	public static Object toObject(final byte[] byteArray) throws IOException, ClassNotFoundException
	{
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream))
		{
			return objectInputStream.readObject();
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
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static boolean writeSerializedObjectToFile(final Object obj, final File file)
		throws IOException
	{
		boolean written = false;
		try (FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos))
		{
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			written = true;
		}
		return written;
	}

}
