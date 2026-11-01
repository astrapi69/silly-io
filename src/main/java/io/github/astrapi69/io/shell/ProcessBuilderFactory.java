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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Factory class for creating and configuring instances of {@link ProcessBuilder} with specified
 * shell and command details
 */
public class ProcessBuilderFactory
{

	/**
	 * Creates a new {@link ProcessBuilder} configured to run a specified command in a given shell
	 * within a specified execution path
	 *
	 * @param shellPath
	 *            the path to the shell executable
	 * @param shellFlag
	 *            the shell flag to be used, such as '-c' for bash
	 * @param executionPath
	 *            the path where the command will be executed, a leading {@code ~} alone or before a
	 *            separator stands for the home directory
	 * @param command
	 *            the command to be executed
	 * @return a configured {@link ProcessBuilder} instance
	 * @throws IllegalArgumentException
	 *             if the execution directory does not exist
	 */
	public static ProcessBuilder newProcessBuilder(String shellPath, String shellFlag,
		String executionPath, String command)
	{
		if (startsWithTheHomeDirectory(executionPath))
		{
			executionPath = System.getProperty("user.home") + executionPath.substring(1);
		}
		File executionDirectory = new File(executionPath);
		if (!executionDirectory.exists())
		{
			throw new IllegalArgumentException("Execution directory does not exist");
		}

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.directory(executionDirectory);
		processBuilder.command(shellPath, shellFlag, command);
		return processBuilder;
	}

	/**
	 * A {@code ~} stands for the home directory only where it starts the path, alone or before a
	 * separator; anywhere else, as in {@code backup~1}, it is a character of a name
	 *
	 * @param executionPath
	 *            the path to check
	 * @return true if the path starts with the home directory
	 */
	private static boolean startsWithTheHomeDirectory(String executionPath)
	{
		// the first name of a relative path, split by the platform's own path syntax, so no branch
		// is
		// reachable on one platform only
		Path path = Paths.get(executionPath);
		return !path.isAbsolute() && path.getName(0).toString().equals("~");
	}
}
