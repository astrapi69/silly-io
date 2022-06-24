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
package io.github.astrapi69.io.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * The class {@link MultiplyExtensionsFileFilter} accepts File-objects that are directories or end
 * with one of the extension in the collection. Accepts directories to allow search files recursive
 * in directories
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MultiplyExtensionsFileFilter implements FileFilter
{

	/** The recursive flag, if the recursive flag is true the filter will be executed recursively */
	private final boolean recursive;

	/** The collection of the file extensions */
	private final Collection<String> fileExtensions;

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @param fileExtensions
	 *            the optional arguments of the file extensions
	 */
	public MultiplyExtensionsFileFilter(final boolean recursive, final String... fileExtensions)
	{
		this(Arrays.asList(fileExtensions), recursive);
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param fileExtensions
	 *            the collection of the file extensions
	 */
	public MultiplyExtensionsFileFilter(final Collection<String> fileExtensions)
	{
		this(fileExtensions, false);
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param fileExtensions
	 *            the collection of the file extensions
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 */
	public MultiplyExtensionsFileFilter(final Collection<String> fileExtensions,
		final boolean recursive)
	{
		if (null == fileExtensions || fileExtensions.isEmpty())
		{
			throw new IllegalArgumentException("Argument fileExtensions cant be null or empty. "
				+ "Please set the argument fileExtensions appropriate.");
		}
		this.recursive = recursive;
		this.fileExtensions = new LinkedHashSet<>();
		for (final String extension : fileExtensions)
		{
			this.fileExtensions.add(extension.toLowerCase());
		}
	}

	/**
	 * Instantiates a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param fileExtensions
	 *            the optional arguments of the file extensions
	 */
	public MultiplyExtensionsFileFilter(final String... fileExtensions)
	{
		this(false, fileExtensions);
	}

	/**
	 * Factory method for create a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param fileExtensions
	 *            the collection of the file extensions
	 * @return the new created {@link MultiplyExtensionsFileFilter} object
	 */
	public static FileFilter of(final Collection<String> fileExtensions)
	{
		return new MultiplyExtensionsFileFilter(fileExtensions);
	}

	/**
	 * Factory method for create a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param fileExtensions
	 *            the collection of the file extensions
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @return the new created {@link MultiplyExtensionsFileFilter} object
	 */
	public static FileFilter of(final Collection<String> fileExtensions, final boolean recursive)
	{
		return new MultiplyExtensionsFileFilter(fileExtensions, recursive);
	}

	/**
	 * Factory method for create a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @param fileExtensions
	 *            the optional arguments of the file extensions
	 * @return the new created {@link MultiplyExtensionsFileFilter} object
	 */
	public static FileFilter of(final boolean recursive, final String... fileExtensions)
	{
		return new MultiplyExtensionsFileFilter(recursive, fileExtensions);
	}

	/**
	 * Factory method for create a new {@link MultiplyExtensionsFileFilter}
	 *
	 * @param fileExtensions
	 *            the optional arguments of the file extensions
	 * @return the new created {@link MultiplyExtensionsFileFilter} object
	 */
	public static FileFilter of(final String... fileExtensions)
	{
		return new MultiplyExtensionsFileFilter(fileExtensions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File pathname)
	{
		if (recursive && pathname.isDirectory())
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
		return "[MultiplyExtensionsFileFilter:" + " fileExtensions: " + fileExtensions
			+ " recursive: " + recursive + "]";
	}

}
