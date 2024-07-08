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
