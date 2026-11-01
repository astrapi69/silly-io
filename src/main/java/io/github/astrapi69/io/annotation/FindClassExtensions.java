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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
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
	 * Gets the directories from the given path, one for each root of the class path that holds it:
	 * the directory of a resource of the protocol {@code file}, and the file that the decoded URL
	 * path of a resource of another protocol names, as the virtual file system of an application
	 * server gives one. A resource inside a jar, a war or an ear gives no entry: it is no directory
	 * of the file system, so a package that lies in archives only gives an empty list.
	 *
	 * @param path
	 *            the path
	 * @param isPackage
	 *            If the Flag is true than the given path is a package.
	 * @return the directories from resources, none for a resource inside an archive
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static List<File> getDirectoriesFromResources(String path, final boolean isPackage)
		throws IOException, URISyntaxException
	{
		if (isPackage)
		{
			path = path.replace('.', '/');
		}
		final List<URL> resources = getResources(path);
		final List<File> dirs = new ArrayList<>();
		for (final URL resource : resources)
		{
			if (resource.getProtocol().equals("file"))
			{
				dirs.add(toFile(resource.toString()));
			}
			else if (!isArchive(resource))
			{
				dirs.add(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
			}
		}
		return dirs;
	}

	/**
	 * Gives the file that the given file URL names. A URL that is a valid URI, as
	 * {@link java.nio.file.Path#toUri()} and the class loaders of the JDK build it, is decoded as a
	 * URI: an escaped character is unescaped, and a {@code +} stays a plus, which form decoding
	 * would turn into a space. A URL that is no valid URI, as the deprecated {@link File#toURL()}
	 * builds one for a path with a space, holds its path unescaped and is taken as it is
	 *
	 * @param fileUrl
	 *            the file URL, of the scheme {@code file}
	 * @return the file the URL names
	 */
	static File toFile(final String fileUrl)
	{
		try
		{
			return Path.of(new URI(fileUrl)).toFile();
		}
		catch (final URISyntaxException exception)
		{
			return new File(fileUrl.substring(fileUrl.indexOf(':') + 1));
		}
	}

	/**
	 * Checks if the given URL points into an archive, a jar, a war or an ear
	 *
	 * @param url
	 *            the URL of a resource
	 * @return true if the URL points into an archive
	 */
	static boolean isArchive(final URL url)
	{
		final String protocol = url.getProtocol();
		return protocol.equals("jar") || protocol.equals("ear") || protocol.equals("war");
	}

	/**
	 * Gives the archive file that the given URL points into: the file URL before the first
	 * {@code !/}, where {@link java.net.JarURLConnection} separates the archive from the entry too,
	 * read as {@link #toFile(String)} reads it. A {@code !} without a slash after it belongs to the
	 * path of the archive
	 *
	 * @param archiveEntryUrl
	 *            the URL of an entry in a jar, a war or an ear
	 * @return the archive file
	 */
	static File toArchiveFile(final URL archiveEntryUrl)
	{
		final String entryUrl = archiveEntryUrl.getFile();
		return toFile(entryUrl.substring(0, entryUrl.indexOf("!/")));
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
