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
package io.github.astrapi69.io.shell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * A {@link TeeOutputStream} passes everything written to it, every flush and the close on to both
 * of its streams
 */
class TeeOutputStreamTest
{

	@Test
	void write_sendsSingleBytesAndByteRangesToBothStreams() throws IOException
	{
		final RecordingOutputStream original = new RecordingOutputStream();
		final RecordingOutputStream additional = new RecordingOutputStream();
		final TeeOutputStream teeOutputStream = new TeeOutputStream(original, additional);

		teeOutputStream.write('>');
		teeOutputStream.write("[echo]".getBytes(StandardCharsets.UTF_8), 1, 4);
		teeOutputStream.write("!".getBytes(StandardCharsets.UTF_8));

		assertEquals(">echo!", original.toString(StandardCharsets.UTF_8));
		assertEquals(">echo!", additional.toString(StandardCharsets.UTF_8));
	}

	@Test
	void flush_flushesBothStreams() throws IOException
	{
		final RecordingOutputStream original = new RecordingOutputStream();
		final RecordingOutputStream additional = new RecordingOutputStream();

		new TeeOutputStream(original, additional).flush();

		assertEquals(1, original.flushes);
		assertEquals(1, additional.flushes);
	}

	@Test
	void close_closesBothStreams() throws IOException
	{
		final RecordingOutputStream original = new RecordingOutputStream();
		final RecordingOutputStream additional = new RecordingOutputStream();

		new TeeOutputStream(original, additional).close();

		assertTrue(original.closed);
		assertTrue(additional.closed);
	}

	/**
	 * Keeps what is written to it and counts its flushes and whether it was closed
	 */
	private static final class RecordingOutputStream extends ByteArrayOutputStream
	{
		private int flushes;

		private boolean closed;

		@Override
		public void flush()
		{
			flushes++;
		}

		@Override
		public void close()
		{
			closed = true;
		}
	}
}
