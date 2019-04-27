package de.alpharogroup.io.file.namefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;

import lombok.NonNull;

/**
 * The class {@link MultiplyExtensionsFilenameFilter} accepts File-objects that end with one of the
 * extension in the collection
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MultiplyExtensionsFilenameFilter implements FilenameFilter
{

	/** The accept dir */
	private boolean acceptDir;

	/** The file extensions */
	private Collection<String> fileExtensions;

	/**
	 * Instantiates a new {@link MultiplyExtensionsFilenameFilter}
	 *
	 * @param fileExtensions
	 *            the file extensions
	 */
	public MultiplyExtensionsFilenameFilter(final Collection<String> fileExtensions)
	{
		this(fileExtensions, false);
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFilenameFilter}
	 *
	 * @param fileExtensions
	 *            the file extensions
	 * @param acceptDir
	 *            the accept dir
	 */
	public MultiplyExtensionsFilenameFilter(final @NonNull Collection<String> fileExtensions,
		final boolean acceptDir)
	{
		if (fileExtensions.isEmpty())
		{
			throw new IllegalArgumentException("Argument fileExtensions cant be empty. "
				+ "Please set the argument fileExtensions appropriate.");
		}
		this.fileExtensions = new ArrayList<>(fileExtensions.size());
		this.acceptDir = acceptDir;
		for (final String extension : fileExtensions)
		{
			this.fileExtensions.add(extension.toLowerCase());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File dir, final String name)
	{
		final File currentFile = new File(dir, name);
		if (acceptDir && currentFile.isDirectory())
		{
			return true;
		}
		for (final String extension : fileExtensions)
		{
			if (name.endsWith(extension))
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
		buffer.append("[MultiplyExtensionsFilenameFilter:");
		buffer.append(" fileExtensions: ");
		buffer.append(fileExtensions);
		buffer.append(" acceptDir: ");
		buffer.append(acceptDir);
		buffer.append("]");
		return buffer.toString();
	}

}
