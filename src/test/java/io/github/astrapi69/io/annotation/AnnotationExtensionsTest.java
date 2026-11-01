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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapi69.io.annotation.ClassFixtures.RootKind;
import io.github.astrapi69.io.annotation.ClassFixtures.UrlStyle;
import io.github.astrapi69.io.annotation.testdata.broken.FailingStaticInitializer;
import io.github.astrapi69.io.annotation.testdata.info.InfoPlain;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPage;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPanel;
import io.github.astrapi69.io.annotation.testdata.scan.ScannedPlain;
import io.github.astrapi69.io.annotation.testdata.scan.sub.ScannedSubPage;

/**
 * {@link AnnotationExtensions} finds annotations on a class, its interfaces, its annotations and
 * its super classes, and scans the directories of a package for annotated classes. The scans read
 * the fixtures in {@code testdata.scan}, {@code testdata.broken} and {@code testdata.info} on the
 * test class path; the class hierarchy is made of the nested types below
 */
class AnnotationExtensionsTest
{

	private static final String SCAN_PACKAGE = ScanPackageExtensionsTest.SCAN_PACKAGE;

	private static final String BROKEN_PACKAGE = "io.github.astrapi69.io.annotation.testdata.broken";

	private static final String ROOT_PACKAGE = "gen.pkg";

	private static final String INFO_PACKAGE = "info.pkg";

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

	static Stream<Arguments> rootScans()
	{
		return packageScansOfEveryKindOfRoot(
			Set.of(ROOT_PACKAGE + ".Annotated", ROOT_PACKAGE + ".Plain",
				ROOT_PACKAGE + ".Plain$Inner", ROOT_PACKAGE + ".Uninitializable",
				ROOT_PACKAGE + ".sub.Deep"),
			Set.of(ROOT_PACKAGE + ".Annotated", ROOT_PACKAGE + ".sub.Deep"));
	}

	/**
	 * The package scans over the classes of {@link #infoClassFiles()}: every one of them finds the
	 * classes and no {@code package-info} or {@code module-info}
	 *
	 * @return the scan, the kind of root and the binary names of the classes it finds, for each
	 *         case
	 */
	static Stream<Arguments> infoFileScans()
	{
		return packageScansOfEveryKindOfRoot(
			Set.of(INFO_PACKAGE + ".Info", INFO_PACKAGE + ".sub.Deep"),
			Set.of(INFO_PACKAGE + ".Info"));
	}

	/**
	 * The scans over the package {@code testdata.info}, whose {@code package-info} carries
	 * {@link Deprecated}: none of them takes it for a class, with the annotation or without one
	 *
	 * @return the scan and the classes it finds, for each case
	 */
	static Stream<Arguments> annotatedPackageScans()
	{
		return Stream.of(
			arguments(named("getAllAnnotatedClasses with Deprecated",
				(PackageScan)packagePath -> AnnotationExtensions.getAllAnnotatedClasses(packagePath,
					Deprecated.class)),
				Set.of()),
			arguments(
				named("getAllAnnotatedClassesFromSet with Deprecated",
					(PackageScan)packagePath -> AnnotationExtensions
						.getAllAnnotatedClassesFromSet(packagePath, Set.of(Deprecated.class))),
				Set.of()),
			arguments(named("getAllClasses", (PackageScan)AnnotationExtensions::getAllClasses),
				Set.of(InfoPlain.class)),
			arguments(
				named("scanForAllClasses",
					(PackageScan)packagePath -> AnnotationExtensions
						.scanForAllClasses(packageDirectory(InfoPlain.class), packagePath)),
				Set.of(InfoPlain.class)));
	}

