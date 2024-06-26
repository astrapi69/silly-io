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

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link LinuxShellExecutor}.
 */
public class LinuxShellExecutorTest
{

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

}
