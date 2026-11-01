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
import java.util.LinkedHashSet;
import java.util.Set;

import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.io.file.FilenameExtensions;
import io.github.astrapi69.io.file.filter.ClassFileFilter;

/**
 * The class {@link ScanClassExtensions} provides extension methods for the scan {@link Class}
 * objects
 */
public final class ScanClassExtensions
{

	/**
	 * Equal the given class objects by they qualified class names.
	 *
	 * @param oneClass
	 *            one class for equation by the qualified class name.
	 * @param otherClass
	 *            the other class for equation by the qualified class name.
	 * @return s true if the qualified class names are equal otherwise false.
	 */
	public static boolean equalsByClassName(final Class<?> oneClass, final Class<?> otherClass)
	{
		final String oneNormalizedClassName = ScanClassExtensions
			.normalizeQualifiedClassName(oneClass.getName());
		final String otherNormalizedClassName = ScanClassExtensions
			.normalizeQualifiedClassName(otherClass.getName());
		if (otherNormalizedClassName.equals(oneNormalizedClassName))
		{
			return true;
		}
		return false;
	}

	/**
	 * Normalizes the given full qualified class name. This can be an entry from a jar file for
	 * instance.
	 *
	 * @param qualifiedClassname
	 *            the full qualified class name to normalize.
	 * @return The normalized class name.
	 */
	public static String normalizeQualifiedClassName(final String qualifiedClassname)
	{
		return normalizeSimpleClassName(qualifiedClassname).replaceAll("/", ".");
	}

	/**
	 * Normalizes the given simple class name.
	 *
	 * @param className
	 *            the class name to normalize.
	 * @return The normalized class name.
	 */
	public static String normalizeSimpleClassName(final String className)
	{
		String result = className;
		if (className.endsWith(FileExtension.CLASS.getExtension()))
		{
			result = className
				.substring(0, className.length() - FileExtension.CLASS.getExtension().length())
				.trim();
		}
		final int lastIndexOf$ = result.lastIndexOf("$");
		if (lastIndexOf$ != -1)
		{
			final String prefix = result.substring(0, lastIndexOf$);
			final String compilerClassName = result.substring(lastIndexOf$ + 1, result.length());
			if (isNumeric(compilerClassName))
			{
				return prefix;
			}
		}
		return result;
	}

	private static boolean isNumeric(final CharSequence cs)
	{
		if (cs.length() == 0)
		{
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++)
		{
			if (!Character.isDigit(cs.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Loads all classes of the given directory, without its sub directories: every class file in
	 * the directory counts, the ones of nested, local and anonymous classes included, and no
	 * {@code package-info} or {@code module-info}, which hold no class.
	 *
	 * @param directory
	 *            the directory
	 * @param packagePath
	 *            the package path
	 * @return all classes of the directory
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 */
	public static Set<Class<?>> scanAllClassesFromPackage(final File directory,
		final String packagePath) throws ClassNotFoundException
	{
		return scanAllClassesFromPackage(directory, packagePath, false);
	}

	/**
	 * Loads all classes of the given directory, and of its sub directories when recursive: every
	 * class file counts, the ones of nested, local and anonymous classes included, and no
	 * {@code package-info} or {@code module-info}, which hold no class.
	 *
	 * @param directory
	 *            the directory
	 * @param packagePath
	 *            the package path
	 * @param recursive
	 *            the recursive flag
	 * @return all classes of the directory
	 * @throws ClassNotFoundException
	 *             is thrown if a class in the given path cannot be located.
	 */
	public static Set<Class<?>> scanAllClassesFromPackage(final File directory,
		final String packagePath, final boolean recursive) throws ClassNotFoundException
	{
		final Set<Class<?>> foundClasses = new LinkedHashSet<>();
		if (!directory.exists())
		{
			return foundClasses;
		}
		// define the include filefilter for class files...
		final FileFilter includeFileFilter = new ClassFileFilter();
		final File[] files = directory.listFiles(includeFileFilter);
		for (final File file : files)
		{
			String qualifiedClassname = null;

			if (file.isDirectory() && recursive)
			{
				qualifiedClassname = packagePath + "." + file.getName();
				foundClasses.addAll(scanAllClassesFromPackage(file, qualifiedClassname, recursive));
			}
			else
			{
				if (!file.isDirectory() && ScanPackageExtensions.isTypeClassFile(file.getName()))
				{
					final String filename = FilenameExtensions.getFilenameWithoutExtension(file);
					qualifiedClassname = packagePath + '.' + filename;
					foundClasses.add(FindClassExtensions.forName(qualifiedClassname));
				}
			}
		}
		return foundClasses;
	}

	/**
	 * Loads the top-level classes of the given package, without its sub packages, as
	 * {@link ScanPackageExtensions#scanTopLevelClassNames(String, boolean, boolean)} lists them: no
	 * nested, local or anonymous class, and no {@code package-info} or {@code module-info}.
	 *
	 * @param packageName
	 *            the package name
	 * @return the top-level classes of the package
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             is thrown if a class in the given path cannot be located.
	 */
	public static Set<Class<?>> scanTopLevelClasses(final String packageName)
		throws IOException, ClassNotFoundException
	{
		return scanTopLevelClasses(packageName, false);
	}

	/**
	 * Loads the top-level classes of the given package, and of its sub packages when recursive, as
	 * {@link ScanPackageExtensions#scanTopLevelClassNames(String, boolean, boolean)} lists them: no
	 * nested, local or anonymous class, and no {@code package-info} or {@code module-info}.
	 *
	 * @param packageName
	 *            the package name
	 * @param recursive
	 *            the recursive flag
	 * @return the top-level classes of the package
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             is thrown if a class in the given path cannot be located.
	 */
	public static Set<Class<?>> scanTopLevelClasses(final String packageName,
		final boolean recursive) throws IOException, ClassNotFoundException
	{
		final Set<Class<?>> foundClasses = new LinkedHashSet<>();
		final Set<String> qualifiedClassnames = ScanPackageExtensions
			.scanTopLevelClassNames(packageName, recursive, true);
		for (final String qualifiedClassname : qualifiedClassnames)
		{
			foundClasses.add(FindClassExtensions.forName(qualifiedClassname));
		}
		return foundClasses;
	}


}
