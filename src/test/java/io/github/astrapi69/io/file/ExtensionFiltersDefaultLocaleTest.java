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
package io.github.astrapi69.io.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.io.file.filter.MultiplyExtensionsFileFilter;
import io.github.astrapi69.io.file.filter.SuffixFileFilter;
import io.github.astrapi69.io.file.namefilter.MultiplyExtensionsFilenameFilter;
import io.github.astrapi69.io.file.namefilter.SimpleFilenameFilter;

/**
 * The four filters that compare an extension without its case match the same files whatever the
 * default locale is. In the Turkish locale an upper-case I lowercases to a dotless i, so a filter
 * that lowercases with the default locale reads the extension {@code .INI} as {@code .ını} and
 * matches no {@code config.ini}. Each filter is built and asked while the default locale is
 * {@code tr-TR}; the files are created in a temporary directory and listed through the filter
 */
class ExtensionFiltersDefaultLocaleTest
{

	private static final Locale TURKISH = Locale.forLanguageTag("tr-TR");

	@TempDir
	Path directory;

	static Stream<Arguments> extensionFilters()
	{
		return Stream.of(
			arguments(named("SuffixFileFilter",
				(ExtensionListing)(folder, extension) -> namesOf(
					folder.listFiles(new SuffixFileFilter(extension, false))))),
			arguments(named("MultiplyExtensionsFileFilter",
				(ExtensionListing)(folder, extension) -> namesOf(
					folder.listFiles(new MultiplyExtensionsFileFilter(extension))))),
			arguments(named("MultiplyExtensionsFilenameFilter",
				(ExtensionListing)(folder, extension) -> namesOf(
					folder.listFiles(new MultiplyExtensionsFilenameFilter(List.of(extension)))))),
			arguments(named("SimpleFilenameFilter", (ExtensionListing)(folder,
				extension) -> namesOf(folder.listFiles(new SimpleFilenameFilter(extension))))));
	}

	/**
	 * The extension of the filter, the name of the file and whether the filter accepts it, for
	 * every filter: an upper-case I on either side or on both, and a name with another extension
	 *
	 * @return the cases
	 */
	static Stream<Arguments> extensionFilterCases()
	{
		final List<Arguments> cases = List.of(arguments(".INI", "config.ini", true),
			arguments(".ini", "CONFIG.INI", true), arguments(".Ini", "config.iNi", true),
			arguments(".INI", "CONFIG.INI", true), arguments(".INI", "config.inf", false));
		return extensionFilters()
			.flatMap(filter -> cases.stream().map(fileCase -> arguments(filter.get()[0],
				fileCase.get()[0], fileCase.get()[1], fileCase.get()[2])));
	}

	private static Set<String> namesOf(final File[] files)
	{
		return Arrays.stream(files).map(File::getName).collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * Gives what the given action gives while the default locale is the given one, and restores the
	 * previous default locale and its categories afterwards
	 *
	 * @param <T>
	 *            the type of the result
	 * @param locale
	 *            the default locale to run with
	 * @param action
	 *            the action to run
	 * @return the result of the action
	 */
	private static <T> T inTheDefaultLocale(final Locale locale, final Supplier<T> action)
	{
		final Locale previous = Locale.getDefault();
		final Locale previousDisplay = Locale.getDefault(Locale.Category.DISPLAY);
		final Locale previousFormat = Locale.getDefault(Locale.Category.FORMAT);
		Locale.setDefault(locale);
		try
		{
			return action.get();
		}
		finally
		{
			Locale.setDefault(previous);
			Locale.setDefault(Locale.Category.DISPLAY, previousDisplay);
			Locale.setDefault(Locale.Category.FORMAT, previousFormat);
		}
	}

	@ParameterizedTest(name = "{0}, extension {1}, file {2}: {3}")
	@MethodSource("extensionFilterCases")
	void accept_ignoresTheCaseOfTheExtension_inTheTurkishDefaultLocale(
		final ExtensionListing listing, final String extension, final String fileName,
		final boolean accepted) throws IOException
	{
		Files.createFile(directory.resolve(fileName));

		final Set<String> listed = inTheDefaultLocale(TURKISH,
			() -> listing.list(directory.toFile(), extension));

		assertEquals(accepted, listed.contains(fileName), () -> "listed: " + listed);
	}

	/**
	 * Lists the names of the files of a directory that one of the extension filters, built for the
	 * given extension, accepts
	 */
	@FunctionalInterface
	interface ExtensionListing
	{
		/**
		 * Lists the files of the given directory through a filter for the given extension
		 *
		 * @param folder
		 *            the directory to list
		 * @param extension
		 *            the extension of the filter
		 * @return the names of the files the filter accepts
		 */
		Set<String> list(File folder, String extension);
	}
}
