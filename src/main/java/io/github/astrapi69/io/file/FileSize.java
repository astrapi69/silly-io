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
