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

import java.util.regex.Pattern;

/**
 * The Enum OS provides constants for identifying the operating system
 */
public enum OS
{

	/** Constant for Linux operating systems */
	LINUX("Linux"),

	/** Constant for Mac OS operating systems */
	MAC("Mac OS"),

	/** Constant for other operating systems */
	OTHER("Other"),

	/** Constant for Unix operating systems */
	UNIX("Unix"),

	/** Constant for Windows operating systems */
	WINDOWS("Windows");

	/** Pattern to match Linux OS names */
	private static final Pattern LINUX_PATTERN = Pattern.compile(".*[L|l]inux.*");

	/** Pattern to match Mac OS names */
	private static final Pattern MAC_PATTERN = Pattern.compile(".*(mac|darwin).*",
		Pattern.CASE_INSENSITIVE);

	/** System property name for OS name */
	private static final String OS_NAME = "os.name";

	/** System property name for an OS version */
	private static final String OS_VERSION = "os.version";

	/** System property name for OS architecture */
	private static final String OS_ARCHITECTURE = "os.arch";

	/** Pattern to match Windows OS names */
	private static final Pattern WINDOWS_PATTERN = Pattern.compile(".*[W|w]indows.*");

	/** The OS name */
	private final String os;

	/**
	 * Constructor with OS name
	 *
	 * @param os
	 *            the OS name
	 */
	OS(final String os)
	{
		this.os = os;
	}

	/**
	 * Gets the current operating system
	 *
	 * @return the current operating system as an OS enum constant
	 */
	public static OS get()
	{
		return getOperatingSystem();
	}

	/**
	 * Returns true if the operating system is Windows
	 *
	 * @return true if Windows, otherwise false
	 */
	public static boolean isWindows()
	{
		return getOperatingSystemName().startsWith(OS.WINDOWS.getOs());
	}

	/**
	 * Returns true if the operating system is macOS
	 *
	 * @return true if macOS, otherwise false
	 */
	public static boolean isMac()
	{
		return getOperatingSystemName().startsWith(OS.MAC.getOs());
	}

	/**
	 * Returns true if the operating system is Linux
	 *
	 * @return true if Linux, otherwise false
	 */
	public static boolean isLinux()
	{
		return getOperatingSystemName().startsWith(OS.LINUX.getOs());
	}

	/**
	 * Gets the operating system by matching OS name patterns
	 *
	 * @return the operating system as an OS enum constant
	 */
	public static OS getOperatingSystem()
	{
		return getOperatingSystem(null);
	}

	/**
	 * Gets the operating system by matching OS name patterns. If an OS name is provided, it uses
	 * that value; otherwise, it uses the system property.
	 *
	 * @param osName
	 *            the name of the operating system to check, or null to use the system property
	 * @return the operating system as an OS enum constant
	 */
	public static OS getOperatingSystem(String osName)
	{
		String name = osName != null ? osName : System.getProperty(OS_NAME);
		if (WINDOWS_PATTERN.matcher(name).matches())
		{
			return WINDOWS;
		}
		else if (LINUX_PATTERN.matcher(name).matches())
		{
			return LINUX;
		}
		else if (MAC_PATTERN.matcher(name).matches())
		{
			return MAC;
		}
		else
		{
			return OTHER;
		}
	}

	/**
	 * Gets the operating system name
	 *
	 * @return the operating system name as a String
	 */
	public static String getOperatingSystemName()
	{
		return get().getOs();
	}

	/**
	 * Gets the operating system version
	 *
	 * @return the operating system version as a String
	 */
	public static String getOperatingSystemVersion()
	{
		return System.getProperty(OS_VERSION);
	}

	/**
	 * Gets the operating system architecture
	 *
	 * @return the operating system architecture as a String
	 */
	public static String getOperatingSystemArchitecture()
	{
		return System.getProperty(OS_ARCHITECTURE);
	}

	/**
	 * Gets the OS name of this enum constant
	 *
	 * @return the OS name as a String
	 */
	public String getOs()
	{
		return os;
	}
}
