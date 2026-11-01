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

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

/**
 * Builds classes that are on no class path, as class files or in a jar, so that a test can load
 * them through a class loader of its own, and runs code with a given context class loader
 */
final class ClassFixtures
{

	private static final String CLASS_FILE_SUFFIX = ".class";

	private ClassFixtures()
	{
	}

	/**
	 * Builds the class file of a public class with a public constructor without parameters
	 *
	 * @param qualifiedClassName
	 *            the binary name of the class
	 * @return the bytes of the class file
	 */
	static byte[] newClassFile(final String qualifiedClassName)
	{
		return newClassFile(qualifiedClassName, classBuilder -> {
		});
	}

	/**
	 * Builds the class file of a public class with a public constructor without parameters that
	 * carries an {@link ImportResource} of the given resource name and the resource type css
	 *
	 * @param qualifiedClassName
	 *            the binary name of the class
	 * @param resourceName
	 *            the resource name of the annotation
	 * @return the bytes of the class file
	 */
	static byte[] newImportResourceClassFile(final String qualifiedClassName,
		final String resourceName)
	{
		return newClassFile(qualifiedClassName,
			classBuilder -> classBuilder.with(RuntimeVisibleAnnotationsAttribute
				.of(Annotation.of(ClassDesc.of(ImportResource.class.getName()),
					AnnotationElement.ofString("resourceName", resourceName),
					AnnotationElement.ofString("resourceType", "css")))));
	}

	/**
	 * Builds the class file of a public class with a public constructor without parameters whose
	 * static initializer throws an {@link IllegalStateException}, so that the class loads but
	 * cannot be initialized
	 *
	 * @param qualifiedClassName
	 *            the binary name of the class
	 * @return the bytes of the class file
	 */
	static byte[] newUninitializableClassFile(final String qualifiedClassName)
	{
		final ClassDesc illegalState = ClassDesc.of(IllegalStateException.class.getName());
		return newClassFile(qualifiedClassName,
			classBuilder -> classBuilder.withMethodBody(ConstantDescs.CLASS_INIT_NAME,
				ConstantDescs.MTD_void, ClassFile.ACC_STATIC,
				code -> code.new_(illegalState).dup()
					.invokespecial(illegalState, ConstantDescs.INIT_NAME, ConstantDescs.MTD_void)
					.athrow()));
	}

	private static byte[] newClassFile(final String qualifiedClassName,
		final Consumer<ClassBuilder> classContent)
	{
		return ClassFile.of().build(ClassDesc.of(qualifiedClassName), classBuilder -> {
			classBuilder.withFlags(ClassFile.ACC_PUBLIC).withMethodBody(ConstantDescs.INIT_NAME,
				ConstantDescs.MTD_void, ClassFile.ACC_PUBLIC,
				code -> code.aload(0).invokespecial(ConstantDescs.CD_Object,
					ConstantDescs.INIT_NAME, ConstantDescs.MTD_void).return_());
			classContent.accept(classBuilder);
		});
	}

	/**
	 * Builds the class files of generated classes of the given names, as
	 * {@link #newClassFile(String)} builds them
	 *
	 * @param qualifiedClassNames
	 *            the binary names of the classes
	 * @return the class file of each class, by its binary name, in the given order
	 */
	static Map<String, byte[]> classFilesOf(final String... qualifiedClassNames)
	{
		final Map<String, byte[]> classFiles = new LinkedHashMap<>();
		for (final String qualifiedClassName : qualifiedClassNames)
		{
			classFiles.put(qualifiedClassName, newClassFile(qualifiedClassName));
		}
		return classFiles;
	}

	/**
	 * Writes a jar with the given entries, in the given order. A name that ends with a slash is a
	 * directory entry, a name that ends with {@code .class} holds a generated class of the name its
	 * path gives, and every other name is a file that holds its own name as text
	 *
	 * @param jar
	 *            the jar file to write
	 * @param entryNames
	 *            the names of the entries
	 * @return the written jar file
	 * @throws IOException
	 *             if the jar cannot be written
	 */
	static Path newJar(final Path jar, final String... entryNames) throws IOException
	{
		final Map<String, byte[]> entries = new LinkedHashMap<>();
		for (final String entryName : entryNames)
		{
			entries.put(entryName, contentOf(entryName));
		}
		return writeJar(jar, entries);
	}

