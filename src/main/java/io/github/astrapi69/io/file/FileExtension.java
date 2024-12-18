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

import java.io.File;
import java.util.Objects;
import java.util.Set;

/**
 * The enum {@link FileExtension} for file extensions
 */
public enum FileExtension
{

	/** The file extension for adobe illustrator image files */
	AI(".ai"),

	/** The file extension for audio aif files */
	AIF(".aif"),

	/** The file extension for android package files */
	APK(".apk"),

	/** The file extension for archive files */
	ARJ(".arj"),

	/** The file extension for AVI Media files */
	AVI(".avi"),

	/** The file extension for backup files */
	BACKUP(".bak"),

	/** The file extension for batch files */
	BAT(".bat"),

	/** The file extension for binary disc image files */
	BIN(".bin"),

	/** The file extension for bitmap image files */
	BMP(".bmp"),

	/** The file extension for archive files created with bzip2 unix compression program */
	BZ2(".bz2"),

	/** The file extension for audio cda files */
	CDA(".cda"),

	/** The file extension for perl script files */
	CGI(".cgi"),

	/** The file extension for class files */
	CLASS(".class"),

	/** The file extension for ms-dos command files */
	COM(".com"),

	/** The file extension for CONF configuration files */
	CONF(".conf"),

	/** The file extension for CONFIG configuration files */
	CONFIG(".config"),

	/** The file extension for CPP programming files */
	CPP(".cpp"),

	/** The file extension for CSS web files */
	CSS(".css"),

	/** The file extension for csv files */
	CSV(".csv"),

	/** The file extension for data files */
	DAT(".dat"),

	/** The file extension for database files */
	DB(".db"),

	/** The file extension for database files */
	DBF(".dbf"),

	/** The file extension for Debian software package files */
	DEB(".deb"),

	/** The file extension for decrypted files */
	DECRYPTED(".decrypted"),

	/** The file extension for macOS X disk image files */
	DMG(".dmg"),

	/** The file extension for DOC document files */
	DOC(".doc"),

	/** The file extension for DOCX document files */
	DOCX(".docx"),

	/** The file extension for DOTX document files */
	DOTX(".dotx"),

	/** The file extension for java enterprise archive files */
	EAR(".ear"),

	/** The file extension for email message files */
	EML(".eml"),

	/** The file extension for apple email message files */
	EMLX(".emlx"),

	/** The file extension for encrypted files */
	ENCRYPTED(".enc"),

	/** The file extension for ENV programming files */
	ENV(".env"),

	/** The file extension for executable files */
	EXE(".exe"),

	/** The file extension for FLAC Media files */
	FLAC(".flac"),

	/** The file extension for FLV Media files */
	FLV(".flv"),

	/** The file extension for Windows font files */
	FNT(".fnt"),

	/** The file extension for FODT document files */
	FODT(".fodt"),

	/** The file extension for generic font files */
	FON(".fon"),

	/** The file extension for gif image files */
	GIF(".gif"),

	/** The file extension for archive files created with gzip compression algorithm */
	GZ(".gz"),

	/** The file extension for H programming files */
	H(".h"),

	/** The file extension for HEIC Image files */
	HEIC(".heic"),

	/** The file extension for HTML web files */
	HTML(".html"),

	/** The file extension for icon image files */
	ICO(".ico"),

	/** The file extension for INI programming files */
	INI(".ini"),

	/** The file extension for ISO disc image files */
	ISO(".iso"),

	/** The file extension for java archive files */
	JAR(".jar"),

	/** The file extension for java files */
	JAVA(".java"),

	/** The file extension for JPEG image files */
	JPEG(".jpeg"),

	/** The file extension for JPEG image files */
	JPG(".jpg"),

	/** The file extension for javascript files */
	JS(".js"),

	/** The file extension for json files */
	JSON(".json"),

	/** The file extension for KT programming files */
	KT(".kt"),

	/** The file extension for log files */
	LOG(".log"),

	/** The file extension for M4A Media files */
	M4A(".m4a"),

