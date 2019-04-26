package de.alpharogroup.io.file.filter;

import java.io.File;
import java.io.FileFilter;

import de.alpharogroup.io.file.FileExtension;


/**
 * The class ClassFileFilter accepts File-objects that are directories or end with the suffix
 * '.class'. Accepts directories to allow search files recursive in directories.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ClassFileFilter implements FileFilter
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File pathname)
	{
		// To allow recursive search we use file.isDirectory()-method.
		if (pathname.isDirectory())
		{
			return true;
		}
		else
		{
			final String fileName = pathname.getName().toLowerCase();
			if (fileName.endsWith(FileExtension.CLASS.getExtension()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		buffer.append("[ClassFileFilter:");
		buffer.append(FileExtension.CLASS.getExtension());
		buffer.append("]");
		return buffer.toString();
	}

}
