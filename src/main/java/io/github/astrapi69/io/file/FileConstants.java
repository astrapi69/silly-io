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
 * The class {@link FileConstants} provides constant values relevant for file operations
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public abstract class FileConstants
{

	/**
	 * Constant for the slash. current value:"\"
	 */
	public static final String BACKSLASH = "\\";

	/**
	 * Constant for the blocksize. current value:8192
	 */
	public static final int BLOCKSIZE = 8192;

	/**
	 * Constant for the dot. current value:"."
	 */
	public static final String DOT = ".";

	/**
	 * Constant for the dot. current value:":"
	 */
	public static final String DOUBLEDOT = ":";

	/**
	 * Constant for the kilobyte. current value:1024
	 */
	public static final int KILOBYTE = 1024;

	/**
	 * Constant for the slash. current value:"/"
	 */
	public static final String SLASH = "/";

	/**
	 * String array with a few extensions from zip-files
	 */
	public static final String[] ZIP_EXTENSIONS = { FileExtension.ZIP.getExtension(),
			FileExtension.JAR.getExtension(), FileExtension.WAR.getExtension(),
			FileExtension.EAR.getExtension(), FileExtension.TAR.getExtension(),
			FileExtension.RAR.getExtension(), FileExtension.SEVEN_ZIP.getExtension(),
			FileExtension.BZ2.getExtension(), FileExtension.GZ.getExtension() };

	/**
	 * String array with the invalid characters in filenames
	 */
	public static String[] INVALID_CHARS_IN_FILENAME = { "\\", "/", ":", "*", "\"", "<", ">", "|" };

}
