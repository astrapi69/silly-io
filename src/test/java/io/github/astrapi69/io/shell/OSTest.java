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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit test class for {@link OS}
 */
public class OSTest
{

	/**
	 * The operating system name before each case: several cases set {@code os.name}, and the test
	 * classes that run after this one read it again
	 */
	private String operatingSystemName;

	@BeforeEach
	void keepTheOperatingSystemName()
	{
		operatingSystemName = System.getProperty("os.name");
	}

	@AfterEach
	void restoreTheOperatingSystemName()
	{
		System.setProperty("os.name", operatingSystemName);
	}

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

	@ParameterizedTest(name = "{0}: {1}")
	@CsvSource({ "Windows 11, WINDOWS, Windows, true, false, false",
			"Linux, LINUX, Linux, false, false, true", "Mac OS X, MAC, Mac OS, false, true, false",
			"Darwin, MAC, Mac OS, false, true, false", "FreeBSD, UNIX, Unix, false, false, false",
			"Unknown OS, OTHER, Other, false, false, false" })
	void theQueries_followTheOperatingSystemNameProperty(final String osName, final OS expectedOS,
		final String expectedName, final boolean windows, final boolean mac, final boolean linux)
	{
		System.setProperty("os.name", osName);

		assertEquals(expectedOS, OS.get());
		assertEquals(expectedName, OS.getOperatingSystemName());
		assertEquals(windows, OS.isWindows());
		assertEquals(mac, OS.isMac());
		assertEquals(linux, OS.isLinux());
	}

	@ParameterizedTest(name = "{0}: {1}")
	@CsvSource({ "Windows Server 2022, WINDOWS", "linux, LINUX", "darwin, MAC", "MacOS, MAC",
			"Plan 9, OTHER" })
	void getOperatingSystem_matchesTheGivenName_ratherThanTheProperty(final String osName,
		final OS expected)
	{
		System.setProperty("os.name", "Unknown OS");

		assertEquals(expected, OS.getOperatingSystem(osName));
	}

	/**
	 * How the case of the name counts: Mac OS and the Unix systems match in any case, Linux and
	 * Windows only with their first letter in either case and the rest in lower case, and the
	 * {@code |} that the character classes {@code [L|l]} and {@code [W|w]} hold stands for that
	 * first letter (#57)
	 *
	 * @param osName
	 *            the name
	 * @param expected
	 *            the operating system it names
	 */
	@ParameterizedTest(name = "{0}: {1}")
	@CsvSource({ "Linux, LINUX", "linux, LINUX", "LINUX, OTHER", "GNU/Linux, LINUX", "|inux, LINUX",
			"Windows 11, WINDOWS", "windows 11, WINDOWS", "WINDOWS 11, OTHER", "|indows, WINDOWS",
			"Mac OS X, MAC", "MAC OS X, MAC", "SunOS, UNIX", "SUNOS, UNIX" })
	void getOperatingSystem_andTheCaseOfTheName(final String osName, final OS expected)
	{
		assertEquals(expected, OS.getOperatingSystem(osName));
	}

	/**
	 * The Unix systems besides Linux and macOS, by the names their JDKs give them, as Apache
	 * Commons Lang's {@code SystemUtils.IS_OS_UNIX} counts them, and a name that says Unix; in
	 * either case
	 *
	 * @param osName
	 *            the name of a Unix system
	 */
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "FreeBSD", "OpenBSD", "NetBSD", "SunOS", "Solaris", "AIX", "HP-UX",
			"Irix", "Digital Unix", "freebsd" })
	void getOperatingSystem_returnsUnix_forAUnixSystem(final String osName)
	{
		assertEquals(OS.UNIX, OS.getOperatingSystem(osName));
	}

	@Test
	void versionAndArchitecture_areTheSystemProperties()
	{
		assertEquals(System.getProperty("os.version"), OS.getOperatingSystemVersion());
		assertEquals(System.getProperty("os.arch"), OS.getOperatingSystemArchitecture());
	}
}
