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
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import io.github.astrapi69.io.file.FileExtension;
import io.github.astrapi69.io.file.FilenameExtensions;
import io.github.astrapi69.io.file.filter.ClassFileFilter;

/**
 * The class {@link AnnotationExtensions} provides methods for scan and resolve annotations in
 * classes and interfaces.
 *
 * @author Asterios Raptis
 */
public final class AnnotationExtensions
{

	/**
	 * Gets all annotated classes that belongs from the given package path and the given annotation
	 * class, from the class directories and the archives of the class path.
	 *
	 * @param packagePath
	 *            the package path
	 * @param annotationClass
	 *            the annotation class
	 * @return the all classes
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static Set<Class<?>> getAllAnnotatedClasses(final String packagePath,
		final Class<? extends Annotation> annotationClass)
		throws ClassNotFoundException, IOException, URISyntaxException
	{
		final List<File> directories = FindClassExtensions.getDirectoriesFromResources(packagePath,
			true);
		final Set<Class<?>> classes = new HashSet<>();
		for (final File directory : directories)
		{
			classes.addAll(scanForAnnotatedClasses(directory, packagePath, annotationClass));
		}
		for (final Class<?> archiveClass : scanArchivesForClasses(packagePath))
		{
			if (annotationClass == null || archiveClass.isAnnotationPresent(annotationClass))
			{
				classes.add(archiveClass);
			}
		}
		return classes;
	}

	/**
	 * Gets all annotated classes that belongs from the given package path and the given list with
	 * annotation classes, from the class directories and the archives of the class path.
	 *
	 * @param packagePath
	 *            the package path
	 * @param annotationClasses
	 *            the list with the annotation classes
	 * @return the all classes
	 *
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static Set<Class<?>> getAllAnnotatedClassesFromSet(final String packagePath,
		final Set<Class<? extends Annotation>> annotationClasses)
		throws ClassNotFoundException, IOException, URISyntaxException
	{
		final List<File> directories = FindClassExtensions.getDirectoriesFromResources(packagePath,
			true);
		final Set<Class<?>> classes = new HashSet<>();
		for (final File directory : directories)
		{
			classes
				.addAll(scanForAnnotatedClassesFromSet(directory, packagePath, annotationClasses));
		}
		for (final Class<?> archiveClass : scanArchivesForClasses(packagePath))
		{
			resolveAnnotatedClasses(annotationClasses, classes, archiveClass);
		}
		return classes;
	}

	/**
	 * Loads the classes of the given package and of its sub packages from every archive of the
	 * class path that holds the package. Every class file counts, the ones of nested classes too,
	 * as the scan of a class directory reads them
	 *
	 * @param packagePath
	 *            the package path
	 * @return the classes found in the archives
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static Set<Class<?>> scanArchivesForClasses(final String packagePath)
		throws ClassNotFoundException, IOException
	{
		final String packageEntry = packagePath.replace('.', '/');
		final Set<Class<?>> archiveClasses = new HashSet<>();
		for (final URL resource : FindClassExtensions.getResources(packageEntry))
		{
			if (FindClassExtensions.isArchive(resource))
			{
				for (final String className : archiveClassNames(resource, packageEntry + "/"))
				{
					archiveClasses.add(loadClass(className));
				}
			}
		}
		return archiveClasses;
	}

	/**
	 * Lists the binary names of the classes whose class files lie below the given entry prefix of
	 * the archive the given URL points into
	 *
	 * @param archiveEntryUrl
	 *            the URL of the package in a jar, a war or an ear
	 * @param entryPrefix
	 *            the path of the package in the archive, with its trailing slash
	 * @return the binary names of the classes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static Set<String> archiveClassNames(final URL archiveEntryUrl,
		final String entryPrefix) throws IOException
	{
		final String classFileSuffix = FileExtension.CLASS.getExtension();
		try (JarFile jarFile = new JarFile(FindClassExtensions.toArchiveFile(archiveEntryUrl)))
		{
			return jarFile.stream().map(JarEntry::getName)
				.filter(entryName -> entryName.startsWith(entryPrefix)
					&& entryName.endsWith(classFileSuffix))
				.map(entryName -> FilenameExtensions.getFilenameWithoutExtension(entryName)
					.replace('/', '.'))
				.collect(Collectors.toSet());
		}
	}

	/**
	 * Loads the class of the given name as the scan of a class directory does: initialized, or
	 * without initializing it when its initialization fails
	 *
	 * @param qualifiedClassname
	 *            the binary name of the class
	 * @return the class
	 * @throws ClassNotFoundException
	 *             occurs if the class cannot be located by the specified class loader
	 */
	private static Class<?> loadClass(final String qualifiedClassname) throws ClassNotFoundException
	{
		try
		{
			return FindClassExtensions.forName(qualifiedClassname);
		}
		catch (final LinkageError linkageError)
		{
			return Class.forName(qualifiedClassname, false, ClassloaderExtensions.getClassLoader());
		}
	}