	/**
	 * Writes a jar that holds generated classes of the given names, as
	 * {@link #newJarOfClasses(Path, Map)} writes them
	 *
	 * @param jar
	 *            the jar file to write
	 * @param qualifiedClassNames
	 *            the binary names of the classes
	 * @return the written jar file
	 * @throws IOException
	 *             if the jar cannot be written
	 */
	static Path newJarOfClasses(final Path jar, final String... qualifiedClassNames)
		throws IOException
	{
		return newJarOfClasses(jar, classFilesOf(qualifiedClassNames));
	}

	/**
	 * Writes a jar that holds the given class files, each after the directory entries of its
	 * package and of every package above it, as a jar tool writes them
	 *
	 * @param jar
	 *            the jar file to write
	 * @param classFiles
	 *            the class file of each class, by its binary name
	 * @return the written jar file
	 * @throws IOException
	 *             if the jar cannot be written
	 */
	static Path newJarOfClasses(final Path jar, final Map<String, byte[]> classFiles)
		throws IOException
	{
		final Map<String, byte[]> entries = new LinkedHashMap<>();
		for (final Map.Entry<String, byte[]> classFile : classFiles.entrySet())
		{
			final String classEntry = classFile.getKey().replace('.', '/') + CLASS_FILE_SUFFIX;
			int slash = classEntry.indexOf('/');
			while (slash != -1)
			{
				entries.putIfAbsent(classEntry.substring(0, slash + 1), new byte[0]);
				slash = classEntry.indexOf('/', slash + 1);
			}
			entries.put(classEntry, classFile.getValue());
		}
		return writeJar(jar, entries);
	}

	/**
	 * Writes generated classes of the given names below the given class directory, as
	 * {@link #newClassDirectory(Path, Map)} writes them
	 *
	 * @param classDirectory
	 *            the root directory of the classes, created if it does not exist
	 * @param qualifiedClassNames
	 *            the binary names of the classes
	 * @return the class directory
	 * @throws IOException
	 *             if a class file cannot be written
	 */
	static Path newClassDirectory(final Path classDirectory, final String... qualifiedClassNames)
		throws IOException
	{
		return newClassDirectory(classDirectory, classFilesOf(qualifiedClassNames));
	}

	/**
	 * Writes the given class files below the given class directory, each in the directory of its
	 * package
	 *
	 * @param classDirectory
	 *            the root directory of the classes, created if it does not exist
	 * @param classFiles
	 *            the class file of each class, by its binary name
	 * @return the class directory
	 * @throws IOException
	 *             if a class file cannot be written
	 */
	static Path newClassDirectory(final Path classDirectory, final Map<String, byte[]> classFiles)
		throws IOException
	{
		for (final Map.Entry<String, byte[]> classFile : classFiles.entrySet())
		{
			final Path classFilePath = classDirectory
				.resolve(classFile.getKey().replace('.', '/') + CLASS_FILE_SUFFIX);
			Files.createDirectories(classFilePath.getParent());
			Files.write(classFilePath, classFile.getValue());
		}
		return classDirectory;
	}

	private static byte[] contentOf(final String entryName)
	{
		if (entryName.endsWith(CLASS_FILE_SUFFIX))
		{
			return newClassFile(toClassName(entryName));
		}
		if (entryName.endsWith("/"))
		{
			return new byte[0];
		}
		return entryName.getBytes(StandardCharsets.UTF_8);
	}

