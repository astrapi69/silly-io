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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
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
	private AnnotationExtensions()
	{
	}

	/**
	 * Gets all annotated classes that belongs from the given package path and the given annotation
	 * class, from the class directories and the archives of the class path. All classes count:
	 * every class file of the package and of its sub packages, the ones of nested, local and
	 * anonymous classes included. Without an annotation class every class is returned. A
	 * {@code package-info} and a {@code module-info} are skipped: they hold no class, and the
	 * annotations of a package are read with {@link Package#getAnnotation(Class)}.
	 * <p>
	 * A jar must contain its directory entries, which {@code jar}, Gradle and Maven write by
	 * default: the class loader finds a jar through the directory entry of the package, so the
	 * classes of a jar without one are not found. A jar below a directory whose name ends in
	 * {@code !}, as in {@code x!/lib.jar}, cannot be read: its URL is split at the first
	 * {@code !/}, as {@link java.net.JarURLConnection} splits it, which ends the path of the
	 * archive at {@code x}.
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
			classes.addAll(scanForAllAnnotatedClasses(directory, packagePath, annotationClass));
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
	 * annotation classes, from the class directories and the archives of the class path. All
	 * classes count: every class file of the package and of its sub packages, the ones of nested,
	 * local and anonymous classes included. Without an annotation set every class is returned. A
	 * {@code package-info} and a {@code module-info} are skipped: they hold no class, and the
	 * annotations of a package are read with {@link Package#getAnnotation(Class)}.
	 * <p>
	 * A jar must contain its directory entries, which {@code jar}, Gradle and Maven write by
	 * default: the class loader finds a jar through the directory entry of the package, so the
	 * classes of a jar without one are not found. A jar below a directory whose name ends in
	 * {@code !}, as in {@code x!/lib.jar}, cannot be read: its URL is split at the first
	 * {@code !/}, as {@link java.net.JarURLConnection} splits it, which ends the path of the
	 * archive at {@code x}.
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
			classes.addAll(
				scanForAllAnnotatedClassesFromSet(directory, packagePath, annotationClasses));
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
	 * and no {@code package-info} or {@code module-info}, as the scan of a class directory reads
	 * them
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
	 * the archive the given URL points into, without a {@code package-info} or a
	 * {@code module-info}
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
					&& entryName.endsWith(classFileSuffix)
					&& ScanPackageExtensions
						.isTypeClassFile(entryName.substring(entryName.lastIndexOf('/') + 1)))
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
	 * Gets all the classes from the class loader that belongs to the given package path: every
	 * class file of the package and of its sub packages, the ones of nested, local and anonymous
	 * classes included, and no {@code package-info} or {@code module-info}. A jar is read as
	 * {@link #getAllAnnotatedClasses(String, Class)} reads it.
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
	 * Gets all the classes from the class loader that belongs to the given package path and carry
	 * one of the given annotations, as {@link #getAllAnnotatedClassesFromSet(String, Set)} does:
	 * nested, local and anonymous classes included, and no {@code package-info} or
	 * {@code module-info}.
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
	 * successful. The search goes depth first and returns the first annotation it meets: the
	 * component class itself, the annotations it inherits included; then each of its interfaces
	 * with everything above it; then the types of its annotations, unless it is an annotation or
	 * implements one; then its superclass with everything above it, up to {@link Object}
	 *
	 * @param <T>
	 *            the generic type
	 * @param componentClass
	 *            the component class
	 * @param annotationClass
	 *            the annotation class
	 * @return the annotation, or null if the search does not meet it
	 * @throws NullPointerException
	 *             if the component class or the annotation class is null
	 */
	public static <T extends Annotation> T getAnnotation(final Class<?> componentClass,
		final Class<T> annotationClass)
	{
		// the types still to search, the next one on top
		final Deque<Class<?>> pending = new ArrayDeque<>();
		pending.push(componentClass);
		while (!pending.isEmpty())
		{
			final Class<?> type = pending.pop();
			final T found = type.getAnnotation(annotationClass);
			if (found != null)
			{
				return found;
			}
			final List<Class<?>> next = typesToSearchAfter(type);
			// pushed last first, so that the first of them is searched next, with all above it
			for (int index = next.size() - 1; index >= 0; index--)
			{
				pending.push(next.get(index));
			}
		}
		return null;
	}

	/**
	 * The types {@link #getAnnotation(Class, Class)} searches after the given one, in their order:
	 * its interfaces, the types of its annotations unless it is an annotation or implements one,
	 * and its superclass unless that is {@link Object}
	 *
	 * @param type
	 *            the type just searched
	 * @return the types to search after it
	 */
	private static List<Class<?>> typesToSearchAfter(final Class<?> type)
	{
		final List<Class<?>> next = new ArrayList<>(Arrays.asList(type.getInterfaces()));
		// an annotation's own annotations would lead from meta-annotation to meta-annotation
		if (!Annotation.class.isAssignableFrom(type))
		{
			for (final Annotation annotation : type.getAnnotations())
			{
				next.add(annotation.annotationType());
			}
		}
		final Class<?> superclass = type.getSuperclass();
		if (superclass != null && superclass != Object.class)
		{
			next.add(superclass);
		}
		return next;
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
	 * Scan recursive for all annotated classes in the given directory: every class file below the
	 * given directory counts, the ones of nested, local and anonymous classes included. Without an
	 * annotation class every class is returned. A {@code package-info} and a {@code module-info}
	 * are skipped, which hold no class.
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
	public static Set<Class<?>> scanForAllAnnotatedClasses(final File directory,
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
					.addAll(scanForAllAnnotatedClasses(file, qualifiedClassname, annotationClass));
			}
			else if (ScanPackageExtensions.isTypeClassFile(file.getName()))
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
	 * Scan recursive for all annotated classes in the given directory: every class file below the
	 * given directory counts, the ones of nested, local and anonymous classes included. Without an
	 * annotation set every class is returned. A {@code package-info} and a {@code module-info} are
	 * skipped, which hold no class.
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
	public static Set<Class<?>> scanForAllAnnotatedClassesFromSet(final File directory,
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
					scanForAllAnnotatedClassesFromSet(file, qualifiedClassname, annotationClasses));
			}
			else if (ScanPackageExtensions.isTypeClassFile(file.getName()))
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
	 * Scan recursive for all classes in the given directory: every class file below the given
	 * directory counts, the ones of nested, local and anonymous classes included, and no
	 * {@code package-info} or {@code module-info}, which hold no class.
	 *
	 * @param directory
	 *            the directory
	 * @param packagePath
	 *            the package path
	 * @return the list
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 */
	public static Set<Class<?>> scanForAllClasses(final File directory, final String packagePath)
		throws ClassNotFoundException
	{
		return AnnotationExtensions.scanForAllAnnotatedClasses(directory, packagePath, null);
	}

}
