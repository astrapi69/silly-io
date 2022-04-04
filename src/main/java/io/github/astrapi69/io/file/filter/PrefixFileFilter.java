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

/**
 * The class {@link PrefixFileFilter} accepts File-objects that match to the given prefix.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class PrefixFileFilter implements FileFilter
{

	/** The file prefix */
	private final String prefix;

	/** The recursive flag, if the recursive flag is true the filter will be executed recursively */
	private final boolean recursive;

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix and the given flag for
	 * recursion
	 *
	 * @param prefix
	 *            the prefix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 */
	public PrefixFileFilter(final String prefix, final boolean recursive)
	{
		this.prefix = prefix;
		this.recursive = recursive;
	}

	/**
	 * Factory method for create a new {@link PrefixFileFilter} with the given prefix
	 *
	 * @param prefix
	 *            the prefix
	 * @return the new created {@link PrefixFileFilter} object
	 */
	public static FileFilter of(final String prefix)
	{
		return new PrefixFileFilter(prefix);
	}

	/**
	 * Factory method for create a new {@link PrefixFileFilter} with the given prefix and the given
	 * flag for recursion
	 *
	 * @param prefix
	 *            the prefix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @return the new created {@link PrefixFileFilter} object
	 */
	public static FileFilter of(final String prefix, final boolean recursive)
	{
		return new PrefixFileFilter(prefix, recursive);
	}

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix and the recursive flag is
	 * true
	 *
	 * @param prefix
	 *            the prefix
	 */
	public PrefixFileFilter(final String prefix)
	{
		this.prefix = prefix;
		this.recursive = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File file)
	{
		// if recursive flag true and file is a directory allow recursive search
		if (recursive && file.isDirectory())
		{
			return true;
		}
		return file.getName().startsWith(prefix);
	}

}
