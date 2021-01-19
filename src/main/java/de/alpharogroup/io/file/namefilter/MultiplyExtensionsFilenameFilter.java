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
package de.alpharogroup.io.file.namefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * The class {@link MultiplyExtensionsFilenameFilter} accepts File-objects that end with one of the
 * extension in the collection
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MultiplyExtensionsFilenameFilter implements FilenameFilter
{

	/** The recursive flag */
	private boolean recursive;

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
	 * @param recursive
	 *            the accept dir
	 */
	public MultiplyExtensionsFilenameFilter(final Collection<String> fileExtensions,
		final boolean recursive)
	{
		Objects.requireNonNull(fileExtensions);
		if (fileExtensions.isEmpty())
		{
			throw new IllegalArgumentException("Argument fileExtensions cant be empty. "
				+ "Please set the argument fileExtensions appropriate.");
		}
		this.fileExtensions = new ArrayList<>(fileExtensions.size());
		this.recursive = recursive;
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
		if (recursive && currentFile.isDirectory())
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
		buffer.append(recursive);
		buffer.append("]");
		return buffer.toString();
	}

}
