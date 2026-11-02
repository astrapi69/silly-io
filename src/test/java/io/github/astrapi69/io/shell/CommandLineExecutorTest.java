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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;


/**
 * Unit test class for {@link CommandLineExecutor}
 */
public class CommandLineExecutorTest
{

	private static String executionPath;

	@TempDir
	Path directory;

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
		assertEquals(result, additionalOutput.toString(),
			"The additional output stream mirrors the output, line separators included");
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

	static Stream<Arguments> shellsByOperatingSystem()
	{
		return Stream.of(arguments(OS.WINDOWS, List.of("cmd.exe", "/c", "echo", "Hello")),
			arguments(OS.LINUX, List.of("/bin/bash", "-c", "echo", "Hello")),
			arguments(OS.MAC, List.of("/bin/bash", "-c", "echo", "Hello")),
			arguments(OS.UNIX, List.of("/bin/sh", "-c", "echo", "Hello")));
	}

	/**
	 * Runs the given action while the system property {@code os.name} has the given value, and
	 * restores the previous value afterwards
	 */
	private static void withOperatingSystemName(final String osName, final Executable action)
		throws Throwable
	{
		final String previous = System.getProperty("os.name");
		System.setProperty("os.name", osName);
		try
		{
			action.execute();
		}
		finally
		{
			System.setProperty("os.name", previous);
		}
	}

	private static boolean isCmdExeOnThePath()
	{
		final String path = System.getenv("PATH");
		return path != null && Arrays.stream(path.split(File.pathSeparator))
			.anyMatch(entry -> Files.isExecutable(Path.of(entry, "cmd.exe")));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("shellsByOperatingSystem")
	void getCommandList_putsTheShellOfTheOperatingSystemBeforeTheCommands(final OS os,
		final List<String> expected)
	{
		assertEquals(expected,
			CommandLineExecutor.getCommandList(new String[] { "echo", "Hello" }, os));
	}

	@Test
	void getCommandList_throwsUnsupportedOperationException_forAnOtherOperatingSystem()
	{
		final UnsupportedOperationException exception = assertThrows(
			UnsupportedOperationException.class,
			() -> CommandLineExecutor.getCommandList(new String[] { "echo" }, OS.OTHER));
		assertEquals("Unsupported operating system: OTHER", exception.getMessage());
	}

	@Test
	void execute_throwsUnsupportedOperationException_onAnOperatingSystemWithoutAKnownShell()
		throws Throwable
	{
		withOperatingSystemName("Unknown OS", () -> {
			final UnsupportedOperationException exception = assertThrows(
				UnsupportedOperationException.class, () -> CommandLineExecutor
					.execute(directory.toString(), "echo never", new ByteArrayOutputStream()));
			assertEquals("Unsupported operating system: OTHER", exception.getMessage());
		});
	}

	/**
	 * The shell that runs the command, by the name of the operating system: bash on Linux and
	 * macOS, and on the other Unix systems the {@code /bin/sh} that POSIX guarantees. The command
	 * prints {@code $0}, which a shell started with {@code -c} and no further argument sets to the
	 * path it was started with
	 *
	 * @return the operating system name and the path of its shell, for each case
	 */
	static Stream<Arguments> shellsByOperatingSystemName()
	{
		return Stream.of(arguments("FreeBSD", "/bin/sh"), arguments("OpenBSD", "/bin/sh"),
			arguments("NetBSD", "/bin/sh"), arguments("SunOS", "/bin/sh"),
			arguments("Linux", "/bin/bash"), arguments("Mac OS X", "/bin/bash"));
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("shellsByOperatingSystemName")
	void execute_runsTheCommandInTheShellOfTheOperatingSystem(final String osName,
		final String shell) throws Throwable
	{
		assumeTrue(Files.isExecutable(Path.of(shell)), () -> "needs " + shell);

		withOperatingSystemName(osName,
			() -> assertEquals(shell + System.lineSeparator(), CommandLineExecutor
				.execute(directory.toString(), "echo $0", new ByteArrayOutputStream())));
	}

	/**
	 * On Windows the command goes to {@code cmd.exe}. Run on a system without it, so that starting
	 * it fails and names the shell it tried
	 *
	 * @throws Throwable
	 *             if the operating system name cannot be set
	 */
	@Test
	void execute_handsTheCommandToCmdExe_onWindows() throws Throwable
	{
		assumeTrue(File.separatorChar == '/', "needs a system without cmd.exe");
		assumeFalse(isCmdExeOnThePath(), "needs a system without cmd.exe on the PATH");

		withOperatingSystemName("Windows 10", () -> {
			final IOException exception = assertThrows(IOException.class, () -> CommandLineExecutor
				.execute(directory.toString(), "echo never", new ByteArrayOutputStream()));
			assertTrue(exception.getMessage().contains("\"cmd.exe\""), exception.getMessage());
		});
	}
}
