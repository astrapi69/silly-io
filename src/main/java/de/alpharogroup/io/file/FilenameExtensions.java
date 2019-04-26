package de.alpharogroup.io.file;

import java.io.File;

import lombok.experimental.UtilityClass;

/**
 * The class {@link FilenameExtensions}.
 */
@UtilityClass
public final class FilenameExtensions
{

	/**
	 * Gets the filename with the absolute path prefix.
	 *
	 * @param file
	 *            the file.
	 * @return the filename prefix.
	 */
	public static String getFilenamePrefix(final File file)
	{
		final String fileName = file.getAbsolutePath();
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

	/**
	 * Gets the filename suffix or null if no suffix exists or the given file object is a directory.
	 *
	 * @param file
	 *            the file.
	 * @return 's the filename suffix or null if no suffix exists or the given file object is a
	 *         directory.
	 */
	public static String getFilenameSuffix(final File file)
	{
		if (!file.isDirectory())
		{
			final String fileName = file.getAbsolutePath();
			final int ext_index = fileName.lastIndexOf(".");
			final String fileNameSuffix;
			if (ext_index != -1)
			{
				fileNameSuffix = fileName.substring(ext_index, fileName.length());
			}
			else
			{
				fileNameSuffix = null;
			}
			return fileNameSuffix;
		}
		return null;
	}

	/**
	 * Gets the filename without the extension or null if the given file object is a directory.
	 *
	 * @param file
	 *            the file.
	 * @return the filename without the extension or null if the given file object is a directory.
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
	 * Gets the filename without the extension or null if the given file object is a directory.
	 *
	 * @param fileName
	 *            the file.
	 * @return the filename without the extension or null if the given file object is a directory.
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
