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
 * The class ClassFileFilter accepts File-objects that are directories or end with the suffix
 * '.class'. Accepts directories to allow search files recursive in directories
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ClassFileFilter extends SuffixFileFilter
{

	/**
	 * Instantiates a new {@link ClassFileFilter} with the given flag for recursion
	 *
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 */
	public ClassFileFilter(final boolean recursive)
	{
		super(FileExtension.CLASS.getExtension(), recursive);
	}

	/**
	 * Instantiates a new {@link ClassFileFilter}
	 */
	public ClassFileFilter()
	{
		this(true);
	}

	/**
	 * Factory method for create a new {@link ClassFileFilter} for the class file extension
	 *
	 * @return the new created {@link ClassFileFilter} object
	 */
	public static FileFilter of()
	{
		return new ClassFileFilter();
	}

	/**
	 * Factory method for create a new {@link ClassFileFilter} for the class file extension with the
	 * given flag for recursion
	 *
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @return the new created {@link ClassFileFilter} object
	 */
	public static FileFilter of(final boolean recursive)
	{
		return new ClassFileFilter(recursive);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "[ClassFileFilter:" + FileExtension.CLASS.getExtension() + "]";
	}

}
