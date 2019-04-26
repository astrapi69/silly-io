package de.alpharogroup.io.file;

/**
 * Constant Class for files.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public abstract class FileConstants
{

	/** Constant for the slash. current value:"\" */
	public static final String BACKSLASH = "\\";

	/** Constant for the blocksize. current value:8192 */
	public static final int BLOCKSIZE = 8192;

	/** Constant for the dot. current value:"." */
	public static final String DOT = ".";

	/** Constant for the dot. current value:":" */
	public static final String DOUBLEDOT = ":";

	/**
	 * String array with the invalid characters in filenames.
	 */
	public static String[] INVALID_CHARS_IN_FILENAME = { "\\", "/", ":", "*", "\"", "<", ">", "|" };

	/** Constant for the kilobyte. current value:1024 */
	public static final int KILOBYTE = 1024;

	/** Constant for the slash. current value:"/" */
	public static final String SLASH = "/";

	/**
	 * String array with a few extensions from zip-files.
	 */
	public static final String[] ZIP_EXTENSIONS = { ".zip", ".jar", ".war", ".ear", ".tar", ".rar",
			".7z", ".bz2", ".gz" };

}
