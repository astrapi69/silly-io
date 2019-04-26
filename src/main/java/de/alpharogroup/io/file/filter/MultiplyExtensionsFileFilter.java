package de.alpharogroup.io.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The class {@link MultiplyExtensionsFileFilter} accepts File-objects that are directories or end
 * with one of the extension in the collection. Accepts directories to allow search files recursive
 * in directories.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MultiplyExtensionsFileFilter implements FileFilter
{

	/** The accept dir. */
	private boolean acceptDir;

	/** The file extensions. */
	private Set<String> fileExtensions;

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}.
	 *
	 *
	 * @param acceptDir
	 *            Flag to accept directories.
	 * @param fileExtensions
	 *            the file extensions
	 */
	public MultiplyExtensionsFileFilter(final boolean acceptDir, final String... fileExtensions)
	{
		this(Arrays.asList(fileExtensions), acceptDir);
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}.
	 *
	 * @param fileExtensions
	 *            the file extensions
	 */
	public MultiplyExtensionsFileFilter(final Collection<String> fileExtensions)
	{
		this(fileExtensions, false);
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}.
	 *
	 * @param fileExtensions
	 *            the file extensions.
	 * @param acceptDir
	 *            Flag to accept directories.
	 */
	public MultiplyExtensionsFileFilter(final Collection<String> fileExtensions,
		final boolean acceptDir)
	{
		if (null == fileExtensions || fileExtensions.isEmpty())
		{
			throw new IllegalArgumentException("Argument fileExtensions cant be null or empty. "
				+ "Please set the argument fileExtensions appropriate.");
		}
		this.acceptDir = acceptDir;
		this.fileExtensions = new LinkedHashSet<>();
		for (final String extension : fileExtensions)
		{
			this.fileExtensions.add(extension.toLowerCase());
		}
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}.
	 *
	 * @param fileExtensions
	 *            the file extensions
	 */
	public MultiplyExtensionsFileFilter(final String... fileExtensions)
	{
		this(false, fileExtensions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File pathname)
	{
		if (acceptDir && pathname.isDirectory())
		{
			return true;
		}
		final String filename = pathname.getName();
		for (final String extension : fileExtensions)
		{
			if (filename.endsWith(extension))
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
		buffer.append("[MultiplyExtensionsFileFilter:");
		buffer.append(" fileExtensions: ");
		buffer.append(fileExtensions);
		buffer.append(" acceptDir: ");
		buffer.append(acceptDir);
		buffer.append("]");
		return buffer.toString();
	}

}
