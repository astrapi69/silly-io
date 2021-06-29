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
package io.github.astrapi69.io.annotations;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class {@link FindClassExtensions} provides methods for resolve classes
 *
 * @author Asterios Raptis
 */
public class FindClassExtensions
{


	/**
	 * Look up the class in the "current" ClassLoader.
	 *
	 * @param className
	 *            The class name to load
	 * @return the class
	 * @throws ClassNotFoundException
	 *             is thrown if the Class was not found or could not be located.
	 */
	public static Class<?> forName(final String className) throws ClassNotFoundException
	{
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(className);
		}
		catch (final Throwable throwable)
		{
			clazz = Class.forName(className, true, ClassloaderExtensions.getClassLoader());
			if (clazz == null)
			{
				clazz = Class.forName(className, false, ClassloaderExtensions.getClassLoader());
				if (clazz == null)
				{
					throw throwable;
				}
			}
		}
		return clazz;
	}

	/**
	 * Gets the directories from the given path.
	 *
	 * @param path
	 *            the path
	 * @param isPackage
	 *            If the Flag is true than the given path is a package.
	 * @return the directories from resources
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static List<File> getDirectoriesFromResources(String path,
		final boolean isPackage) throws IOException, URISyntaxException
	{
		if (isPackage)
		{
			path = path.replace('.', '/');
		}
		final List<URL> resources = getResources(path);
		final List<File> dirs = new ArrayList<>();
		for (final URL resource : resources)
		{
			dirs.add(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
		}
		return dirs;
	}

	/**
	 * Gets a list with urls from the given path for all resources.
	 *
	 * @param path
	 *            The base path.
	 * @return The resources.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<URL> getResources(final String path) throws IOException
	{
		final ClassLoader classLoader = ClassloaderExtensions.getClassLoader();
		final List<URL> list = Collections.list(classLoader.getResources(path));
		return list;
	}
}
