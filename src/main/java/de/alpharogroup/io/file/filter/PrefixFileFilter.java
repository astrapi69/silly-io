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
package de.alpharogroup.io.file.filter;

import de.alpharogroup.io.file.FileExtension;
import de.alpharogroup.io.file.namefilter.MultiplyExtensionsFilenameFilter;

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
	private final String prefix;

	/** The recursive flag */
	private final boolean recursive;

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix and the given flag for
	 * recursion
	 *
	 * @param prefix
	 *            the prefix
	 * @param recursive
	 *            if this flag is true the filter will be executed recursively
	 */
	public PrefixFileFilter(final String prefix, final boolean recursive) {
		this.prefix = prefix;
		this.recursive = recursive;
	}

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix and the recursive flag is
	 * true
	 *
	 * @param prefix
	 *            the prefix
	 */
	public PrefixFileFilter(final String prefix) {
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
