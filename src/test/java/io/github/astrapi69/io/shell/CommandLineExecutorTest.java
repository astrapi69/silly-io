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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;


/**
 * Unit test class for {@link CommandLineExecutor}
 */
public class CommandLineExecutorTest
{

	private static String executionPath;

	@BeforeAll
	static void setUp()
	{
		executionPath = System.getProperty("user.dir"); // Set up a known path, e.g., project
														// directory
	}

	/**
	 * Tests {@link CommandLineExecutor#execute(String, String, OutputStream)} with a simple command
	 * and validates the output.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the command execution is interrupted
	 */
	@Test
	public void testExecuteWithAdditionalOutput() throws IOException, InterruptedException
	{
		String command = "echo Hello, World!";
		ByteArrayOutputStream additionalOutput = new ByteArrayOutputStream();

		String result = CommandLineExecutor.execute(executionPath, command, additionalOutput);

		assertNotNull(result);
		assertTrue(result.contains("Hello, World!"), "Expected output not found in the result");
		assertTrue(additionalOutput.toString().contains("Hello, World!"),
			"Expected output not found in additional output stream");
	}

	/**
	 * Tests {@link CommandLineExecutor#execute(String, String, OutputStream)} with an invalid
	 * command to validate error handling.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the command execution is interrupted
	 */
	@Test
	public void testExecuteWithInvalidCommand() throws IOException, InterruptedException
	{
		String command = "invalidcommand"; // A command that doesn't exist
		ByteArrayOutputStream additionalOutput = new ByteArrayOutputStream();

		String result = CommandLineExecutor.execute(executionPath, command, additionalOutput);
		String additionalOutputString = additionalOutput.toString();
		// Check if the result and additional output contain an indication of an error or exit code
		assertTrue(result.contains("Exit code") || result.contains("not found"),
			"Expected exit code or error message in result for an invalid command");
	}

	/**
	 * Tests {@link CommandLineExecutor#execute(String, String, OutputStream)} with a multi-line
	 * output command.
	 *
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the command execution is interrupted
	 */
	@Test
	public void testExecuteMultiLineOutput() throws IOException, InterruptedException
	{
		String command = "echo Line1 && echo Line2";
		ByteArrayOutputStream additionalOutput = new ByteArrayOutputStream();

		String result = CommandLineExecutor.execute(executionPath, command, additionalOutput);

		assertTrue(result.contains("Line1") && result.contains("Line2"),
			"Expected multi-line output in result");
		assertTrue(
			additionalOutput.toString().contains("Line1")
				&& additionalOutput.toString().contains("Line2"),
			"Expected multi-line output in additional output stream");
	}

	/**
	 * Tests {@link CommandLineExecutor#getCommandList(String[], OS)} for constructing command list
	 * for different operating systems.
	 */
	@Test
	public void testGetCommandList()
	{
		String[] commands = { "echo", "Hello" };

		// Test on a simulated Windows environment
		List<String> commandListWindows = CommandLineExecutor.getCommandList(commands, OS.WINDOWS);
		assertTrue(commandListWindows.contains("cmd.exe") && commandListWindows.contains("/c"),
			"Expected Windows command structure");

		// Test on a simulated Unix/Linux environment
		List<String> commandListUnix = CommandLineExecutor.getCommandList(commands, OS.LINUX);
		assertTrue(commandListUnix.contains("/bin/bash") && commandListUnix.contains("-c"),
			"Expected Unix command structure");
	}

	/**
	 * Bean test method for {@link CommandLineExecutor} to verify it meets JavaBean standards
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CommandLineExecutor.class);
	}
}
