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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Where {@link AnnotationExtensions#getAnnotation(Class, Class)} looks, and in which order, told by
 * the value of the annotation it returns: first the class itself, the annotations it inherits
 * included, then each of its interfaces with everything above it, then the types of its annotations
 * unless it is an annotation or implements one, then its superclass with everything above it, up to
 * {@link Object}. A meta-annotation counts one level deep only. These tests pin the behaviour
 * before the implementation is replaced (#60)
 */
class AnnotationExtensionsGetAnnotationTest
{

	static Stream<Arguments> lookups()
	{
		return Stream.of(arguments(named("the class itself first", Own.class), "own"),
			arguments(named(
				"an interface, depth first through the interfaces it extends, before the "
					+ "next interface, a meta-annotation and the superclass",
				InterfacesMetaSuperclass.class), "base interface"),
			arguments(
				named("a meta-annotation before the superclass", MetaSuperclass.class), "meta"),
			arguments(named("the superclass", SuperclassOnly.class), "superclass"),
			arguments(named("the superclass of the superclass", SuperclassOfTheSuperclass.class),
				"superclass"),
			arguments(named("the meta-annotation of an interface", MetaInterface.class), "meta"),
			arguments(named("the meta-annotation of an implemented interface",
				MetaInterfaceImplementation.class), "meta"),
			arguments(named("an annotation type carrying it", FoundMeta.class), "meta"),
			arguments(named("not a meta-annotation two levels deep", TwoLevelsMeta.class), null),
			arguments(named("not an annotation type two levels deep", FoundMetaMeta.class), null),
			arguments(named("no meta-annotation of a class that implements an annotation, but its "
				+ "superclass", AnnotationImplementation.class), "superclass"),
			arguments(named("Object", Object.class), null),
			arguments(named("a primitive type", int.class), null),
			arguments(named("an array type", String[].class), null),
			arguments(named("a class without it anywhere", Unrelated.class), null));
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("lookups")
	void getAnnotation_returnsTheFirstOneInTheSearchOrder(final Class<?> componentClass,
		final String expected)
	{
		final Found found = AnnotationExtensions.getAnnotation(componentClass, Found.class);

		assertEquals(expected, found == null ? null : found.value());
	}

	@Test
	void getAnnotation_takesAnInheritedAnnotationAsTheClassOwn_beforeTheInterfaces()
	{
		assertEquals("inherited",
			AnnotationExtensions.getAnnotation(InheritedChild.class, InheritedFound.class).value());
	}

	@Test
	void getAnnotation_findsTheAnnotationsOfTheTypesOfTheAnnotations()
	{
		// the class carries no @Retention, but the type of its annotation @FoundMeta does
		assertEquals(RetentionPolicy.RUNTIME,
			AnnotationExtensions.getAnnotation(MetaSuperclass.class, Retention.class).value());
		assertNull(AnnotationExtensions.getAnnotation(SuperclassOnly.class, Inherited.class));
	}

	@Test
	void getAnnotation_refusesNull()
	{
		assertThrows(NullPointerException.class,
			() -> AnnotationExtensions.getAnnotation(null, Found.class));
		assertThrows(NullPointerException.class,
			() -> AnnotationExtensions.getAnnotation(Own.class, null));
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@interface Found {
		String value();
	}

	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface InheritedFound {
		String value();
	}

	@Found("meta")
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@interface FoundMeta {
	}

	@FoundMeta
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface FoundMetaMeta {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface Plain {
	}

	@Found("base interface")
	interface BaseInterface
	{
	}

	interface ChildInterface extends BaseInterface
	{
	}

	@Found("other interface")
	interface OtherInterface
	{
	}

	@FoundMeta
	interface MetaInterface
	{
	}

	@InheritedFound("interface")
	interface InheritedFoundInterface
	{
	}

	@Found("superclass")
	static class Superclass
	{
	}

	@Found("own")
	@FoundMeta
	static class Own extends Superclass implements OtherInterface
	{
	}

	@FoundMeta
	static class InterfacesMetaSuperclass extends Superclass
		implements
			ChildInterface,
			OtherInterface
	{
	}

	@FoundMeta
	static class MetaSuperclass extends Superclass
	{
	}

	static class SuperclassOnly extends Superclass
	{
	}

	static class SuperclassOfTheSuperclass extends SuperclassOnly
	{
	}

	static class MetaInterfaceImplementation implements MetaInterface
	{
	}

	@FoundMetaMeta
	static class TwoLevelsMeta
	{
	}

	@FoundMeta
	static class AnnotationImplementation extends Superclass implements Plain
	{
		@Override
		public Class<? extends Annotation> annotationType()
		{
			return Plain.class;
		}
	}

	@InheritedFound("inherited")
	static class InheritedBase
	{
	}

	static class InheritedChild extends InheritedBase implements InheritedFoundInterface
	{
	}

	@Plain
	static class Unrelated
	{
	}
}
