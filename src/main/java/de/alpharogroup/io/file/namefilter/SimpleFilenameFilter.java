package de.alpharogroup.io.file.namefilter;

import java.io.File;
import java.io.FilenameFilter;

import lombok.NonNull;

/**
 * The class {@link SimpleFilenameFilter}
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class SimpleFilenameFilter implements FilenameFilter
{

	/** The accept dir */
	private boolean acceptDir;

	/** The filesuffix */
	private String filesuffix;

	/**
	 * Instantiates a new {@link SimpleFilenameFilter}
	 *
	 * @param filesuffix
	 *            the filesuffix
	 * @param acceptDir
	 *            the accept dir
	 */
	public SimpleFilenameFilter(final @NonNull String filesuffix, final boolean acceptDir)
	{
		if (filesuffix.equals(""))
		{
			throw new IllegalArgumentException("Argument filesuffix cant be empty. "
				+ "Please set the argument filesuffix.");
		}
		this.filesuffix = filesuffix.toLowerCase();
		this.acceptDir = acceptDir;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File dir, final String name)
	{
		final File currentFile = new File(dir, name);
		if (acceptDir)
		{
			if (currentFile.isDirectory())
			{
				return true;
			}
		}
		return name.toLowerCase().endsWith(this.filesuffix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		buffer.append("[SimpleFilenameFilter:");
		buffer.append(" filesuffix: ");
		buffer.append(filesuffix);
		buffer.append(" acceptDir: ");
		buffer.append(acceptDir);
		buffer.append("]");
		return buffer.toString();
	}

}