	/**
	 * Gets all the classes from the class loader that belongs to the given package path.
	 *
	 * @param packagePath
	 *            the package path
	 *
	 * @return the all classes
	 *
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static Set<Class<?>> getAllClasses(final String packagePath)
		throws ClassNotFoundException, IOException, URISyntaxException
	{
		return getAllAnnotatedClasses(packagePath, null);
	}

	/**
	 * Gets all the classes from the class loader that belongs to the given package path.
	 *
	 * @param packagePath
	 *            the package path
	 * @param annotationClasses
	 *            the annotation classes
	 * @return the all classes
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static Set<Class<?>> getAllClasses(final String packagePath,
		final Set<Class<? extends Annotation>> annotationClasses)
		throws ClassNotFoundException, IOException, URISyntaxException
	{
		return getAllAnnotatedClassesFromSet(packagePath, annotationClasses);
	}

	/**
	 * Search for the given annotationClass in the given componentClass and return it if search was
	 * successful.
	 *
	 * @param <T>
	 *            the generic type
	 * @param componentClass
	 *            the component class
	 * @param annotationClass
	 *            the annotation class
	 * @return the annotation
	 */
	public static <T extends Annotation> T getAnnotation(final Class<?> componentClass,
		final Class<T> annotationClass)
	{
		T annotation = componentClass.getAnnotation(annotationClass);
		if (annotation != null)
		{
			return annotation;
		}
		for (final Class<?> ifc : componentClass.getInterfaces())
		{
			annotation = getAnnotation(ifc, annotationClass);
			if (annotation != null)
			{
				return annotation;
			}
		}
		if (!Annotation.class.isAssignableFrom(componentClass))
		{
			for (final Annotation ann : componentClass.getAnnotations())
			{
				annotation = getAnnotation(ann.annotationType(), annotationClass);
				if (annotation != null)
				{
					return annotation;
				}
			}
		}
		final Class<?> superClass = componentClass.getSuperclass();
		if (superClass == null || superClass.equals(Object.class))
		{
			return null;
		}
		return getAnnotation(superClass, annotationClass);
	}

	/**
	 * Checks if is annotation present through making a lookup if the given annotation class is
	 * present in the given class or in one of the super classes.
	 *
	 * @param componentClass
	 *            the component class
	 * @param annotationClass
	 *            the annotation class
	 * @return true, if is annotation present
	 */
	public static boolean isAnnotationPresentInSuperClasses(final Class<?> componentClass,
		final Class<? extends Annotation> annotationClass)
	{
		if (componentClass.isAnnotationPresent(annotationClass))
		{
			return true;
		}
		Class<?> superClass = componentClass.getSuperclass();
		while (superClass != null)
		{
			if (superClass.isAnnotationPresent(annotationClass))
			{
				return true;
			}
			superClass = superClass.getSuperclass();
		}
		return false;
	}

	/**
	 * Checks if is annotation present through making a lookup if the given annotation class is
	 * present in the given class or in one of the super classes.
	 *
	 * @param componentClass
	 *            the component class
	 * @param annotationClass
	 *            the annotation class
	 * @return true, if is annotation present
	 */
	public static boolean isAnnotationPresentInSuperClassesOrInterfaces(
		final Class<?> componentClass, final Class<? extends Annotation> annotationClass)
	{
		return getAnnotation(componentClass, annotationClass) != null;
	}

