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
package io.github.astrapi69.io.file;

import java.util.Locale;

/**
 * The enum {@link CaseSensitivity} tells whether a comparison of file names tells upper and lower
 * case apart, as the {@code IOCase} of Apache Commons IO does. A comparison without case
 * lower-cases both sides with {@link Locale#ROOT}, as the suffix filters do, so that it gives the
 * same result in every default locale
 */
public enum CaseSensitivity
{

	/** Upper and lower case differ: {@code Report-1.txt} does not start with {@code report} */
	SENSITIVE,

	/** Upper and lower case are the same: {@code Report-1.txt} starts with {@code report} */
	INSENSITIVE;

	/**
	 * Tells whether the given name starts with the given prefix
	 *
	 * @param name
	 *            the name
	 * @param prefix
	 *            the prefix
	 * @return true if the name starts with the prefix, with or without regard to case as this
	 *         constant says
	 */
	public boolean startsWith(final String name, final String prefix)
	{
		if (this == SENSITIVE)
		{
			return name.startsWith(prefix);
		}
		return name.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT));
	}
}
