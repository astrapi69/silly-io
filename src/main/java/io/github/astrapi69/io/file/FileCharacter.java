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
 * The enum File character.
 */
public enum FileCharacter
{

	/**
	 * Backslash file character.
	 */
	BACKSLASH(FileCharacter.BACKSLASH_CHARACTER),

	/**
	 * Dot file character.
	 */
	DOT(FileCharacter.DOT_CHARACTER),

	/**
	 * Double dot character file character.
	 */
	DOUBLE_DOT(FileCharacter.DOUBLE_DOT_CHARACTER),

	/**
	 * Slash file character.
	 */
	SLASH(FileCharacter.SLASH_CHARACTER);

	/**
	 * Constant for the slash. current value:"\"
	 */
	public static final String BACKSLASH_CHARACTER = "\\";

	/**
	 * Constant for the slash. current value:"/"
	 */
	public static final String SLASH_CHARACTER = "/";

	/**
	 * Constant for the dot. current value:"."
	 */
	public static final String DOT_CHARACTER = ".";

	/**
	 * Constant for the dot. current value:":"
	 */
	public static final String DOUBLE_DOT_CHARACTER = ":";

	/**
	 * String array with the invalid characters in filenames
	 */
	public static String[] INVALID_IN_NAME = { "\\", "/", ":", "*", "\"", "<", ">", "|" };

	/**
	 * The file specific character
	 */
	private final String character;

	/**
	 * Instantiates a new {@link FileCharacter}
	 *
	 * @param character
	 *            the file specific character
	 */
	FileCharacter(String character)
	{
		this.character = character;
	}

	/**
	 * Gets the file specific character
	 * 
	 * @return the file specific character
	 */
	public String getCharacter()
	{
		return character;
	}
}
