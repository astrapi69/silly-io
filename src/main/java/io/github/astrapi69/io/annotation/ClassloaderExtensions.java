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

/**
 * The class {@link ClassloaderExtensions} provides methods for resolve the current Classloader
 *
 * @author Asterios Raptis
 */
public class ClassloaderExtensions
{

	/**
	 * Gets the current class loader.
	 *
	 * @return 's the current class loader
	 */
	public static ClassLoader getClassLoader()
	{
		return getClassLoader(null);
	}

	/**
	 * Gets the ClassLoader from the given object.
	 *
	 * @param obj
	 *            The object.
	 * @return the ClassLoader from the given object.
	 */
	public static ClassLoader getClassLoader(final Object obj)
	{
		ClassLoader classLoader = null;
		if (null != obj)
		{
			if (isDerivate(Thread.currentThread().getContextClassLoader(),
				obj.getClass().getClassLoader()))
			{
				classLoader = obj.getClass().getClassLoader();
			}
			else
			{
				classLoader = Thread.currentThread().getContextClassLoader();
			}
			if (isDerivate(classLoader, ClassLoader.getSystemClassLoader()))
			{
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		else
		{
			if (isDerivate(Thread.currentThread().getContextClassLoader(),
				ClassLoader.getSystemClassLoader()))
			{
				classLoader = ClassLoader.getSystemClassLoader();
			}
			else
			{
				classLoader = Thread.currentThread().getContextClassLoader();
			}
		}
		return classLoader;
	}

	/**
	 * Compares the two given ClassLoader objects and returns true if compare is a derivate of
	 * source.
	 *
	 * @param source
	 *            the source
	 * @param compare
	 *            the compare
	 * @return true, if compare is a derivate of source.
	 */
	public static boolean isDerivate(final ClassLoader source, ClassLoader compare)
	{
		if (source == compare)
		{
			return true;
		}
		if (compare == null)
		{
			return false;
		}
		if (source == null)
		{
			return true;
		}
		while (null != compare)
		{
			compare = compare.getParent();
			if (source == compare)
			{
				return true;
			}
		}
		return false;
	}
}
