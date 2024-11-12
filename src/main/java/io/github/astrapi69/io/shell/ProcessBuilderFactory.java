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
	 *            the path where the command will be executed
	 * @param command
	 *            the command to be executed
	 * @return a configured {@link ProcessBuilder} instance
	 * @throws IllegalArgumentException
	 *             if the execution directory does not exist
	 */
	public static ProcessBuilder newProcessBuilder(String shellPath, String shellFlag,
		String executionPath, String command)
	{
		if (executionPath.contains("~"))
		{
			executionPath = executionPath.replace("~", System.getProperty("user.home"));
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
}
