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
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class {@link ImportResourcesExtensions} contains extension methods for {@link ImportResource}
 * objects.
 */
public final class ImportResourcesExtensions
{

	/**
	 * Gets a {@link Map} with {@link ImportResource} objects and the corresponding to the found
	 * class from the given package Name. The search is made recursive. The key from an entry of the
	 * map is the class where the {@link ImportResource} objects found and the value is an Array of
	 * the {@link ImportResource} objects that contains in the class.
	 *
	 * @param packageName
	 *            the package name
	 * @return the import resources
	 * @throws ClassNotFoundException
	 *             occurs if a given class cannot be located by the specified class loader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * 
	 * @throws URISyntaxException
	 *             is thrown if a string could not be parsed as a URI reference.
	 */
	public static Map<Class<?>, ImportResource[]> getImportResources(final String packageName)
		throws ClassNotFoundException, IOException, URISyntaxException
	{
		final Map<Class<?>, ImportResource[]> resourcesMap = new LinkedHashMap<>();

		final Class<ImportResources> importResourcesClass = ImportResources.class;
		final Class<ImportResource> importResourceClass = ImportResource.class;
		final Set<Class<?>> importResourcesClasses = AnnotationExtensions
			.getAllAnnotatedClasses(packageName, importResourcesClass);
		final Set<Class<?>> importResourceClasses = AnnotationExtensions
			.getAllAnnotatedClasses(packageName, importResourceClass);
		importResourcesClasses.addAll(importResourceClasses);
		for (final Class<?> annotatedClass : importResourcesClasses)
		{
			final ImportResources importResources = annotatedClass
				.getAnnotation(ImportResources.class);
			ImportResource[] importResourcesArray = null;
			ImportResource[] importResourceArray = null;
			if (importResources != null)
			{
				importResourcesArray = importResources.resources();
			}

			final ImportResource importResource = annotatedClass
				.getAnnotation(ImportResource.class);
			if (importResource != null)
			{
				importResourceArray = new ImportResource[1];
				importResourceArray[0] = importResource;
			}
			final ImportResource[] array = join(importResourceArray, importResourcesArray);
			Arrays.sort(array, new ImportResourceComparator());
			resourcesMap.put(annotatedClass, array);

		}
		return resourcesMap;
	}

	/**
	 * Joins two arrays of the same type into a single array.
	 * <p>
	 * This method combines the elements of the two input arrays {@code array1} and {@code array2}
	 * into a new array. If either of the arrays is {@code null}, the other array is returned. If
	 * both arrays are {@code null}, the result is {@code null}. The resulting array is of the same
	 * component type as the first array, and it contains all elements from both input arrays.
	 * </p>
	 *
	 * @param <T>
	 *            the component type of the arrays
	 * @param array1
	 *            the first array, may be {@code null}
	 * @param array2
	 *            the second array, may be {@code null}
	 * @return a new array containing all elements of {@code array1} and {@code array2}, or one of
	 *         the arrays if the other is {@code null}
	 * @throws IllegalArgumentException
	 *             if the component types of the two arrays are incompatible
	 * @throws ArrayStoreException
	 *             if an incompatible element is found when copying the arrays
	 */
	@SafeVarargs
	public static <T> T[] join(final T[] array1, final T... array2)
	{
		if (array1 == null)
		{
			return array2;
		}
		else if (array2 == null)
		{
			return array1;
		}
		final Class<?> type1 = array1.getClass().getComponentType();
		@SuppressWarnings("unchecked") // OK, because array is of type T
		final T[] joinedArray = (T[])Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try
		{
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		}
		catch (final ArrayStoreException ase)
		{
			// Check if the problem was due to incompatible types
			/*
			 * We do this here, rather than before the copy because: - it would be a wasted check
			 * most of the time - safer, in case check turns out to be too strict
			 */
			final Class<?> type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2))
			{
				throw new IllegalArgumentException(
					"Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
			}
			throw ase; // No, so rethrow original
		}
		return joinedArray;
	}

}
