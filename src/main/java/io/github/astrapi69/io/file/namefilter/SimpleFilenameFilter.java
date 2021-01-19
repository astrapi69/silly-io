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
package io.github.astrapi69.io.file.namefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

/**
 * The class {@link SimpleFilenameFilter}
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class SimpleFilenameFilter implements FilenameFilter
{

	/** The accept dir */
	private boolean recursive;

	/** The filesuffix */
	private String filesuffix;

	/**
	 * Instantiates a new {@link SimpleFilenameFilter}
	 *
	 * @param filesuffix
	 *            the filesuffix
	 * @param recursive
	 *            the accept dir
	 */
	public SimpleFilenameFilter(final String filesuffix, final boolean recursive)
	{
		Objects.requireNonNull(filesuffix);
		if (filesuffix.equals(""))
		{
			throw new IllegalArgumentException(
				"Argument filesuffix cant be empty. " + "Please set the argument filesuffix.");
		}
		this.filesuffix = filesuffix.toLowerCase();
		this.recursive = recursive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File dir, final String name)
	{
		final File currentFile = new File(dir, name);
		if (recursive)
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
		buffer.append(recursive);
		buffer.append("]");
		return buffer.toString();
	}

}
