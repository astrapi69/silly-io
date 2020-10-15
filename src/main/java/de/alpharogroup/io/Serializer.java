package de.alpharogroup.io;

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
