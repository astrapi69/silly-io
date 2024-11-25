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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The class {@link FilenameExtensions}
 */
public final class FilenameExtensions
{

	private FilenameExtensions()
	{
	}

	/**
	 * Creates a map of character replacements for sanitizing filenames.
	 * <p>
	 * This map is used to replace characters in filenames that are invalid or problematic in file
	 * systems. The replacements ensure that filenames are safe and compatible across different
	 * operating systems.
	 * </p>
	 *
	 * <p>
	 * Replacements include:
	 * <ul>
	 * <li>Spaces (' ') are replaced with underscores ('_').</li>
	 * <li>Periods ('.') are replaced with hyphens ('-') except for the last one (used for
	 * extensions).</li>
	 * <li>Forward slashes ('/') are replaced with hyphens ('-').</li>
	 * <li>Backslashes ('\\') are replaced with hyphens ('-').</li>
	 * <li>Colons (':') are replaced with hyphens ('-').</li>
	 * <li>Asterisks ('*') are replaced with underscores ('_').</li>
	 * <li>Question marks ('?') are replaced with underscores ('_').</li>
	 * <li>Double quotes ('"') are replaced with underscores ('_').</li>
	 * <li>Less-than signs ('&lt;') are replaced with underscores ('_').</li>
	 * <li>Greater-than signs ('&gt;') are replaced with underscores ('_').</li>
	 * <li>Pipes ('|') are replaced with underscores ('_').</li>
	 * </ul>
	 *
	 * <p>
	 * Example usage:
	 * </p>
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Map&lt;Character, String&gt; replacementMap = getCharacterFileReplacementMap();
	 * 	String sanitizedFilename = sanitizeFilename("A &lt;fancy&gt; file|name?.txt", replacementMap);
	 * 	// Result: "A_-fancy_-file_name_.txt"
	 * }
	 * </pre>
	 *
	 * @return a map where the keys are characters to be replaced, and the values are their
	 *         replacements
	 */
	public static Map<Character, String> getCharacterFileReplacementMap()
	{
		Map<Character, String> replacementMap = new HashMap<>();
		replacementMap.put(' ', "_"); // Replace spaces with underscores
		replacementMap.put('.', "-"); // Replace periods with hyphens (except the last one for
										// extensions)
		replacementMap.put('/', "-"); // Replace forward slash
		replacementMap.put('\\', "-"); // Replace backslash
		replacementMap.put(':', "-"); // Replace colon
		replacementMap.put('*', "_"); // Replace asterisk
		replacementMap.put('?', "_"); // Replace question mark
		replacementMap.put('"', "_"); // Replace double quotes
		replacementMap.put('<', "_"); // Replace less-than sign
		replacementMap.put('>', "_"); // Replace greater-than sign
		replacementMap.put('|', "_"); // Replace pipe
		return replacementMap;
	}

	/**
	 * Sanitizes a given filename by replacing specified characters using a replacement map and
	 * preserving the file extension.
	 *
	 * <p>
	 * The method processes the filename in two parts:
	 * <ul>
	 * <li>The name part: characters are replaced according to the replacement map.</li>
	 * <li>The extension part: the portion after the last period (.) is preserved unchanged.</li>
	 * </ul>
	 *
	 * <p>
	 * For example, given the input filename <code>"A fancy.filename.txt"</code> and a replacement
	 * map:
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Map&lt;Character, String&gt; replacementMap = new HashMap&lt;&gt;();
	 * 	replacementMap.put(' ', "-");
	 * 	replacementMap.put('.', "_");
	 * 	replacementMap.put('a', "@");
	 * }
	 * </pre>
	 * 
	 * The output will be: <code>"@-f@ncy_filename.txt"</code>.
	 *
	 * @param filename
	 *            the original filename to sanitize
	 * @param replacementMap
	 *            a map containing characters to replace as keys and their replacements as values;
	 *            replacements are case-insensitive
	 * @return the sanitized filename with the file extension preserved
	 * @throws NullPointerException
	 *             if the filename or replacement map is null
	 */
	public static String sanitizeFilename(String filename, Map<Character, String> replacementMap)
	{
		StringBuilder sanitized = new StringBuilder();

		// Locate the last period (.) to preserve the file extension
		int lastDotIndex = filename.lastIndexOf('.');
		String namePart = (lastDotIndex != -1) ? filename.substring(0, lastDotIndex) : filename;
		String extensionPart = (lastDotIndex != -1) ? filename.substring(lastDotIndex) : "";

		// Replace characters in the name part
		for (char c : namePart.toCharArray())
		{
			char lowerCaseChar = Character.toLowerCase(c);
			if (replacementMap.containsKey(lowerCaseChar))
			{
				sanitized.append(replacementMap.get(lowerCaseChar));
			}
			else
			{
				sanitized.append(c);
			}
		}

		// Append the extension part unchanged
		sanitized.append(extensionPart);

		return sanitized.toString();
	}

