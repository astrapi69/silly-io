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
package io.github.astrapi69.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for input/output operations
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class StreamExtensions
{
	private StreamExtensions()
	{
	}

	/**
	 * Returns the given file as a byte array
	 *
	 * @param file
	 *            the file
	 * @return Returns the given file as a byte array
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static byte[] getByteArray(final File file) throws IOException
	{
		return getByteArray(getInputStream(file));
	}

	/**
	 * Returns the given InputStream as a byte array
	 *
	 * @param in
	 *            The InputStream
	 * @return Returns the given InputStream as a byte array
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static byte[] getByteArray(final InputStream in) throws IOException
	{
		return getByteArray(in, new ByteArrayOutputStream(in.available()));
	}

	/**
	 * Gets the byte array
	 *
	 * @param in
	 *            The InputStream
	 * @param os
	 *            The ByteArrayOutputStream
	 * @return the byte array
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static byte[] getByteArray(final InputStream in, final ByteArrayOutputStream os)
		throws IOException
	{
		int byt;
		while ((byt = in.read()) != -1)
		{
			os.write(byt);
		}
		return os.toByteArray();
	}

	/**
	 * Gets the input stream from a File object.
	 *
	 * @param file
	 *            the file
	 * @return the input stream.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static InputStream getInputStream(final File file) throws IOException
	{
		return getInputStream(file, false);
	}

	/**
	 * Gets the input stream from a File object.
	 *
	 * @param file
	 *            the file
	 * @param createFile
	 *            If true and the file does not exist it will be create a new file.
	 * @return the input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static InputStream getInputStream(final File file, final boolean createFile)
		throws IOException
	{
		InputStream is;
		if (file.exists())
		{
			is = file.toURI().toURL().openStream();
		}
		else
		{
			if (createFile)
			{
				file.createNewFile();
				is = file.toURI().toURL().openStream();
			}
			else
			{
				throw new FileNotFoundException("File " + file.getName() + " does not exist.");
			}
		}
		return is;
	}

	/**
	 * Gets the input stream from the given filename with the full path
	 *
	 * @param fullpathWithFilename
	 *            the full path with filename
	 * @return the input stream.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static InputStream getInputStream(final String fullpathWithFilename) throws IOException
	{
		return getInputStream(Paths.get(fullpathWithFilename));
	}

	/**
	 * Gets the input stream from the given filename with the full path
	 *
	 * @param path
	 *            the {@link Path} object
	 * @return the input stream.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static InputStream getInputStream(final Path path) throws IOException
	{
		return Files.newInputStream(path);
	}

	/**
	 * Gets the output stream from the given filename with the full path
	 *
	 * @param fullPathWithFilename
	 *            the full path with filename
	 * @return the output stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static OutputStream getOutputStream(final String fullPathWithFilename) throws IOException
	{
		return getOutputStream(Paths.get(fullPathWithFilename));
	}

	/**
	 * Gets the input stream from the given filename with the full path
	 *
	 * @param path
	 *            the {@link Path} object
	 * @return the input stream.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static OutputStream getOutputStream(final Path path) throws IOException
	{
		return Files.newOutputStream(path);
	}

	/**
	 * Gets the output stream from a File object.
	 *
	 * @param file
	 *            the file.
	 * @return the output stream.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static OutputStream getOutputStream(final File file) throws IOException
	{
		return getOutputStream(file, false);
	}

	/**
	 * Gets a {@link BufferedOutputStream} from the given {@link File} object.
	 *
	 * @param file
	 *            the file
	 * @param createFile
	 *            If true and the file does not exist it will be create a new file.
	 * @return the output stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static OutputStream getOutputStream(final File file, final boolean createFile)
		throws IOException
	{
		OutputStream os = null;
		BufferedOutputStream bos = null;
		if (file.exists())
		{
			os = new FileOutputStream(file);
		}
		else
		{
			if (createFile)
			{
				file.createNewFile();
				os = new FileOutputStream(file);
			}
			else
			{
				throw new FileNotFoundException("File " + file.getName() + " does not exist.");
			}
		}
		bos = new BufferedOutputStream(os);
		return bos;
	}

	/**
	 * Gets a Reader from the given file object.
	 *
	 * @param inputFile
	 *            the input file.
	 * @return the reader.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Reader getReader(final File inputFile) throws IOException
	{
		return getReader(inputFile, null, false);
	}

	/**
	 * Gets a Reader from the given file object.
	 *
	 * @param inputFile
	 *            the input file
	 * @param encoding
	 *            The encoding from the file.
	 * @param createFile
	 *            If true and the file does not exist, a new file will be created
	 * @return the reader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static Reader getReader(final File inputFile, final String encoding,
		final boolean createFile) throws IOException
	{
		FileInputStream fis;
		InputStreamReader isr;
		BufferedReader reader;
		if (inputFile.exists())
		{
			fis = new FileInputStream(inputFile);
		}
		else
		{
			if (createFile)
			{
				inputFile.createNewFile();
				fis = new FileInputStream(inputFile);
			}
			else
			{
				throw new FileNotFoundException("File " + inputFile.getName() + " does not exist.");
			}
		}
		if (null == encoding)
		{
			isr = new InputStreamReader(fis);
		}
		else
		{
			isr = new InputStreamReader(fis, encoding);
		}
		// create the bufferedreader
		reader = new BufferedReader(isr);

		return reader;
	}

	/**
	 * Gets the SerialVersionUID from the given Class.
	 *
	 * @param clazz
	 *            The class
	 * @return the serial version uid
	 */
	public static long getSerialVersionUID(final Class<? extends Serializable> clazz)
	{
		return ObjectStreamClass.lookup(clazz).getSerialVersionUID();
	}

	/**
	 * Gets a Writer from the given file object.
	 *
	 * @param file
	 *            the file.
	 * @return the Writer.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Writer getWriter(final File file) throws IOException
	{
		return getWriter(file, null, false);
	}

	/**
	 * Gets a Writer from the given file object.
	 *
	 * @param file
	 *            the file
	 * @param encoding
	 *            The encoding from the file.
	 * @param createFile
	 *            the create file
	 * @return the Writer.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Writer getWriter(final File file, final String encoding, final boolean createFile)
		throws IOException
	{
		PrintWriter printWriter;
		BufferedOutputStream bos;
		FileOutputStream fos;
		OutputStreamWriter osw;
		if (file.exists())
		{
			fos = new FileOutputStream(file);
		}
		else
		{
			if (createFile)
			{
				file.createNewFile();
				fos = new FileOutputStream(file);
			}
			else
			{
				throw new FileNotFoundException("File " + file.getName() + "does not exist.");
			}
		}
		bos = new BufferedOutputStream(fos);
		if (null == encoding)
		{
			osw = new OutputStreamWriter(bos);
		}
		else
		{
			osw = new OutputStreamWriter(bos, encoding);
		}
		printWriter = new PrintWriter(osw);

		return printWriter;

	}

	/**
	 * The Method writeInputStreamToOutputStream(InputStream, OutputStream, boolean) writes to the
	 * given OutputStream from an opened InputStream.
	 *
	 * @param inputStream
	 *            The opened InputStream.
	 * @param outputStream
	 *            The opened OutputStream.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void writeInputStreamToOutputStream(final InputStream inputStream,
		final OutputStream outputStream) throws IOException
	{
		int nextByte;
		while ((nextByte = inputStream.read()) != -1)
		{
			outputStream.write(nextByte);
		}
	}

	/**
	 * This method reads from an opened {@link Reader} object and writes it to the opened
	 * {@link Writer} object
	 *
	 * @param reader
	 *            The opened Reader
	 * @param writer
	 *            The opened Writer
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static void writeReaderToWriter(final Reader reader, final Writer writer)
		throws IOException
	{
		int character;
		while ((character = reader.read()) != -1)
		{
			writer.write(character);
		}
	}

	/**
	 * Converts the given InputStream to a string.
	 *
	 * @param is
	 *            the is
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String toString(final InputStream is) throws IOException
	{

		if (is != null)
		{
			final StringBuilder sb = new StringBuilder();
			final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null)
			{
				sb.append(line).append("\n");
			}
			return sb.toString();
		}
		else
		{
			return "";
		}
	}
}