	/** The file extension for microsoft access database files */
	MDB(".mdb"),

	/** The file extension for audio midi files */
	MID(".mid"),

	/** The file extension for audio midi files */
	MIDI(".midi"),

	/** The file extension for MKV Media files */
	MKV(".mkv"),

	/** The file extension for MOV Media files */
	MOV(".mov"),

	/** The file extension for audio mp3 files */
	MP3(".mp3"),

	/** The file extension for MP4 Media files */
	MP4(".mp4"),

	/** The file extension for MPEG-2 audio files */
	MPA(".mpa"),

	/** The file extension for microsoft installer package files */
	MSI(".msi"),

	/** The file extension for mystic-crypt encrypted files */
	MYSTIC_CRYPT_ENCRYPTED(".mcrdb"),

	/** The file extension for ODT document files */
	ODT(".odt"),

	/** The file extension for Ogg Vorbis audio files */
	OGG(".ogg"),

	/** The file extension for open type font files */
	OTF(".otf"),

	/** The file extension for OTT document files */
	OTT(".ott"),

	/** The file extension for PDF image files */
	PDF(".pdf"),

	/** The file extension for package archive files for apple macintosh OSX installer packages */
	PKG(".pkg"),

	/** The file extension for perl script files */
	PL(".pl"),

	/** The file extension for PNG image files */
	PNG(".png"),

	/** The file extension for PPT document files */
	PPT(".ppt"),

	/** The file extension for PPTX document files */
	PPTX(".pptx"),

	/** The file extension for properties files */
	PROPERTIES(".properties"),

	/** The file extension for PostScript image files */
	PS(".ps"),

	/** The file extension for PSD image files */
	PSD(".psd"),

	/** The file extension for python files */
	PY(".py"),

	/** The file extension for archive files created with RAR compression */
	RAR(".rar"),

	/** The file extension for RAW Image files */
	RAW(".raw"),

	/** The file extension for Red Hat Package Manager files */
	RPM(".rpm"),

	/** The file extension for RTF document files */
	RTF(".rtf"),

	/** The file extension for game save files */
	SAV(".sav"),

	/** The file extension for archive files created with 7z compression */
	SEVEN_ZIP(".7z"),

	/** The file extension for sql files */
	SQL(".sql"),

	/** The file extension for Scalable Vector Graphics image files */
	SVG(".svg"),

	/** The file extension for archive files created by tar unix utility program */
	TAR(".tar"),

	/** The file extension for archive files created by tar unix utility program and gzip */
	TAR_GZ(".tar.gz"),

	/** The file extension for TIFF image files */
	TIF(".tif"),

	/** The file extension for TIFF image files */
	TIFF(".tiff"),

	/** The file extension for toast disc image files */
	TOAST(".toast"),

	/** The file extension for TS programming web files */
	TS(".ts"),

	/** The file extension for true type font files */
	TTF(".ttf"),

	/** The file extension for txt files */
	TXT(".txt"),

	/** The file extension for UOT document files */
	UOT(".uot"),

	/** The file extension for virtual CD files */
	VCD(".vcd"),

	/** The file extension for email contact files */
	VCF(".vcf"),

	/** The file extension for velocity template files */
	VELOCITY_TEMPLATE(".vm"),

	/** The file extension for java web archive files */
	WAR(".war"),

	/** The file extension for WAV files */
	WAV(".wav"),

	/** The file extension for WEBP Image files */
	WEBP(".webp"),

	/** The file extension for WMA audio files */
	WMA(".wma"),

	/** The file extension for WMV Media files */
	WMV(".wmv"),

	/** The file extension for Windows Media Player playlist files */
	WPL(".wpl"),

	/** The file extension for Windows script files */
	WSF(".wsf"),

	/** The file extension for xls files */
	XLS(".xls"),

	/** The file extension for XLSX document files */
	XLSX(".xlsx"),

	/** The file extension for xml files */
	XML(".xml"),

	/** The file extension for yaml files */
	YAML(".yaml"),

