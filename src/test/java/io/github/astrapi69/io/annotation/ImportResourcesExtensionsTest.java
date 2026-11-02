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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.io.OtherPage;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPage;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPanel;
import io.github.astrapi69.io.annotation.testdata.scan.sub.ScannedSubPage;

/**
 * The unit test class for the class {@link ImportResourcesExtensions}.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class ImportResourcesExtensionsTest
{

	/**
	 * Test for method {@link ImportResourcesExtensions#getImportResources(String)}
	 *
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	@Test
	public void testGetImportResources()
		throws IOException, ClassNotFoundException, URISyntaxException
	{
		int expectedLength;
		int actualLength;
		int expectedIndex;
		int actualIndex;
		String expected;
		String actual;
		final Map<Class<?>, ImportResource[]> resources = ImportResourcesExtensions
			.getImportResources("io.github.astrapi69.io");
		final ImportResource[] somePageResources = resources
			.get(io.github.astrapi69.io.annotation.TestPage.class);

		assertNotNull(somePageResources);
		expectedLength = 3;
		actualLength = somePageResources.length;
		assertTrue(expectedLength == actualLength);

		final ImportResource cssResource = somePageResources[0];
		expectedIndex = 1;
		actualIndex = cssResource.index();
		assertTrue(expectedIndex == actualIndex);

		expected = "TestPage.css";
		actual = cssResource.resourceName();
		assertEquals(expected, actual);

		expected = "css";
		actual = cssResource.resourceType();
		assertEquals(expected, actual);

		final ImportResource jsResource = somePageResources[1];

		expectedIndex = 2;
		actualIndex = jsResource.index();
		assertTrue(expectedIndex == actualIndex);

		expected = "TestPage.js";
		actual = jsResource.resourceName();
		assertEquals(expected, actual);

		expected = "js";
		actual = jsResource.resourceType();
		assertEquals(expected, actual);

		final ImportResource jsResource2 = somePageResources[2];

		expectedIndex = 2;
		actualIndex = jsResource2.index();
		assertTrue(expectedIndex == actualIndex);

		expected = "TestPanel.js";
		actual = jsResource2.resourceName();
		assertEquals(expected, actual);

		expected = "js";
		actual = jsResource2.resourceType();
		assertEquals(expected, actual);

		final ImportResource[] otherPageResources = resources.get(OtherPage.class);
		assertNotNull(otherPageResources);

		expectedLength = 1;
		actualLength = otherPageResources.length;
		assertTrue(expectedLength == actualLength);

	}

	/**
	 * Test method for {@link ImportResourcesExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ImportResourcesExtensions.class);
	}

	static Stream<Arguments> joinedArrays()
	{
		final String[] first = { "a", "b" };
		final String[] second = { "c" };
		return Stream.of(
			arguments(named("two arrays", first), second, new String[] { "a", "b", "c" }),
			arguments(named("an empty first array", new String[0]), second, second),
			arguments(named("no first array", null), second, second),
			arguments(named("no second array", first), null, first),
			arguments(named("no array at all", null), null, null));
	}

	private static List<String> resourceNames(final ImportResource[] importResources)
	{
		return Arrays.stream(importResources).map(ImportResource::resourceName).toList();
	}

	/**
	 * The import resources of a package are collected from the classes of the package and its sub
	 * packages that carry an {@link ImportResource} or an {@link ImportResources}, each array
	 * sorted by the index of its resources
	 *
	 * @throws Exception
	 *             if the package cannot be scanned
	 */
	@Test
	void getImportResources_collectsTheResourcesOfEveryAnnotatedClass_sortedByIndex()
		throws Exception
	{
		final Map<Class<?>, ImportResource[]> resources = ImportResourcesExtensions
			.getImportResources(ScanPackageExtensionsTest.SCAN_PACKAGE);

		assertEquals(Set.of(ScannedPage.class, ScannedPanel.class, ScannedSubPage.class),
			resources.keySet());
		assertEquals(List.of("ScannedPage.css"), resourceNames(resources.get(ScannedPage.class)));
		assertEquals(List.of("ScannedPanel.css", "ScannedPanel.js"),
			resourceNames(resources.get(ScannedPanel.class)));
		assertEquals(List.of("ScannedSubPage.js"),
			resourceNames(resources.get(ScannedSubPage.class)));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("joinedArrays")
	void join_appendsTheSecondArrayToTheFirst(final String[] first, final String[] second,
		final String[] expected)
	{
		assertArrayEquals(expected, ImportResourcesExtensions.join(first, second));
	}

	@Test
	void join_throwsIllegalArgumentException_whenTheSecondArrayDoesNotFitIntoTheFirstsType()
	{
		final Object[] integers = new Integer[] { 1 };
		final Object[] strings = new String[] { "a" };

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> ImportResourcesExtensions.join(integers, strings));
		assertEquals("Cannot store java.lang.String in an array of java.lang.Integer",
			exception.getMessage());
		assertInstanceOf(ArrayStoreException.class, exception.getCause());
	}

	/**
	 * Characterizes {@link ImportResourcesExtensions#join(Object[], Object[])} beyond the elements:
	 * without one array it returns the other one itself, and otherwise a new array, whose component
	 * type is the runtime component type of the first array
	 */
	@Test
	void join_returnsTheOtherArrayItself_orANewArrayOfTheFirstArraysComponentType()
	{
		final String[] strings = { "a" };
		final String[] empty = new String[0];

		assertSame(strings, ImportResourcesExtensions.join(null, strings));
		assertSame(strings, ImportResourcesExtensions.join(strings, (String[])null));

		final String[] joinedWithEmpty = ImportResourcesExtensions.join(strings, empty);
		assertNotSame(strings, joinedWithEmpty);
		assertArrayEquals(strings, joinedWithEmpty);

		final Object[] covariant = new String[] { "s" };
		assertEquals(String[].class, ImportResourcesExtensions.join(covariant, "t").getClass());
		final CharSequence[] charSequences = { "x" };
		final CharSequence[] joined = ImportResourcesExtensions.join(charSequences, "y");
		assertEquals(CharSequence[].class, joined.getClass());
		assertArrayEquals(new CharSequence[] { "x", "y" }, joined);
	}
}
