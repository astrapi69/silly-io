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