	/** The short type of file extension for yaml files */
	YML(".yml"),

	/** The file extension for archive files created with Z compression */
	Z(".z"),

	/** The file extension for archive files created with zip compression */
	ZIP(".zip");

	/**
	 * String array with a few extensions from zip-files
	 */
	public static final String[] ZIP_EXTENSIONS = { FileExtension.ZIP.getExtension(),
			FileExtension.JAR.getExtension(), FileExtension.WAR.getExtension(),
			FileExtension.EAR.getExtension(), FileExtension.TAR.getExtension(),
			FileExtension.RAR.getExtension(), FileExtension.SEVEN_ZIP.getExtension(),
			FileExtension.BZ2.getExtension(), FileExtension.GZ.getExtension(),
			FileExtension.TAR_GZ.getExtension(), FileExtension.Z.getExtension(),
			FileExtension.ARJ.getExtension() };

	/**
	 * Gets all known compression file extensions as a {@link Set}
	 * 
	 * @return a {@link Set} with all known compression file extensions
	 */
	public static Set<String> getZipExtensions()
	{
		return Set.of(FileExtension.ZIP_EXTENSIONS);
	}

	/**
	 * Checks if the given file is from the given {@link FileExtension} object
	 *
	 * @param file
	 *            the file to check
	 * @param fileExtension
	 *            the file extension
	 * @return true if the given file is from the given {@link FileExtension} object otherwise false
	 */
	public static boolean is(File file, FileExtension fileExtension)
	{
		Objects.requireNonNull(file, "File must not be null");
		Objects.requireNonNull(fileExtension, "FileExtension must not be null");
		return is(file, fileExtension.getExtension());
	}

	/**
	 * Checks if the given file is from the given file extension as {@link String} object
	 *
	 * @param file
	 *            the file to check
	 * @param fileExtension
	 *            the file extension as {@link String} object
	 * @return true if the given file is from the given {@link String} object otherwise false
	 */
	public static boolean is(File file, String fileExtension)
	{
		Objects.requireNonNull(file, "File must not be null");
		Objects.requireNonNull(fileExtension, "String for the file extension must not be null");
		return fileExtension.startsWith(".")
			? FilenameExtensions.getFileExtension(file, true).equals(fileExtension)
			: FilenameExtensions.getFileExtension(file, false).equals(fileExtension);
	}

	/**
	 * Checks if the given file has a known file extension that exist in the enum
	 * {@link FileExtension}
	 *
	 * @param file
	 *            the file to check
	 * @return true if the given file has a known file extension that exist in the enum
	 *         {@link FileExtension} otherwise false
	 */
	public static boolean isKnown(File file)
	{
		Objects.requireNonNull(file, "File must not be null");
		Objects.requireNonNull(file, "FileExtension must not be null");
		FileExtension[] fileExtensions = FileExtension.values();
		for (FileExtension fileExtension : fileExtensions)
		{
			if (file.getName().endsWith(fileExtension.getExtension()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Resolves a {@link FileExtension} from the given string representation of a file extension
	 *
	 * @param fileExtension
	 *            the string representation of the file extension
	 * @return the corresponding {@link FileExtension} or null if no match is found
	 */
	public static FileExtension resolve(String fileExtension)
	{
		if (fileExtension == null)
		{
			return null;
		}
		for (FileExtension extension : FileExtension.values())
		{
			if (extension.getExtension().equalsIgnoreCase(fileExtension))
			{
				return extension;
			}
		}
		return null;
	}

	/** The file extension */
	private final String extension;

	/**
	 * Instantiates a new {@link FileExtension}
	 *
	 * @param extension
	 *            the file extension
	 */
	FileExtension(final String extension)
	{
		this.extension = extension;
	}

	/**
	 * Gets the extension
	 *
	 * @return the extension
	 */
	public String getExtension()
	{
		return extension;
	}

	/**
	 * Gets the extension without the dot
	 *
	 * @return the extension without the dot
	 */
	public String getExtensionOnly()
	{
		return extension.substring(1, extension.length());
	}

}
