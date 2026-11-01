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

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

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
		return ClassFile.of().build(ClassDesc.of(qualifiedClassName),
			classBuilder -> classBuilder.withFlags(ClassFile.ACC_PUBLIC).withMethodBody(
				ConstantDescs.INIT_NAME, ConstantDescs.MTD_void, ClassFile.ACC_PUBLIC,
				code -> code.aload(0).invokespecial(ConstantDescs.CD_Object,
					ConstantDescs.INIT_NAME, ConstantDescs.MTD_void).return_()));
	}

	/**
	 * Writes a jar with the given entries, in the given order. A name that ends with a slash is a
	 * directory entry, every other name ends with {@code .class} and holds a generated class of the
	 * name its path gives
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
		try (JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(jar)))
		{
			for (final String entryName : entryNames)
			{
				jarOutputStream.putNextEntry(new JarEntry(entryName));
				if (!entryName.endsWith("/"))
				{
					jarOutputStream.write(newClassFile(toClassName(entryName)));
				}
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

	private static String toClassName(final String entryName)
	{
		if (!entryName.endsWith(CLASS_FILE_SUFFIX))
		{
			throw new IllegalArgumentException("not a class file entry: " + entryName);
		}
		return entryName.substring(0, entryName.length() - CLASS_FILE_SUFFIX.length()).replace('/',
			'.');
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
