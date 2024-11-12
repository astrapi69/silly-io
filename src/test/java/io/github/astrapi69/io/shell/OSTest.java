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
package io.github.astrapi69.io.shell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit test class for {@link OS}
 */
public class OSTest
{

	/**
	 * Tests the {@link OS#get()} method to verify that it returns the correct OS enum constant
	 */
	@Test
	@DisplayName("Test get() method")
	void testGet()
	{
		OS currentOS = OS.get();
		assertTrue(currentOS != null, "get() should return a non-null OS constant");
	}

	/**
	 * Tests the {@link OS#isWindows()} method to verify it returns true when the OS is Windows
	 */
	@Test
	@DisplayName("Test isWindows() method")
	void testIsWindows()
	{
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
		{
			assertTrue(OS.isWindows(), "isWindows() should return true on Windows OS");
		}
	}

	/**
	 * Tests the {@link OS#isMac()} method to verify it returns true when the OS is Mac OS
	 */
	@Test
	@DisplayName("Test isMac() method")
	void testIsMac()
	{
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
		{
			assertTrue(OS.isMac(), "isMac() should return true on Mac OS");
		}
	}

	/**
	 * Tests the {@link OS#isLinux()} method to verify it returns true when the OS is Linux
	 */
	@Test
	@DisplayName("Test isLinux() method")
	void testIsLinux()
	{
		if (System.getProperty("os.name").toLowerCase().contains("linux"))
		{
			assertTrue(OS.isLinux(), "isLinux() should return true on Linux OS");
		}
	}

	/**
	 * Parameterized test for the {@link OS#getOperatingSystem()} method with different OS names
	 *
	 * @param osName
	 *            the OS name to test
	 * @param expectedOS
	 *            the expected OS enum constant
	 */
	@ParameterizedTest
	@CsvSource({ "Windows 10, WINDOWS", "Linux, LINUX", "Mac OS X, MAC", "Unknown OS, OTHER" })
	@DisplayName("Test getOperatingSystem() with different OS names")
	void testGetOperatingSystem(String osName, OS expectedOS)
	{
		System.setProperty("os.name", osName);
		assertEquals(expectedOS, OS.getOperatingSystem(),
			"getOperatingSystem() should return the expected OS constant");
	}

	/**
	 * Provides OS names and expected OS enum constants for parameterized tests
	 *
	 * @return stream of OS names and expected results
	 */
	static Stream<Object[]> provideOperatingSystems()
	{
		return Stream.of(new Object[] { "Windows 10", OS.WINDOWS },
			new Object[] { "Linux", OS.LINUX }, new Object[] { "Mac OS X", OS.MAC },
			new Object[] { "Unknown OS", OS.OTHER });
	}

	/**
	 * Parameterized test for {@link OS#getOperatingSystem()} with OS names from a method source
	 *
	 * @param osName
	 *            the OS name to test
	 * @param expectedOS
	 *            the expected OS enum constant
	 */
	@ParameterizedTest
	@MethodSource("provideOperatingSystems")
	@DisplayName("Test getOperatingSystem() with MethodSource")
	void testGetOperatingSystemMethodSource(String osName, OS expectedOS)
	{
		System.setProperty("os.name", osName);
		assertEquals(expectedOS, OS.getOperatingSystem(),
			"getOperatingSystem() should return the expected OS constant");
	}

}
