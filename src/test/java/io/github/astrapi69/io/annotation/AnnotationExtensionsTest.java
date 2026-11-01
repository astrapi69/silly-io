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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.io.annotation.testdata.broken.FailingStaticInitializer;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPage;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPanel;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPlain;
import io.github.astrapi69.io.annotation.testdata.scan.sub.ScannedSubPage;

/**
 * {@link AnnotationExtensions} finds annotations on a class, its interfaces, its annotations and
 * its super classes, and scans the directories of a package for annotated classes. The scans read
 * the fixtures in {@code testdata.scan} and {@code testdata.broken} on the test class path; the
 * class hierarchy is made of the nested types below
 */
class AnnotationExtensionsTest
{

	private static final String SCAN_PACKAGE = ScanPackageExtensionsTest.SCAN_PACKAGE;

	private static final String BROKEN_PACKAGE = "io.github.astrapi69.io.annotation.testdata.broken";

	private static final Set<Class<?>> ALL_SCANNED_CLASSES = Set.of(ScannedPage.class,
		ScannedPanel.class, ScannedPlain.class, ScannedSubPage.class);

	@TempDir
	Path directory;

	static Stream<Arguments> markerLookups()
	{
		return Stream.of(arguments(named("a class annotated directly", MarkedType.class), true),
			arguments(named("a class whose super class is annotated", MarkedTypeChild.class), true),
			arguments(
				named("a class whose interface is annotated", MarkedInterfaceImplementation.class),
				true),
			arguments(
				named("a class annotated with an annotated annotation", ComposedMarkedType.class),
				true),
			arguments(named("an annotation annotated with it", ComposedMarker.class), true),
			arguments(named("a class without it in its hierarchy", UnmarkedType.class), false),
			arguments(named("a class whose super class does not have it", UnmarkedTypeChild.class),
				false),
			arguments(named("an interface without it", UnmarkedInterface.class), false),
			arguments(named("an annotation without it", Unrelated.class), false));
	}

	static Stream<Arguments> superClassLookups()
	{
		return Stream.of(arguments(named("a class annotated directly", MarkedType.class), true),
			arguments(named("a class whose super class is annotated", MarkedTypeChild.class), true),
			arguments(
				named("a class whose interface is annotated", MarkedInterfaceImplementation.class),
				false),
			arguments(
				named("a class annotated with an annotated annotation", ComposedMarkedType.class),
				false),
			arguments(named("a class whose super class does not have it", UnmarkedTypeChild.class),
				false));
	}

	static Stream<Arguments> annotatedClassScans()
	{
		return Stream.of(
			arguments(named("ImportResource", ImportResource.class),
				Set.of(ScannedPage.class, ScannedSubPage.class)),
			arguments(named("ImportResources", ImportResources.class), Set.of(ScannedPanel.class)),
			arguments(named("no annotation", null), ALL_SCANNED_CLASSES));
	}

	static Stream<Arguments> annotationSetScans()
	{
		return Stream.of(
			arguments(
				named("ImportResource and ImportResources",
					Set.of(ImportResource.class, ImportResources.class)),
				Set.of(ScannedPage.class, ScannedPanel.class, ScannedSubPage.class)),
			arguments(named("ImportResources", Set.of(ImportResources.class)),
				Set.of(ScannedPanel.class)),
			arguments(named("Marker, which no scanned class carries", Set.of(Marker.class)),
				Set.of()),
			arguments(named("no annotation set", null), ALL_SCANNED_CLASSES));
	}

	static Stream<Arguments> uninitializableClassScans()
	{
		return Stream.of(
			arguments(named("ImportResource, which it carries", ImportResource.class),
				Set.of(FailingStaticInitializer.class)),
			arguments(named("ImportResources, which it does not carry", ImportResources.class),
				Set.of()),
			arguments(named("no annotation", null), Set.of(FailingStaticInitializer.class)));
	}

	static Stream<Arguments> invalidAnnotationValues()
	{
		return Stream.of(arguments(named("a value of another type", "resourceName"), 42),
			arguments(named("a key the annotation does not have", "noSuchKey"), "value"));
	}

	private static File packageDirectory(final Class<?> classInThePackage) throws Exception
	{
		return Path.of(classInThePackage.getResource("").toURI()).toFile();
	}

