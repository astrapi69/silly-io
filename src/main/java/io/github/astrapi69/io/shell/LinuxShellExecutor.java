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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.astrapi69.io.StreamExtensions;

/**
 * The class LinuxShellExecutor.
 */
public final class LinuxShellExecutor
{
	private LinuxShellExecutor()
	{
	}

	/**
	 * Executes the given command in the given execution path with the given executable
	 *
	 * @param shellPath
	 *            the path where the shell executable locates
	 * @param executionPath
	 *            the path where the command shell be executed
	 * @param command
	 *            the command to execute
	 * @return the output if any
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws InterruptedException
	 *             is thrown when a thread is waiting, sleeping, or otherwise occupied, * and the
	 *             thread is interrupted, either before or during the activity
	 */
	public static String execute(String shellPath, String executionPath, String command)
		throws IOException, InterruptedException
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

		String cdToExecutionPath = "cd " + executionPath;
		String commands = cdToExecutionPath + " && " + command;
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(shellPath, "-c", commands);

		Process process = processBuilder.start();
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null)
		{
			stringBuilder.append(line).append(System.lineSeparator());
		}
		int waitFor = process.waitFor();

		if (waitFor != 0)
			stringBuilder.append("Exit code: ").append(waitFor).append("\n");
		return stringBuilder.toString();
	}

	/**
	 * Execute the given commands and return the result.
	 *
	 * @param withResponse
	 *            the with response
	 * @param command
	 *            the command(s)
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String execute(final boolean withResponse, final String... command)
		throws IOException
	{
		final List<String> commands = new ArrayList<>();
		commands.add("bash");
		commands.add("-c");
		commands.addAll(Arrays.asList(command));
		String response = "";

		final ProcessBuilder pb = new ProcessBuilder(commands);
		pb.redirectErrorStream(true);

		final Process shell = pb.start();

		if (withResponse)
		{
			try (InputStream shellIn = shell.getInputStream())
			{
				response = StreamExtensions.toString(shellIn);
			}
		}

		return response;
	}

}
