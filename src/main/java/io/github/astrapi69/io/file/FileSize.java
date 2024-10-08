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

/**
 * The enum {@link FileSize} that holds file specific sizes
 */
public enum FileSize
{

	/**
	 * Enum value for the default block size.
	 */
	DEFAULT_BLOCK_SIZE(FileSize.BLOCK_SIZE_VALUE),

	/**
	 * Enum value for the kilobyte.
	 */
	KILOBYTE(FileSize.KILOBYTE_VALUE);

	/**
	 * Constant for the default block size. current value:8192
	 */
	public static final int BLOCK_SIZE_VALUE = 8192;

	/**
	 * Constant for the kilobyte.
	 */
	public static final int KILOBYTE_VALUE = 1024;

	/**
	 * The file specific size
	 */
	private final int size;

	/**
	 * Instantiates a new {@link FileCharacter}
	 *
	 * @param size
	 *            the file specific size
	 */
	FileSize(int size)
	{
		this.size = size;
	}

	/**
	 * Gets the file specific size
	 * 
	 * @return the file specific size
	 */
	public int getSize()
	{
		return size;
	}

}
