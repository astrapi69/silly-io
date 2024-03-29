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
package io.github.astrapi69.io.annotation;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The class {@link ImportResourceComparator} compares two given {@link ImportResource} objects
 * based on the index.
 */
public class ImportResourceComparator implements Comparator<ImportResource>, Serializable
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 6972397205717174979L;

	/**
	 * Compares the given objects and returns the int value.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @param compareWithObject
	 *            the object to compare with.
	 * @return the int
	 */
	public static <T extends Comparable<T>> int compare(final T object, final T compareWithObject)
	{
		final Integer nullCheck = nullCheck(object, compareWithObject, false);
		if (nullCheck != null)
		{
			return nullCheck;
		}
		// Null check completed so we can compare the objects
		return object.compareTo(compareWithObject);
	}

	/**
	 * Checks if one of the given objects are null and returns the value for the Comparator or null
	 * if both are not null or if the given objects are not the same Object.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @param compareWithObject
	 *            the compare with object
	 * @param nullIsGreaterThan
	 *            the flag that says if null objects are greater
	 * @return the integer or null if both are not null or if the given objects are not the same
	 *         Object.
	 */
	public static <T> Integer nullCheck(final T object, final T compareWithObject,
		final boolean nullIsGreaterThan)
	{
		if (object == compareWithObject)
		{
			return 0;// it is the same Object
		}
		if (object == null)
		{
			if (nullIsGreaterThan)
			{
				return 1;
			}
			return -1; // object is null so its smaller
		}
		// Check if one of the objects are null
		if (compareWithObject == null)
		{
			if (nullIsGreaterThan)
			{
				return -1;
			}
			return 1;// compareWithObject is null so its bigger
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(final ImportResource object, final ImportResource compareWithObject)
	{
		final Integer nullCheck = nullCheck(object, compareWithObject, false);
		if (nullCheck != null)
		{
			return nullCheck;
		}
		return compare(object.index(), compareWithObject.index());
	}

}
