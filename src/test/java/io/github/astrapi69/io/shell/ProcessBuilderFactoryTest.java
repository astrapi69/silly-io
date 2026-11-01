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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * {@link ProcessBuilderFactory} builds a process for a command in a shell, run in an existing
 * directory; the process is never started here
 */
class ProcessBuilderFactoryTest
{

	@TempDir
	Path directory;

	@Test
	void newProcessBuilder_runsTheCommandInTheShell_inTheGivenDirectory()
	{
		final ProcessBuilder processBuilder = ProcessBuilderFactory.newProcessBuilder("/bin/bash",
			"-c", directory.toString(), "echo hello");

		assertEquals(List.of("/bin/bash", "-c", "echo hello"), processBuilder.command());
		assertEquals(directory.toFile(), processBuilder.directory());
	}

	@Test
	void newProcessBuilder_replacesTheTildeWithTheHomeDirectory()
	{
		final ProcessBuilder processBuilder = ProcessBuilderFactory.newProcessBuilder("/bin/bash",
			"-c", "~", "echo hello");

		assertEquals(new File(System.getProperty("user.home")), processBuilder.directory());
	}

	@Test
	void newProcessBuilder_throwsIllegalArgumentException_forADirectoryThatDoesNotExist()
	{
		final String missing = directory.resolve("missing").toString();

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> ProcessBuilderFactory.newProcessBuilder("/bin/bash", "-c", missing, "echo"));
		assertEquals("Execution directory does not exist", exception.getMessage());
	}
}