	/**
	 * The package scans of {@link AnnotationExtensions}, each from a class directory and from a jar
	 *
	 * @param everyClass
	 *            the binary names of every class of the package
	 * @param importResourceClasses
	 *            the binary names of the classes that carry an {@link ImportResource}
	 * @return the scan, the kind of root and the binary names of the classes it finds, for each
	 *         case
	 */
	private static Stream<Arguments> packageScansOfEveryKindOfRoot(final Set<String> everyClass,
		final Set<String> importResourceClasses)
	{
		final Stream<Arguments> scans = Stream.of(
			arguments(named("getAllClasses", (PackageScan)AnnotationExtensions::getAllClasses),
				everyClass),
			arguments(
				named("getAllAnnotatedClasses with ImportResource",
					(PackageScan)packagePath -> AnnotationExtensions
						.getAllAnnotatedClasses(packagePath, ImportResource.class)),
				importResourceClasses),
			arguments(
				named("getAllAnnotatedClassesFromSet with ImportResource",
					(PackageScan)packagePath -> AnnotationExtensions
						.getAllAnnotatedClassesFromSet(packagePath, Set.of(ImportResource.class))),
				importResourceClasses),
			arguments(named("getAllAnnotatedClassesFromSet without an annotation set",
				(PackageScan)packagePath -> AnnotationExtensions
					.getAllAnnotatedClassesFromSet(packagePath, null)),
				everyClass),
			arguments(named("getAllClasses with ImportResource",
				(PackageScan)packagePath -> AnnotationExtensions.getAllClasses(packagePath,
					Set.of(ImportResource.class))),
				importResourceClasses));
		return scans.flatMap(scan -> Stream.of(RootKind.values())
			.map(rootKind -> arguments(scan.get()[0], rootKind, scan.get()[1])));
	}

	/**
	 * The directory scans of {@link AnnotationExtensions}, each over the class directory of
	 * {@link #nestedClassFiles()}: all classes count, the nested and anonymous ones included
	 *
	 * @return the scan and the binary names of the classes it finds, for each case
	 */
	static Stream<Arguments> allClassesDirectoryScans()
	{
		final Set<String> everyClass = Set.of("nest.pkg.Outer", "nest.pkg.Outer$Inner",
			"nest.pkg.Outer$1", "nest.pkg.sub.Deep", "nest.pkg.sub.Deep$Inner");
		final Set<String> importResourceClasses = Set.of("nest.pkg.Outer$Inner",
			"nest.pkg.sub.Deep$Inner");
		return Stream.of(
			arguments(
				named("scanForAllClasses", (DirectoryScan)AnnotationExtensions::scanForAllClasses),
				everyClass),
			arguments(named("scanForAllAnnotatedClasses without an annotation",
				(DirectoryScan)(folder, packagePath) -> AnnotationExtensions
					.scanForAllAnnotatedClasses(folder, packagePath, null)),
				everyClass),
			arguments(
				named("scanForAllAnnotatedClasses with ImportResource",
					(DirectoryScan)(folder, packagePath) -> AnnotationExtensions
						.scanForAllAnnotatedClasses(folder, packagePath, ImportResource.class)),
				importResourceClasses),
			arguments(named("scanForAllAnnotatedClassesFromSet without an annotation set",
				(DirectoryScan)(folder, packagePath) -> AnnotationExtensions
					.scanForAllAnnotatedClassesFromSet(folder, packagePath, null)),
				everyClass),
			arguments(named("scanForAllAnnotatedClassesFromSet with ImportResource",
				(DirectoryScan)(folder, packagePath) -> AnnotationExtensions
					.scanForAllAnnotatedClassesFromSet(folder, packagePath,
						Set.of(ImportResource.class))),
				importResourceClasses));
	}