	/**
	 * Scan recursive for annotated classes in the given directory.
	 *
	 * @param directory
	 *            the directory
	 * @param packagePath
	 *            the package path
	 * @param annotationClass
	 *            the annotation class
	 * @return the list
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 */
	public static Set<Class<?>> scanForAnnotatedClasses(final File directory,
		final String packagePath, final Class<? extends Annotation> annotationClass)
		throws ClassNotFoundException
	{
		final Set<Class<?>> foundClasses = new HashSet<>();
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
			if (file.isDirectory())
			{
				qualifiedClassname = packagePath + "." + file.getName();
				foundClasses
					.addAll(scanForAnnotatedClasses(file, qualifiedClassname, annotationClass));
			}
			else
			{
				final String filename = FilenameExtensions.getFilenameWithoutExtension(file);
				qualifiedClassname = packagePath + '.' + filename;
				Class<?> foundClass = null;
				try
				{
					foundClass = FindClassExtensions.forName(qualifiedClassname);
					if (annotationClass != null)
					{
						if (foundClass.isAnnotationPresent(annotationClass))
						{
							foundClasses.add(foundClass);
						}
					}
					else
					{
						foundClasses.add(foundClass);
					}

				}
				catch (final Throwable throwable)
				{
					foundClass = Class.forName(qualifiedClassname, false,
						ClassloaderExtensions.getClassLoader());
					if (annotationClass != null)
					{
						if (foundClass.isAnnotationPresent(annotationClass))
						{
							foundClasses.add(foundClass);
						}
					}
					else
					{
						foundClasses.add(foundClass);
					}
				}
			}
		}
		return foundClasses;
	}

	/**
	 * Scan recursive for annotated classes in the given directory.
	 *
	 * @param directory
	 *            the directory
	 * @param packagePath
	 *            the package path
	 * @param annotationClasses
	 *            the list with the annotation classes
	 * @return the list
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 */
	public static Set<Class<?>> scanForAnnotatedClassesFromSet(final File directory,
		final String packagePath, final Set<Class<? extends Annotation>> annotationClasses)
		throws ClassNotFoundException
	{
		final Set<Class<?>> foundClasses = new HashSet<>();
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
			if (file.isDirectory())
			{
				qualifiedClassname = packagePath + "." + file.getName();
				foundClasses.addAll(
					scanForAnnotatedClassesFromSet(file, qualifiedClassname, annotationClasses));
			}
			else
			{
				final String filename = FilenameExtensions.getFilenameWithoutExtension(file);
				qualifiedClassname = packagePath + '.' + filename;
				Class<?> foundClass = null;
				try
				{
					foundClass = Class.forName(qualifiedClassname);
					resolveAnnotatedClasses(annotationClasses, foundClasses, foundClass);
				}
				catch (final Throwable throwable)
				{
					foundClass = Class.forName(qualifiedClassname, false,
						ClassloaderExtensions.getClassLoader());
					resolveAnnotatedClasses(annotationClasses, foundClasses, foundClass);
				}
			}
		}
		return foundClasses;
	}

	private static void resolveAnnotatedClasses(Set<Class<? extends Annotation>> annotationClasses,
		Set<Class<?>> foundClasses, Class<?> foundClass)
	{
		if (null != annotationClasses)
		{
			for (final Class<? extends Annotation> annotationClass : annotationClasses)
			{
				if (foundClass.isAnnotationPresent(annotationClass))
				{
					foundClasses.add(foundClass);
				}
			}
		}
		else
		{
			foundClasses.add(foundClass);
		}
	}

	/**
	 * Scan recursive for classes in the given directory.
	 *
	 * @param directory
	 *            the directory
	 * @param packagePath
	 *            the package path
	 * @return the list
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 */
	public static Set<Class<?>> scanForClasses(final File directory, final String packagePath)
		throws ClassNotFoundException
	{
		return AnnotationExtensions.scanForAnnotatedClasses(directory, packagePath, null);
	}

}
