package de.alpharogroup.io.file.filter;

import de.alpharogroup.lang.ClassExtensions;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * The unit test class for the class {@link PrefixFileFilter}
 */
public class PrefixFileFilterTest
{

	@Test public void testAccept() throws URISyntaxException
	{
		boolean expected;
		boolean actual;
		String prefix;
		boolean recursive;
		FileFilter prefixFileFilter;
		String filename;
		String filepath;
		File file;
		File dir;

		prefix = "resourc";
		prefixFileFilter = new PrefixFileFilter(prefix);
		assertNotNull(prefixFileFilter);

		filename = "resources.properties";

		filepath = "de/alpharogroup/lang/" + filename;

		file = ClassExtensions.getResourceAsFile(filepath);
		dir = file.getParentFile();

		actual = prefixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		prefix = "resourc";
		recursive = true;
		prefixFileFilter = new PrefixFileFilter(prefix, true);
		assertNotNull(prefixFileFilter);

		actual = prefixFileFilter.accept(file);
		expected = true;
		assertEquals(expected, actual);

		actual = prefixFileFilter.accept(dir);
		expected = true;
		assertEquals(expected, actual);
	}
}