	private static ImportResource newImportResource(final Map<String, Object> memberValues)
	{
		return (ImportResource)Proxy.newProxyInstance(ImportResource.class.getClassLoader(),
			new Class<?>[] { ImportResource.class }, new MemberValuesHandler(memberValues));
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("markerLookups")
	void getAnnotation_findsTheAnnotationAnywhereInTheHierarchy(final Class<?> componentClass,
		final boolean expected)
	{
		final Marker marker = AnnotationExtensions.getAnnotation(componentClass, Marker.class);

		assertEquals(expected, marker != null);
		assertEquals(expected, AnnotationExtensions
			.isAnnotationPresentInSuperClassesOrInterfaces(componentClass, Marker.class));
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("superClassLookups")
	void isAnnotationPresentInSuperClasses_looksAtTheClassAndItsSuperClassesOnly(
		final Class<?> componentClass, final boolean expected)
	{
		assertEquals(expected,
			AnnotationExtensions.isAnnotationPresentInSuperClasses(componentClass, Marker.class));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("annotatedClassScans")
	void getAllAnnotatedClasses_findsTheAnnotatedClassesOfThePackageAndItsSubPackages(
		final Class<? extends Annotation> annotationClass, final Set<Class<?>> expected)
		throws Exception
	{
		assertEquals(expected,
			AnnotationExtensions.getAllAnnotatedClasses(SCAN_PACKAGE, annotationClass));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("annotationSetScans")
	void getAllAnnotatedClassesFromSet_findsTheClassesThatCarryOneOfTheAnnotations(
		final Set<Class<? extends Annotation>> annotationClasses, final Set<Class<?>> expected)
		throws Exception
	{
		assertEquals(expected,
			AnnotationExtensions.getAllAnnotatedClassesFromSet(SCAN_PACKAGE, annotationClasses));
		assertEquals(expected, AnnotationExtensions.getAllClasses(SCAN_PACKAGE, annotationClasses));
	}

	@Test
	void getAllClasses_findsEveryClassOfThePackageAndItsSubPackages() throws Exception
	{
		assertEquals(ALL_SCANNED_CLASSES, AnnotationExtensions.getAllClasses(SCAN_PACKAGE));
	}

	@Test
	void scanForClasses_findsEveryClassOfTheDirectory() throws Exception
	{
		assertEquals(ALL_SCANNED_CLASSES,
			AnnotationExtensions.scanForClasses(packageDirectory(ScannedPage.class), SCAN_PACKAGE));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("uninitializableClassScans")
	void scanForAnnotatedClasses_findsAClassThatCannotBeInitialized(
		final Class<? extends Annotation> annotationClass, final Set<Class<?>> expected)
		throws Exception
	{
		assertEquals(expected, AnnotationExtensions.scanForAnnotatedClasses(
			packageDirectory(FailingStaticInitializer.class), BROKEN_PACKAGE, annotationClass));
		assertEquals(expected,
			AnnotationExtensions.scanForAnnotatedClassesFromSet(
				packageDirectory(FailingStaticInitializer.class), BROKEN_PACKAGE,
				annotationClass == null ? null : Set.of(annotationClass)));
	}

	@Test
	void scanForAnnotatedClasses_isEmpty_forADirectoryThatDoesNotExist() throws Exception
	{
		final File missing = directory.resolve("missing").toFile();

		assertTrue(AnnotationExtensions.scanForAnnotatedClasses(missing, "missing", Marker.class)
			.isEmpty());
		assertTrue(AnnotationExtensions
			.scanForAnnotatedClassesFromSet(missing, "missing", Set.of(Marker.class)).isEmpty());
	}

	@Test
	void scanForAnnotatedClasses_throwsClassNotFoundException_forAClassFileNoClassLoaderKnows()
		throws Exception
	{
		Files.createFile(directory.resolve("Ghost.class"));
		final File ghostDirectory = directory.toFile();

		assertThrows(ClassNotFoundException.class, () -> AnnotationExtensions
			.scanForAnnotatedClasses(ghostDirectory, "io.github.astrapi69.no.such", Marker.class));
		assertThrows(ClassNotFoundException.class,
			() -> AnnotationExtensions.scanForAnnotatedClassesFromSet(ghostDirectory,
				"io.github.astrapi69.no.such", Set.of(Marker.class)));
	}

	@Test
	void setAnnotationValue_replacesTheMemberValueAndReturnsTheOldOne() throws Exception
	{
		final Map<String, Object> memberValues = new HashMap<>();
		memberValues.put("resourceName", "old.css");
		final ImportResource importResource = newImportResource(memberValues);

		final Object oldValue = AnnotationExtensions.setAnnotationValue(importResource,
			"resourceName", "new.css");

		assertEquals("old.css", oldValue);
		assertEquals("new.css", importResource.resourceName());
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("invalidAnnotationValues")
	void setAnnotationValue_throwsIllegalArgumentException_andKeepsTheOldValue(final String key,
		final Object value)
	{
		final Map<String, Object> memberValues = new HashMap<>();
		memberValues.put("resourceName", "old.css");
		final ImportResource importResource = newImportResource(memberValues);

		assertThrows(IllegalArgumentException.class,
			() -> AnnotationExtensions.setAnnotationValue(importResource, key, value));
		assertEquals(Map.of("resourceName", "old.css"), memberValues);
	}

	/**
	 * The annotation the hierarchy lookups search for; it is not inherited, so a super class only
	 * passes it on through the lookup itself
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@interface Marker {
	}

	/**
	 * An annotation that carries the marker
	 */
	@Marker
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface ComposedMarker {
	}

	/**
	 * An annotation without the marker
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface Unrelated {
	}

	@Marker
	interface MarkedInterface
	{
	}

	interface UnmarkedInterface
	{
	}

	@Marker
	static class MarkedType
	{
	}

	static class MarkedTypeChild extends MarkedType
	{
	}

	static class MarkedInterfaceImplementation implements MarkedInterface
	{
	}

	@ComposedMarker
	static class ComposedMarkedType
	{
	}

	@Unrelated
	static class UnmarkedType implements UnmarkedInterface
	{
	}

	static class UnmarkedTypeChild extends UnmarkedType
	{
	}

	/**
	 * Keeps the member values of an annotation proxy in a map field named {@code memberValues}, as
	 * the invocation handler of the JDK does
	 */
	private static final class MemberValuesHandler implements InvocationHandler
	{
		private final Map<String, Object> memberValues;

		MemberValuesHandler(final Map<String, Object> memberValues)
		{
			this.memberValues = memberValues;
		}

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args)
		{
			return memberValues.get(method.getName());
		}
	}
}
