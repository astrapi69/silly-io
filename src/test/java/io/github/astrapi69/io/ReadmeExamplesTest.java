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
package io.github.astrapi69.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.example.model.Customer;
import com.example.model.Entity;
import com.example.model.Order;

import io.github.astrapi69.io.annotation.AnnotationExtensions;
import io.github.astrapi69.io.annotation.ScanClassExtensions;
import io.github.astrapi69.io.file.CaseSensitivity;
import io.github.astrapi69.io.file.FilenameExtensions;
import io.github.astrapi69.io.file.filter.MultiplyExtensionsFileFilter;
import io.github.astrapi69.io.file.filter.PrefixFileFilter;
import io.github.astrapi69.io.file.filter.SuffixFileFilter;
import io.github.astrapi69.io.file.namefilter.SimpleFilenameFilter;
import io.github.astrapi69.io.shell.OS;

/**
 * The examples of the README's "Quick start", one test for each block: the code between the markers
 * is the README's, and the assertions are the values its comments name. A change to either side has
 * to be made on both
 */
class ReadmeExamplesTest
{

	@TempDir
	File directory;

	private static List<String> namesOf(final File[] files)
	{
		return Arrays.stream(files).map(File::getName).sorted().toList();
	}

	private void createFiles(final String... names) throws IOException
	{
		for (final String name : names)
		{
			new File(directory, name).createNewFile();
		}
	}

	@Test
	void streamsReadersAndWriters() throws IOException
	{
		// README: begin
		File report = new File(directory, "report.txt");
		try (Writer writer = StreamExtensions.getWriter(report)) // creates the file
		{
			writer.write("hello");
		}
		byte[] content = StreamExtensions.getByteArray(report); // "hello", and closes the file
		// README: end

		assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), content);
		assertThrows(FileNotFoundException.class, () -> {
			// README: begin
			try (InputStream in = StreamExtensions
				.getInputStream(new File(directory, "missing.txt").toPath()))
			{
				// ...
			}
			// README: end
		});
	}

	@Test
	void fileAndFilenameFilters() throws IOException
	{
		createFiles("Report-2026.txt", "notes.xml", "Main.java");

		// README: begin
		File[] texts = directory.listFiles(SuffixFileFilter.of(".txt", false));
		File[] sources = directory.listFiles(MultiplyExtensionsFileFilter.of(".java", ".xml"));
		File[] reports = directory
			.listFiles(PrefixFileFilter.of("report", false, CaseSensitivity.INSENSITIVE));
		String[] xml = directory.list(SimpleFilenameFilter.of(".xml"));
		// README: end

		assertEquals(List.of("Report-2026.txt"), namesOf(texts));
		assertEquals(List.of("Main.java", "notes.xml"), namesOf(sources));
		assertEquals(List.of("Report-2026.txt"), namesOf(reports));
		assertArrayEquals(new String[] { "notes.xml" }, xml);
	}

	@Test
	void fileNames()
	{
		// README: begin
		File report = new File("Report-2026.txt");
		// README: end

		assertEquals("txt", FilenameExtensions.getFileExtension(report));
		assertEquals("Report-2026", FilenameExtensions.getFilenameWithoutExtension(report));
		assertEquals("Report-2026.bak",
			FilenameExtensions.getFilenameWithNewExtension(report, ".bak"));
	}

	@Test
	void serialization() throws Exception
	{
		File file = new File(directory, "names.ser");

		// README: begin
		ArrayList<String> names = new ArrayList<>(List.of("Ada", "Grace"));
		SerializedObjectExtensions.writeSerializedObjectToFile(names, file); // throws if it cannot
		Object read = SerializedObjectExtensions.readSerializedObjectFromFile(file); // [Ada, Grace]
		String base64 = Serializer.toBase64EncodedString("hi"); // "rO0ABXQAAmhp"
		// README: end

		assertEquals(List.of("Ada", "Grace"), read);
		assertEquals("rO0ABXQAAmhp", base64);
	}

	@Test
	void classesAndAnnotations() throws Exception
	{
		// README: begin
		Set<Class<?>> classes = ScanClassExtensions.scanTopLevelClasses("com.example.model");
		Set<Class<?>> entities = AnnotationExtensions.getAllAnnotatedClasses("com.example.model",
			Entity.class);
		Entity entity = AnnotationExtensions.getAnnotation(Customer.class, Entity.class);
		// README: end

		assertEquals(Set.of(Customer.class, Entity.class, Order.class), classes);
		assertEquals(Set.of(Customer.class), entities);
		assertNotNull(entity);
	}

	@Test
	void theOperatingSystem()
	{
		// README: begin
		OS os = OS.get(); // LINUX, MAC, UNIX, WINDOWS or OTHER
		boolean linux = OS.isLinux();
		// README: end

		assertEquals(os == OS.LINUX, linux);
	}
}