	private static Path writeJar(final Path jar, final Map<String, byte[]> entries)
		throws IOException
	{
		try (JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(jar)))
		{
			for (final Map.Entry<String, byte[]> entry : entries.entrySet())
			{
				jarOutputStream.putNextEntry(new JarEntry(entry.getKey()));
				jarOutputStream.write(entry.getValue());
				jarOutputStream.closeEntry();
			}
		}
		return jar;
	}

	/**
	 * Runs the given action with the given context class loader of the current thread and restores
	 * the previous one afterwards
	 *
	 * @param <T>
	 *            the type of the result
	 * @param contextClassLoader
	 *            the context class loader to run with, may be null
	 * @param action
	 *            the action to run
	 * @return the result of the action
	 * @throws Exception
	 *             whatever the action throws
	 */
	static <T> T withContextClassLoader(final ClassLoader contextClassLoader,
		final Callable<T> action) throws Exception
	{
		final Thread currentThread = Thread.currentThread();
		final ClassLoader previous = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(contextClassLoader);
		try
		{
			return action.call();
		}
		finally
		{
			currentThread.setContextClassLoader(previous);
		}
	}

	/**
	 * Runs the given action with a context class loader that reads the given class path roots, in
	 * the given order, and closes it afterwards
	 *
	 * @param <T>
	 *            the type of the result
	 * @param action
	 *            the action to run
	 * @param rootUrls
	 *            the URLs of the class directories and jars of the class path
	 * @return the result of the action
	 * @throws Exception
	 *             whatever the action throws
	 */
	static <T> T withClassPathRoots(final Callable<T> action, final URL... rootUrls)
		throws Exception
	{
		try (URLClassLoader rootsClassLoader = new URLClassLoader(rootUrls,
			ClassLoader.getSystemClassLoader()))
		{
			return withContextClassLoader(rootsClassLoader, action);
		}
	}

	/**
	 * The names of the directories, and the URL styles, below which a class path root is read: a
	 * {@code +}, which a URL keeps and form decoding turns into a space, and a space and a percent
	 * sign, which an escaped URL escapes
	 *
	 * @return the directory name and the URL style of each case
	 */
	static Stream<Arguments> urlEscapedDirectories()
	{
		return Stream.of(arguments("a+b", UrlStyle.ESCAPED), arguments("a b", UrlStyle.ESCAPED),
			arguments("a%b", UrlStyle.ESCAPED), arguments("a+b", UrlStyle.UNESCAPED),
			arguments("a b", UrlStyle.UNESCAPED));
	}

	private static String toClassName(final String entryName)
	{
		return entryName.substring(0, entryName.length() - CLASS_FILE_SUFFIX.length()).replace('/',
			'.');
	}

	/**
	 * The two kinds of class path root a package is read from
	 */
	enum RootKind
	{
		/**
		 * A directory with a class file per class, in the directory of its package
		 */
		DIRECTORY {
			@Override
			Path write(final Path location, final Map<String, byte[]> classFiles) throws IOException
			{
				return newClassDirectory(location, classFiles);
			}
		},

		/**
		 * A jar with the directory entries of its packages
		 */
		JAR {
			@Override
			Path write(final Path location, final Map<String, byte[]> classFiles) throws IOException
			{
				Files.createDirectories(location.getParent());
				return newJarOfClasses(location.resolveSibling(location.getFileName() + ".jar"),
					classFiles);
			}
		};

		/**
		 * Writes a root of this kind that holds the given class files
		 *
		 * @param location
		 *            where the root is written, a jar gets the suffix {@code .jar}
		 * @param classFiles
		 *            the class file of each class, by its binary name
		 * @return the written root
		 * @throws IOException
		 *             if the root cannot be written
		 */
		abstract Path write(Path location, Map<String, byte[]> classFiles) throws IOException;

		/**
		 * Writes a root of this kind that holds generated classes of the given names
		 *
		 * @param location
		 *            where the root is written, a jar gets the suffix {@code .jar}
		 * @param qualifiedClassNames
		 *            the binary names of the classes
		 * @return the written root
		 * @throws IOException
		 *             if the root cannot be written
		 */
		Path write(final Path location, final String... qualifiedClassNames) throws IOException
		{
			return write(location, classFilesOf(qualifiedClassNames));
		}
	}

	/**
	 * The two ways a class path root is given to a class loader as a URL
	 */
	enum UrlStyle
	{
		/**
		 * Escaped, as {@link Path#toUri()} builds it and the class loaders of the JDK read the
		 * class path: a space is {@code %20}, a percent sign {@code %25}, a plus stays a plus
		 */
		ESCAPED {
			@Override
			URL of(final Path root) throws IOException
			{
				return root.toUri().toURL();
			}
		},

		/**
		 * Unescaped, as the deprecated {@link java.io.File#toURL()} builds it: a space stays a
		 * space, so that the URL is no valid URI
		 */
		UNESCAPED {
			@Override
			@SuppressWarnings("deprecation")
			URL of(final Path root) throws IOException
			{
				return root.toFile().toURL();
			}
		};

		/**
		 * Gives the URL of the given class path root in this style
		 *
		 * @param root
		 *            the class directory or jar
		 * @return the URL of the root
		 * @throws IOException
		 *             if no URL can be built for the root
		 */
		abstract URL of(Path root) throws IOException;
	}

	/**
	 * A class loader that defines generated classes itself
	 */
	static final class DefiningClassLoader extends ClassLoader
	{

		/**
		 * Creates a class loader with the given parent
		 *
		 * @param parent
		 *            the parent class loader
		 */
		DefiningClassLoader(final ClassLoader parent)
		{
			super(parent);
		}

		/**
		 * Defines a generated class of the given name in this class loader
		 *
		 * @param qualifiedClassName
		 *            the binary name of the class
		 * @return the defined class
		 */
		Class<?> define(final String qualifiedClassName)
		{
			final byte[] classFile = newClassFile(qualifiedClassName);
			return defineClass(qualifiedClassName, classFile, 0, classFile.length);
		}
	}
}
