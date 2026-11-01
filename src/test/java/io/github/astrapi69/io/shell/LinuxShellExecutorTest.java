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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link LinuxShellExecutor}.
 */
public class LinuxShellExecutorTest
{

	@TempDir
	Path directory;

	/**
	 * The cases that run harmless commands in bash need a system that has it
	 */
	private static void assumeBash()
	{
		assumeTrue(Files.isExecutable(Path.of("/bin/bash")), "needs /bin/bash");
	}

	/**
	 * Test method for {@link LinuxShellExecutor#execute(boolean, String...)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExecute() throws IOException
	{
		String actual;
		actual = LinuxShellExecutor.execute(true, "ls -al");
		assertNotNull(actual);
	}


	@Test
	void execute() throws IOException, InterruptedException
	{
		String actual;
		String shellPath;
		String executionPath;
		String command;

		shellPath = "bash";

		executionPath = PathFinder.getProjectDirectory().getAbsolutePath();
		command = "ls -al";

		actual = LinuxShellExecutor.execute(shellPath, executionPath, command);
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link LinuxShellExecutor}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(LinuxShellExecutor.class);
	}

	@Test
	void execute_runsTheCommandInTheExecutionDirectory() throws IOException, InterruptedException
	{
		assumeBash();
		assertEquals(directory.toString() + System.lineSeparator(),
			LinuxShellExecutor.execute("bash", directory.toString(), "pwd"));
	}

	/**
	 * The name of the execution directory is data, never shell code: the command runs in the
	 * directory, and nothing the name holds runs. A {@code $(echo ..)} that ran would move the
	 * command to the parent directory, a {@code ; echo injected} that ran would print
	 * {@code injected}
	 *
	 * @param name
	 *            the name of the execution directory
	 * @throws IOException
	 *             if the directory cannot be created
	 * @throws InterruptedException
	 *             if the thread is interrupted while the command runs
	 */
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "with space", "quote ' in it", "semicolon; echo injected",
			"$(echo ..)", "`echo ..`" })
	void execute_runsTheCommandInTheExecutionDirectory_andNothingItsNameHolds(final String name)
		throws IOException, InterruptedException
	{
		assumeBash();
		final Path executionDirectory = Files.createDirectory(directory.resolve(name));

		assertEquals(executionDirectory + System.lineSeparator(),
			LinuxShellExecutor.execute("bash", executionDirectory.toString(), "pwd"));
	}

	@Test
	void execute_appendsTheExitCode_whenTheCommandFails() throws IOException, InterruptedException
	{
		assumeBash();
		assertEquals("partial" + System.lineSeparator() + "Exit code: 3\n",
			LinuxShellExecutor.execute("bash", directory.toString(), "echo partial; exit 3"));
	}

	@Test
	void execute_replacesTheTildeWithTheHomeDirectory() throws IOException, InterruptedException
	{
		assumeBash();
		final String home = System.getProperty("user.home");

		assertEquals(home + System.lineSeparator(), LinuxShellExecutor.execute("bash", "~", "pwd"));
	}

	@Test
	void execute_throwsIllegalArgumentException_forAnExecutionDirectoryThatDoesNotExist()
	{
		final String missing = directory.resolve("missing").toString();

		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> LinuxShellExecutor.execute("bash", missing, "echo never"));
		assertEquals("Execution directory does not exist", exception.getMessage());
	}

	@Test
	void execute_returnsTheOutput_withResponse() throws IOException
	{
		assumeBash();
		assertEquals("answer\n", LinuxShellExecutor.execute(true, "echo answer"));
	}

	@Test
	void execute_returnsAnEmptyString_withoutResponse() throws IOException
	{
		assumeBash();
		assertEquals("", LinuxShellExecutor.execute(false, "echo unread"));
	}
}
