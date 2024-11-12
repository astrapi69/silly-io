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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class CommandLineExecutor provides functionality for executing shell commands across
 * different operating systems
 */
public final class CommandLineExecutor
{

	private CommandLineExecutor()
	{
	}

	/**
	 * Executes the given command in the specified execution path with the shell appropriate for the
	 * current operating system, and mirrors output to an additional output stream
	 *
	 * @param executionPath
	 *            the path where the command should be executed
	 * @param command
	 *            the command to execute
	 * @param additionalOutput
	 *            the additional output stream to mirror the output to
	 * @return the output if any
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws InterruptedException
	 *             thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread
	 *             is interrupted, either before or during the activity
	 */
	public static String execute(String executionPath, String command,
		OutputStream additionalOutput) throws IOException, InterruptedException
	{
		OS currentOS = OS.get();
		String shellPath;
		String shellFlag;

		switch (currentOS)
		{
			case WINDOWS :
				shellPath = "cmd.exe";
				shellFlag = "/c";
				break;
			case MAC :
			case LINUX :
			case UNIX :
				shellPath = "/bin/bash";
				shellFlag = "-c";
				break;
			default :
				throw new UnsupportedOperationException(
					"Unsupported operating system: " + currentOS);
		}

		return execute(shellPath, shellFlag, executionPath, command, additionalOutput);
	}

	/**
	 * Executes the given command in the specified execution path with the specified shell and flag
	 *
	 * @param shellPath
	 *            the path to the shell executable
	 * @param shellFlag
	 *            the flag for executing the command
	 * @param executionPath
	 *            the path where the command should be executed
	 * @param command
	 *            the command to execute
	 * @param additionalOutput
	 *            the additional output stream to mirror the output to
	 * @return the output if any
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws InterruptedException
	 *             thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread
	 *             is interrupted, either before or during the activity
	 */
	private static String execute(String shellPath, String shellFlag, String executionPath,
		String command, OutputStream additionalOutput) throws IOException, InterruptedException
	{
		ProcessBuilder processBuilder = ProcessBuilderFactory.newProcessBuilder(shellPath,
			shellFlag, executionPath, command);

		processBuilder.redirectErrorStream(true);

		Process process = processBuilder.start();

		// Use TeeOutputStream to duplicate the output stream
		StringBuilder output = new StringBuilder();
		try (InputStream processOutput = process.getInputStream();
			TeeOutputStream teeOutputStream = new TeeOutputStream(new ByteArrayOutputStream(),
				additionalOutput);
			BufferedReader reader = new BufferedReader(new InputStreamReader(processOutput)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				output.append(line).append(System.lineSeparator());
				teeOutputStream.write(line.getBytes());
				teeOutputStream.write(System.lineSeparator().getBytes());
			}
		}

		int exitCode = process.waitFor();
		if (exitCode != 0)
		{
			output.append("Exit code: ").append(exitCode).append(System.lineSeparator());
		}
		return output.toString();
	}

	/**
	 * Gets the list of commands to be executed based on the given commands array and the current
	 * operating system
	 *
	 * @param commands
	 *            the commands array
	 * @param currentOS
	 *            the current operating system
	 * @return the list of commands
	 */
	public static List<String> getCommandList(String[] commands, OS currentOS)
	{
		List<String> commandList = new ArrayList<>();

		switch (currentOS)
		{
			case WINDOWS :
				commandList.add("cmd.exe");
				commandList.add("/c");
				break;
			case MAC :
			case LINUX :
			case UNIX :
				commandList.add("/bin/bash");
				commandList.add("-c");
				break;
			default :
				throw new UnsupportedOperationException(
					"Unsupported operating system: " + currentOS);
		}
		commandList.addAll(Arrays.asList(commands));
		return commandList;
	}
}
