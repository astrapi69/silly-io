package io.github.astrapi69.io.shell;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The unit test class for the class {@link LinuxShellExecutor}.
 */
public class LinuxShellExecutorTest
{

	/**
	 * Test method for {@link LinuxShellExecutor#execute(boolean, String...)}
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test public void testExecute() throws IOException, InterruptedException
	{
		String actual;
		actual = LinuxShellExecutor.execute(true, "ls -al");
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link LinuxShellExecutor#toString(java.io.InputStream)}
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test public void testToString() throws IOException
	{
		String expected;
		String actual;
		expected = "";
		actual = LinuxShellExecutor.toString(null);
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link LinuxShellExecutor}
	 */
	@Test public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(LinuxShellExecutor.class);
	}

}