	/**
	 * The classes of the directory scans for all classes: a top-level class with a nested class
	 * that carries an {@link ImportResource} and an anonymous class, and in a sub package a
	 * top-level class with an annotated nested class
	 *
	 * @return the class file of each class, by its binary name
	 */
	private static Map<String, byte[]> nestedClassFiles()
	{
		final Map<String, byte[]> classFiles = new LinkedHashMap<>();
		classFiles.put("nest.pkg.Outer", ClassFixtures.newClassFile("nest.pkg.Outer"));
		classFiles.put("nest.pkg.Outer$Inner",
			ClassFixtures.newImportResourceClassFile("nest.pkg.Outer$Inner", "Inner.css"));
		classFiles.put("nest.pkg.Outer$1", ClassFixtures.newClassFile("nest.pkg.Outer$1"));
		classFiles.put("nest.pkg.sub.Deep", ClassFixtures.newClassFile("nest.pkg.sub.Deep"));
		classFiles.put("nest.pkg.sub.Deep$Inner",
			ClassFixtures.newImportResourceClassFile("nest.pkg.sub.Deep$Inner", "Deep.css"));
		return classFiles;
	}

	/**
	 * The classes of the root package scans: two carry an {@link ImportResource}, one of them in a
	 * sub package, one is plain, one has the name the compiler gives a nested class, and one cannot
	 * be initialized
	 *
	 * @return the class file of each class, by its binary name
	 */
	private static Map<String, byte[]> rootClassFiles()
	{
		final Map<String, byte[]> classFiles = new LinkedHashMap<>();
		classFiles.put(ROOT_PACKAGE + ".Annotated",
			ClassFixtures.newImportResourceClassFile(ROOT_PACKAGE + ".Annotated", "Annotated.css"));
		classFiles.put(ROOT_PACKAGE + ".Plain",
			ClassFixtures.newClassFile(ROOT_PACKAGE + ".Plain"));
		classFiles.put(ROOT_PACKAGE + ".Plain$Inner",
			ClassFixtures.newClassFile(ROOT_PACKAGE + ".Plain$Inner"));
		classFiles.put(ROOT_PACKAGE + ".Uninitializable",
			ClassFixtures.newUninitializableClassFile(ROOT_PACKAGE + ".Uninitializable"));
		classFiles.put(ROOT_PACKAGE + ".sub.Deep",
			ClassFixtures.newImportResourceClassFile(ROOT_PACKAGE + ".sub.Deep", "Deep.css"));
		return classFiles;
	}

	/**
	 * The classes of the scans that skip a {@code package-info} and a {@code module-info}: a class,
	 * a package-info and a module-info in the package and a package-info in a sub package carry an
	 * {@link ImportResource}, so that a scan for it would find them if it took them for classes,
	 * and the sub package holds a plain class too
	 *
	 * @return the class file of each class, by its binary name
	 */
	private static Map<String, byte[]> infoClassFiles()
	{
		final Map<String, byte[]> classFiles = new LinkedHashMap<>();
		for (final String className : List.of(INFO_PACKAGE + ".Info",
			INFO_PACKAGE + ".package-info", INFO_PACKAGE + ".module-info",
			INFO_PACKAGE + ".sub.package-info"))
		{
			classFiles.put(className,
				ClassFixtures.newImportResourceClassFile(className, "Info.css"));
		}
		classFiles.put(INFO_PACKAGE + ".sub.Deep",
			ClassFixtures.newClassFile(INFO_PACKAGE + ".sub.Deep"));
		return classFiles;
	}

