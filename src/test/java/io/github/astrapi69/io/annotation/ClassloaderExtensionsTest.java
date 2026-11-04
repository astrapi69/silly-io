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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.io.annotation.testdata.scan.ScannedPlain;

/**
 * {@link ClassloaderExtensions} chooses between the context class loader of the current thread, the
 * class loader of an object and the system class loader. Every case sets the context class loader
 * itself and restores the previous one afterwards
 */
class ClassloaderExtensionsTest
{

	private static final ClassLoader SYSTEM = ClassLoader.getSystemClassLoader();

	private static final ClassLoader PLATFORM = ClassLoader.getPlatformClassLoader();

	private static final ClassLoader CHILD = new ClassLoader(SYSTEM)
	{
	};

	private static final ClassLoader OTHER_CHILD = new ClassLoader(SYSTEM)
	{
	};

	private static final ClassFixtures.DefiningClassLoader DEFINING_CHILD = new ClassFixtures.DefiningClassLoader(
		SYSTEM);

	private static final Object OBJECT_OF_THE_CHILD = newObjectOfTheDefiningChild();

	static Stream<Arguments> contextClassLoaders()
	{
		return Stream.of(
			arguments(named("the system class loader", SYSTEM),
				named("the system class loader", SYSTEM)),
			arguments(named("a child of the system class loader", CHILD),
				named("the child", CHILD)),
			arguments(
				named("the platform class loader, a parent of the system class loader", PLATFORM),
				named("the system class loader", SYSTEM)),
			arguments(named("no context class loader", null),
				named("the system class loader", SYSTEM)));
	}

	static Stream<Arguments> objectsAndContextClassLoaders()
	{
		return Stream.of(
			arguments(named("an object of the class path", new ScannedPlain()),
				named("the system class loader", SYSTEM), named("the system class loader", SYSTEM)),
			arguments(named("an object of a child class loader", OBJECT_OF_THE_CHILD),
				named("the system class loader", SYSTEM), named("the child", DEFINING_CHILD)),
			arguments(named("a string, of the bootstrap class loader", "text"),
				named("a child of the system class loader", CHILD), named("the child", CHILD)),
			arguments(named("an object of the class path", new ScannedPlain()),
				named("a child of the system class loader", CHILD), named("the child", CHILD)),
			arguments(named("an object of the class path", new ScannedPlain()),
				named("no context class loader", null), named("the system class loader", SYSTEM)),
			arguments(named("a string, of the bootstrap class loader", "text"),
				named("no context class loader", null), named("the system class loader", SYSTEM)));
	}

	static Stream<Arguments> classLoaderPairs()
	{
		return Stream.of(arguments(named("the same class loader", SYSTEM), SYSTEM, true),
			arguments(named("the system class loader and null", SYSTEM), null, false),
			arguments(named("the bootstrap class loader and any other", null), SYSTEM, true),
			arguments(named("the bootstrap class loader and itself", null), null, true),
			arguments(named("a parent and its child", SYSTEM), CHILD, true),
			arguments(named("a grandparent and its grandchild", PLATFORM), CHILD, true),
			arguments(named("a child and its parent", CHILD), SYSTEM, false),
			arguments(named("two children of the same parent", CHILD), OTHER_CHILD, false));
	}

	private static Object newObjectOfTheDefiningChild()
	{
		try
		{
			return DEFINING_CHILD.define("generated.loader.Defined").getConstructor().newInstance();
		}
		catch (final ReflectiveOperationException exception)
		{
			throw new IllegalStateException("the generated class cannot be instantiated",
				exception);
		}
	}

	@ParameterizedTest(name = "context: {0}, expected: {1}")
	@MethodSource("contextClassLoaders")
	void getClassLoader_choosesBetweenTheContextAndTheSystemClassLoader(
		final ClassLoader contextClassLoader, final ClassLoader expected) throws Exception
	{
		assertSame(expected, ClassFixtures.withContextClassLoader(contextClassLoader,
			ClassloaderExtensions::getClassLoader));
	}

	@ParameterizedTest(name = "{0}, context: {1}, expected: {2}")
	@MethodSource("objectsAndContextClassLoaders")
	void getClassLoaderOfAnObject_choosesTheObjectsClassLoaderWhenItDescendsFromTheContext(
		final Object object, final ClassLoader contextClassLoader, final ClassLoader expected)
		throws Exception
	{
		assertSame(expected, ClassFixtures.withContextClassLoader(contextClassLoader,
			() -> ClassloaderExtensions.getClassLoader(object)));
	}

	@ParameterizedTest(name = "{0}: {2}")
	@MethodSource("classLoaderPairs")
	void isDerivate_isTrue_whenTheComparedClassLoaderDescendsFromTheSource(final ClassLoader source,
		final ClassLoader compare, final boolean expected)
	{
		assertEquals(expected, ClassloaderExtensions.isDerivate(source, compare));
	}
}
