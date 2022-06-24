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

	/** The recursive flag, if the recursive flag is true the filter will be executed recursively */
	private final boolean recursive;

	/** The file suffix */
	private final String fileSuffix;

	/**
	 * Instantiates a new {@link SimpleFilenameFilter} object
	 *
	 * @param fileSuffix
	 *            the file suffix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 */
	public SimpleFilenameFilter(final String fileSuffix, final boolean recursive)
	{
		Objects.requireNonNull(fileSuffix);
		if (fileSuffix.equals(""))
		{
			throw new IllegalArgumentException(
				"Argument fileSuffix cant be empty. " + "Please set the argument fileSuffix.");
		}
		this.fileSuffix = fileSuffix.toLowerCase();
		this.recursive = recursive;
	}

	/**
	 * Instantiates a new {@link SimpleFilenameFilter} object
	 *
	 * @param fileSuffix
	 *            the file suffix
	 */
	public SimpleFilenameFilter(final String fileSuffix)
	{
		this(fileSuffix, false);
	}

	/**
	 * Factory method for create a new {@link FilenameFilter} with the given suffix
	 *
	 * @param suffix
	 *            the suffix
	 * @return the new created {@link FilenameFilter} object
	 */
	public static FilenameFilter of(final String suffix)
	{
		return new SimpleFilenameFilter(suffix);
	}

	/**
	 * Factory method for create a new {@link FilenameFilter} with the given suffix
	 *
	 * @param suffix
	 *            the suffix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @return the new created {@link FilenameFilter} object
	 */
	public static FilenameFilter of(final String suffix, final boolean recursive)
	{
		return new SimpleFilenameFilter(suffix, recursive);
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
		return name.toLowerCase().endsWith(this.fileSuffix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "[SimpleFilenameFilter:" + " fileSuffix: " + fileSuffix + " recursive: " + recursive
			+ "]";
	}

}