	private static File packageDirectory(final Class<?> classInThePackage) throws Exception
	{
		return Path.of(classInThePackage.getResource("").toURI()).toFile();
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

	@ParameterizedTest(name = "below \"{0}\", {1} URL")
	@MethodSource("io.github.astrapi69.io.annotation.ClassFixtures#urlEscapedDirectories")
	void getAllClasses_findsTheClassesOfADirectoryWhosePathHoldsACharacterThatAUrlEscapes(
		final String directoryName, final UrlStyle urlStyle) throws Exception
	{
		final Path root = ClassFixtures
			.newClassDirectory(directory.resolve(directoryName).resolve("root"), "plus.pkg.Plus");

		final Set<Class<?>> classes = ClassFixtures.withClassPathRoots(
			() -> AnnotationExtensions.getAllClasses("plus.pkg"), urlStyle.of(root));

		assertEquals(List.of("plus.pkg.Plus"), classes.stream().map(Class::getName).toList());
	}

	@ParameterizedTest(name = "below {0}")
	@ValueSource(strings = { "x!y", "!x", "x!y!z" })
	void getAllClasses_findsTheClassesOfAJarBelowADirectoryWhoseNameHoldsAnExclamationMark(
		final String directoryName) throws Exception
	{
		final Path jar = RootKind.JAR.write(directory.resolve(directoryName).resolve("root"),
			"bang.pkg.Bang");

		final Set<Class<?>> classes = ClassFixtures.withClassPathRoots(
			() -> AnnotationExtensions.getAllClasses("bang.pkg"), UrlStyle.ESCAPED.of(jar));

		assertEquals(List.of("bang.pkg.Bang"), classes.stream().map(Class::getName).toList());
	}

	@ParameterizedTest(name = "{0}, from a {1}")
	@MethodSource("rootScans")
	void getAllAnnotatedClasses_findsTheClassesOfAJarAsOfAClassDirectory(final PackageScan scan,
		final RootKind rootKind, final Set<String> expected) throws Exception
	{
		assertEquals(expected, scanRoot(scan, rootKind, rootClassFiles(), ROOT_PACKAGE));
	}

	/**
	 * A {@code package-info.class} holds the annotations of its package and a
	 * {@code module-info.class} the declaration of a module: neither is a class, so no scan returns
	 * one, from a class directory or from a jar, in the package or in a sub package, even when it
	 * carries the annotation the scan looks for
	 *
	 * @param scan
	 *            the package scan
	 * @param rootKind
	 *            the kind of class path root that holds the package
	 * @param expected
	 *            the binary names of the classes the scan finds
	 * @throws Exception
	 *             if the root cannot be written or the scan fails
	 */
	@ParameterizedTest(name = "{0}, from a {1}")
	@MethodSource("infoFileScans")
	void getAllAnnotatedClasses_skipsPackageInfoAndModuleInfo_fromEveryKindOfRoot(
		final PackageScan scan, final RootKind rootKind, final Set<String> expected)
		throws Exception
	{
		assertEquals(expected, scanRoot(scan, rootKind, infoClassFiles(), INFO_PACKAGE));
	}

	/**
	 * The {@code package-info.class} the compiler writes for a package annotated with
	 * {@link Deprecated} is no deprecated class: a package annotation is read with
	 * {@link Package#getAnnotation(Class)}
	 *
	 * @param scan
	 *            the scan over the package {@code testdata.info}
	 * @param expected
	 *            the classes the scan finds
	 * @throws Exception
	 *             if the scan fails
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("annotatedPackageScans")
	void getAllAnnotatedClasses_skipsThePackageInfoOfAnAnnotatedPackage(final PackageScan scan,
		final Set<Class<?>> expected) throws Exception
	{
		assertEquals(expected, scan.scan(InfoPlain.class.getPackageName()));
	}

	/**
	 * Writes a class path root of the given kind with the given class files and scans the given
	 * package through a context class loader that reads that root
	 *
	 * @param scan
	 *            the package scan
	 * @param rootKind
	 *            the kind of class path root to write
	 * @param classFiles
	 *            the class file of each class, by its binary name
	 * @param packagePath
	 *            the package to scan
	 * @return the binary names of the classes the scan finds
	 * @throws Exception
	 *             if the root cannot be written or the scan fails
	 */
	private Set<String> scanRoot(final PackageScan scan, final RootKind rootKind,
		final Map<String, byte[]> classFiles, final String packagePath) throws Exception
	{
		final Path root = rootKind.write(directory.resolve("root"), classFiles);

		final Set<Class<?>> classes = ClassFixtures.withClassPathRoots(() -> scan.scan(packagePath),
			UrlStyle.ESCAPED.of(root));

		return classes.stream().map(Class::getName).collect(Collectors.toUnmodifiableSet());
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("allClassesDirectoryScans")
	void scanForAllClasses_findsTheNestedAndAnonymousClassesToo(final DirectoryScan scan,
		final Set<String> expected) throws Exception
	{
		final Path root = ClassFixtures.newClassDirectory(directory.resolve("root"),
			nestedClassFiles());

		final Set<Class<?>> classes = ClassFixtures.withClassPathRoots(
			() -> scan.scan(root.resolve("nest/pkg").toFile(), "nest.pkg"),
			UrlStyle.ESCAPED.of(root));

		assertEquals(expected,
			classes.stream().map(Class::getName).collect(Collectors.toUnmodifiableSet()));
	}

	@Test
	void scanForAllClasses_findsEveryClassOfTheDirectory() throws Exception
	{
		assertEquals(ALL_SCANNED_CLASSES, AnnotationExtensions
			.scanForAllClasses(packageDirectory(ScannedPage.class), SCAN_PACKAGE));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("uninitializableClassScans")
	void scanForAllAnnotatedClasses_findsAClassThatCannotBeInitialized(
		final Class<? extends Annotation> annotationClass, final Set<Class<?>> expected)
		throws Exception
	{
		assertEquals(expected, AnnotationExtensions.scanForAllAnnotatedClasses(
			packageDirectory(FailingStaticInitializer.class), BROKEN_PACKAGE, annotationClass));
		assertEquals(expected,
			AnnotationExtensions.scanForAllAnnotatedClassesFromSet(
				packageDirectory(FailingStaticInitializer.class), BROKEN_PACKAGE,
				annotationClass == null ? null : Set.of(annotationClass)));
	}

	@Test
	void scanForAllAnnotatedClasses_isEmpty_forADirectoryThatDoesNotExist() throws Exception
	{
		final File missing = directory.resolve("missing").toFile();

		assertTrue(AnnotationExtensions.scanForAllAnnotatedClasses(missing, "missing", Marker.class)
			.isEmpty());
		assertTrue(AnnotationExtensions
			.scanForAllAnnotatedClassesFromSet(missing, "missing", Set.of(Marker.class)).isEmpty());
	}

	@Test
	void scanForAllAnnotatedClasses_throwsClassNotFoundException_forAClassFileNoClassLoaderKnows()
		throws Exception
	{
		Files.createFile(directory.resolve("Ghost.class"));
		final File ghostDirectory = directory.toFile();

		assertThrows(ClassNotFoundException.class,
			() -> AnnotationExtensions.scanForAllAnnotatedClasses(ghostDirectory,
				"io.github.astrapi69.no.such", Marker.class));
		assertThrows(ClassNotFoundException.class,
			() -> AnnotationExtensions.scanForAllAnnotatedClassesFromSet(ghostDirectory,
				"io.github.astrapi69.no.such", Set.of(Marker.class)));
	}

	/**
	 * One of the package scans of {@link AnnotationExtensions}
	 */
	@FunctionalInterface
	interface PackageScan
	{
		/**
		 * Scans the given package
		 *
		 * @param packagePath
		 *            the package to scan
		 * @return the classes the scan finds
		 * @throws Exception
		 *             if the scan fails
		 */
		Set<Class<?>> scan(String packagePath) throws Exception;
	}

	/**
	 * One of the directory scans of {@link AnnotationExtensions}
	 */
	@FunctionalInterface
	interface DirectoryScan
	{
		/**
		 * Scans the given directory of the given package
		 *
		 * @param folder
		 *            the directory of the package
		 * @param packagePath
		 *            the package the directory holds
		 * @return the classes the scan finds
		 * @throws Exception
		 *             if the scan fails
		 */
		Set<Class<?>> scan(File folder, String packagePath) throws Exception;
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
}
