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
import java.util.Objects;

import io.github.astrapi69.io.file.CaseSensitivity;

/**
 * The class {@link PrefixFileFilter} accepts File-objects whose name starts with the given prefix.
 * It tells upper and lower case apart unless it is built with {@link CaseSensitivity#INSENSITIVE}
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

	/** Whether the prefix is compared with or without regard to case */
	private final CaseSensitivity caseSensitivity;

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix, the given flag for
	 * recursion and the given case sensitivity
	 *
	 * @param prefix
	 *            the prefix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @param caseSensitivity
	 *            whether the prefix is compared with or without regard to case
	 * @throws NullPointerException
	 *             if the case sensitivity is null
	 */
	public PrefixFileFilter(final String prefix, final boolean recursive,
		final CaseSensitivity caseSensitivity)
	{
		this.prefix = prefix;
		this.recursive = recursive;
		this.caseSensitivity = Objects.requireNonNull(caseSensitivity, "caseSensitivity");
	}

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix and the given flag for
	 * recursion, that tells upper and lower case apart
	 *
	 * @param prefix
	 *            the prefix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 */
	public PrefixFileFilter(final String prefix, final boolean recursive)
	{
		this(prefix, recursive, CaseSensitivity.SENSITIVE);
	}

	/**
	 * Instantiates a new {@link PrefixFileFilter} with the given prefix, that tells upper and lower
	 * case apart, and the recursive flag is true
	 *
	 * @param prefix
	 *            the prefix
	 */
	public PrefixFileFilter(final String prefix)
	{
		this(prefix, true);
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
	 * Factory method for create a new {@link PrefixFileFilter} with the given prefix, the given
	 * flag for recursion and the given case sensitivity
	 *
	 * @param prefix
	 *            the prefix
	 * @param recursive
	 *            if the recursive flag is true the filter will be executed recursively
	 * @param caseSensitivity
	 *            whether the prefix is compared with or without regard to case
	 * @return the new created {@link PrefixFileFilter} object
	 * @throws NullPointerException
	 *             if the case sensitivity is null
	 */
	public static FileFilter of(final String prefix, final boolean recursive,
		final CaseSensitivity caseSensitivity)
	{
		return new PrefixFileFilter(prefix, recursive, caseSensitivity);
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
		return caseSensitivity.startsWith(file.getName(), prefix);
	}

}