	/**
	 * Gets the filename with the absolute path prefix
	 *
	 * @param file
	 *            the file
	 * @return the filename prefix
	 */
	public static String getFilenamePrefix(final File file)
	{
		final String fileName = file.getAbsolutePath();
		final int filenameEndIndex = fileName.lastIndexOf(".");
		final String fileNamePrefix;
		if (filenameEndIndex != -1)
		{
			fileNamePrefix = fileName.substring(0, filenameEndIndex);
		}
		else
		{
			fileNamePrefix = fileName;
		}
		return fileNamePrefix;
	}

	/**
	 * Gets the filename
	 *
	 * @param file
	 *            the file
	 * @return the filename
	 */
	public static String getName(final File file)
	{
		final String fileName = file.getName();
		return fileName;
	}

	/**
	 * Gets the file extension or an empty String if no file extension exists or the given file
	 * object is a directory
	 *
	 * @param file
	 *            the file
	 * @return 's the file extension or an empty String if no file extension exists or the given
	 *         file object is a directory
	 */
	public static String getFileExtension(final File file)
	{
		return getFileExtension(file, false);
	}

	/**
	 * Gets the file extension or an empty String if no file extension exists or the given file
	 * object is a directory
	 *
	 * @param file
	 *            the file
	 * @param withDot
	 *            the flag that indicates if the extension shell have a dot at the start
	 * @return 's the file extension or an empty String if no file extension exists or the given
	 *         file object is a directory
	 */
	public static String getFileExtension(final File file, boolean withDot)
	{
		String fileNameSuffix = "";
		if (!file.isDirectory())
		{
			final String fileName = file.getName();
			final int filenameEndIndex = fileName.lastIndexOf(".");

			if (filenameEndIndex != -1)
			{
				int beginIndex = withDot ? filenameEndIndex : filenameEndIndex + 1;
				fileNameSuffix = fileName.substring(beginIndex);
			}
		}
		return fileNameSuffix;
	}

	/**
	 * Gets the filename suffix or null if no suffix exists or the given file object is a directory
	 *
	 * @param file
	 *            the file
	 * @return 's the filename suffix or null if no suffix exists or the given file object is a
	 *         directory
	 */
	public static String getFilenameSuffix(final File file)
	{
		String fileNameSuffix = null;
		if (!file.isDirectory())
		{
			final String fileName = file.getAbsolutePath();
			final int filenameEndIndex = fileName.lastIndexOf(".");
			if (filenameEndIndex != -1)
			{
				fileNameSuffix = fileName.substring(filenameEndIndex, fileName.length());
			}
		}
		return fileNameSuffix;
	}

	/**
	 * Gets the filename without the extension or null if the given file object is a directory
	 *
	 * @param file
	 *            the file
	 * @return the filename without the extension or null if the given file object is a directory
	 */
	public static String getFilenameWithoutExtension(final File file)
	{
		if (!file.isDirectory())
		{
			final String fileName = file.getName();
			return getFilenameWithoutExtension(fileName);
		}
		return null;
	}

	/**
	 * Gets the filename with the new extension or null if the given file object is a directory
	 *
	 * @param file
	 *            the file
	 * @param newExtension
	 *            the new extension
	 * @return the filename with the new extension or null if the given file object is a directory
	 */
	public static String getFilenameWithNewExtension(final File file, final String newExtension)
	{
		Objects.requireNonNull(file, "File must not be null");
		Objects.requireNonNull(newExtension, "The new extension must not be null");
		String fileName = FilenameExtensions.getFilenameWithoutExtension(file);
		if (fileName != null)
		{
			String newFileName = fileName + newExtension;
			return newFileName;
		}
		return null;
	}

	/**
	 * Gets the filename with the new extension or null if the given file object is a directory
	 *
	 * @param file
	 *            the file
	 * @param newExtension
	 *            the new extension
	 * @return the filename with the new extension or null if the given file object is a directory
	 */
	public static String getFilenameWithNewExtension(final File file,
		final FileExtension newExtension)
	{
		String fileName = FilenameExtensions.getFilenameWithoutExtension(file);
		if (fileName != null)
		{
			String newFileName = fileName + newExtension.getExtension();
			return newFileName;
		}
		return null;
	}

	/**
	 * Gets the filename without the extension or null if the given file object is a directory
	 *
	 * @param fileName
	 *            the file
	 * @return the filename without the extension or null if the given file object is a directory
	 */
	public static String getFilenameWithoutExtension(final String fileName)
	{
		final int ext_index = fileName.lastIndexOf(".");
		final String fileNamePrefix;
		if (ext_index != -1)
		{
			fileNamePrefix = fileName.substring(0, ext_index);
		}
		else
		{
			fileNamePrefix = fileName;
		}
		return fileNamePrefix;
	}

}
