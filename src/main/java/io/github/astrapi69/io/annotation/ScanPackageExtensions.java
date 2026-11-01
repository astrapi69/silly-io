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
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.io.file.FilenameExtensions;
import io.github.astrapi69.io.file.filter.ClassFileFilter;

/**
 * The class {@link ScanPackageExtensions} provides extension methods for the scan packages
 */
public final class ScanPackageExtensions
{

	/**
	 * The names of the class files that hold no class: the annotations and the documentation of a
	 * package, and the declaration of a module
	 */
	private static final Set<String> NO_CLASS_NAMES = Set.of("package-info", "module-info");

	/**
	 * Scans the qualified names of the top-level classes of the given package, without its sub
	 * packages, as {@link #scanTopLevelClassNames(String, boolean, boolean)} lists them.
	 *
	 * @param packageName
	 *            the package name
	 * @return the qualified names of the top-level classes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Set<String> scanTopLevelClassNames(final String packageName) throws IOException
	{
		return scanTopLevelClassNames(packageName, false, true);
	}

	/**
	 * Scans the names of the top-level classes of the given package, in every directory and archive
	 * of the class path that holds the package. Only top-level classes are listed: a class file
	 * whose name holds a {@code $}, as the class files of nested and anonymous classes do, is
	 * skipped, and so are a {@code package-info.class} and a {@code module-info.class}, which hold
	 * no class.
	 *
	 * @param packageName
	 *            the package name
	 * @param recursive
	 *            the recursive flag
	 * @param qualifiedClassnames
	 *            the flag if the class names should be qualified class names.
	 * @return the names of the top-level classes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Set<String> scanTopLevelClassNames(final String packageName,
		final boolean recursive, final boolean qualifiedClassnames) throws IOException
	{
		final Set<String> classNames = new LinkedHashSet<>();
		final String packagePath = packageName.replace(".", "/");
		for (final URL packageURL : FindClassExtensions.getResources(packagePath))
		{
			if (FindClassExtensions.isArchive(packageURL))
			{
				classNames
					.addAll(scanArchive(packageURL, packageName, recursive, qualifiedClassnames));
			}
			else
			{
				classNames
					.addAll(scanDirectory(packageURL, packageName, recursive, qualifiedClassnames));
			}
		}
		return classNames;
	}

	/**
	 * Scans the top-level class names of the given package in the archive that the given URL points
	 * into. The archive is read once: a recursive scan takes the class files of the sub packages
	 * from the same pass over its entries, whether the archive holds directory entries for them or
	 * not
	 *
	 * @param packageURL
	 *            the URL of the package in a jar, a war or an ear
	 * @param packageName
	 *            the package name
	 * @param recursive
	 *            the recursive flag
	 * @param qualifiedClassnames
	 *            the flag if the class names should be qualified class names
	 * @return the class names found in the archive
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static Set<String> scanArchive(final URL packageURL, final String packageName,
		final boolean recursive, final boolean qualifiedClassnames) throws IOException
	{
		final Set<String> classNames = new LinkedHashSet<>();
		final String entryPrefix = packageName.replace(".", "/") + "/";
		try (JarFile jarFile = new JarFile(FindClassExtensions.toArchiveFile(packageURL)))
		{
			final Enumeration<JarEntry> jarEntries = jarFile.entries();
			while (jarEntries.hasMoreElements())
			{
				final String entryName = jarEntries.nextElement().getName();
				if (entryName.startsWith(entryPrefix)
					&& entryName.endsWith(FileExtension.CLASS.getExtension()))
				{
					final String relativePath = entryName.substring(entryPrefix.length());
					final String classFileName = relativePath
						.substring(relativePath.lastIndexOf('/') + 1);
					if ((recursive || !relativePath.contains("/"))
						&& isTopLevelClassFile(classFileName))
					{
						classNames.add(qualifiedClassnames
							? FilenameExtensions.getFilenameWithoutExtension(entryName).replace('/',
								'.')
							: FilenameExtensions.getFilenameWithoutExtension(classFileName));
					}
				}
			}
		}
		return classNames;
	}

	/**
	 * Scans the top-level class names of the given package in the class directory the given URL
	 * points to
	 *
	 * @param packageURL
	 *            the URL of the package directory
	 * @param packageName
	 *            the package name
	 * @param recursive
	 *            the recursive flag
	 * @param qualifiedClassnames
	 *            the flag if the class names should be qualified class names
	 * @return the class names found in the directory
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static Set<String> scanDirectory(final URL packageURL, final String packageName,
		final boolean recursive, final boolean qualifiedClassnames) throws IOException
	{
		final Set<String> classNames = new LinkedHashSet<>();
		final String packagePath = packageName.replace(".", "/");
		final File folder = FindClassExtensions.toFile(packageURL.toString());
		// define the include filefilter for class files...
		final FileFilter includeFileFilter = new ClassFileFilter();
		final File[] files = folder.listFiles(includeFileFilter);
		for (final File file : files)
		{
			String qualifiedClassname = null;
			if (file.isDirectory() && recursive)
			{
				qualifiedClassname = packagePath + "." + file.getName();
				classNames.addAll(
					scanTopLevelClassNames(qualifiedClassname, recursive, qualifiedClassnames));
			}
			else
			{
				if (!file.isDirectory() && isTopLevelClassFile(file.getName()))
				{
					final String filenameWithoutExtension = FilenameExtensions
						.getFilenameWithoutExtension(file);

					if (qualifiedClassnames)
					{
						qualifiedClassname = packageName + '.' + filenameWithoutExtension;
						classNames.add(qualifiedClassname.replace("/", "."));
					}
					else
					{
						classNames.add(filenameWithoutExtension);
					}
				}
			}
		}
		return classNames;
	}

	/**
	 * Checks if the class file of the given name holds a top-level class: a name with a {@code $},
	 * as the class files of nested and anonymous classes have, is none, and neither is a
	 * {@code package-info.class} or a {@code module-info.class}
	 *
	 * @param classFileName
	 *            the name of the class file, without its directory
	 * @return true if the class file holds a top-level class
	 */
	private static boolean isTopLevelClassFile(final String classFileName)
	{
		final String className = FilenameExtensions.getFilenameWithoutExtension(classFileName);
		return !className.contains("$") && !NO_CLASS_NAMES.contains(className);
	}

}
