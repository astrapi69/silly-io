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

import java.io.FileFilter;

import io.github.astrapi69.io.file.FileExtension;

/**
 * The class {@link TxtFileFilter} accepts File-objects that are directories or end with the suffix
 * '.txt'. Accepts directories to allow search files recursive in directories
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class TxtFileFilter extends SuffixFileFilter
{

	/**
	 * Instantiates a new {@link TxtFileFilter} for the txt file extension with the given flag for
	 * recursion
	 *
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 */
	public TxtFileFilter(final boolean recursive)
	{
		super(FileExtension.TXT.getExtension(), recursive);
	}

	/**
	 * Instantiates a new {@link TxtFileFilter} for the txt file extension
	 */
	public TxtFileFilter()
	{
		this(true);
	}

	/**
	 * Factory method for create a new {@link TxtFileFilter} for the txt file extension
	 *
	 * @return the new created {@link TxtFileFilter} object
	 */
	public static FileFilter of()
	{
		return new TxtFileFilter();
	}

	/**
	 * Factory method for create a new {@link TxtFileFilter} for the txt file extension with the
	 * given flag for recursion
	 *
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @return the new created {@link TxtFileFilter} object
	 */
	public static FileFilter of(final boolean recursive)
	{
		return new TxtFileFilter(recursive);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "[TxtFileFilter:" + FileExtension.TXT.getExtension() + "]";
	}

}
