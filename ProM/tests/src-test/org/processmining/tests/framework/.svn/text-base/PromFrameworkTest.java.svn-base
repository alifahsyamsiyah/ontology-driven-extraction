package org.processmining.tests.framework;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Test;
import org.processmining.contexts.cli.CLI;
import org.processmining.contexts.scripting.ScriptExecutor.ScriptExecutionException;
import org.processmining.contexts.test.PromTest;
import org.processmining.framework.packages.CommandLineInterface;
import org.processmining.framework.packages.PackageManager;

public class PromFrameworkTest extends PromTest {

	@Test
	public void testPromFramework_CLIbasic() throws Throwable {
		String args[] = new String[] { "-l" };

		CLI.main(args);
	}

	@Test
	public void testPromFramework_catchJUnitErrorsInScript() throws Throwable {
		String testFileRoot = System.getProperty("test.testFileRoot", defaultTestDir);
		String args[] = new String[] { "-f", testFileRoot + "/test_PromFrameWork_JUnitFailTest.txt" };

		try {
			CLI.main(args);
		} catch (Throwable e) {

			if (e instanceof AssertionFailedError && ((AssertionFailedError) e).getMessage().equals("fail test")) {
				return;
			} else {
				throw e;
			}
		}

		// the test-script contains a test that must fail, but the corresponding
		// AssertFailedError was not thrown: so, the test for checking the throwing
		// of failures failed
		Assert.assertTrue("Fabricated failure in test-script recgonized by JUnit", false);
	}
	
	@Test
	public void testPromFramework_catchNotExecutableScript() throws Throwable {
		String testFileRoot = System.getProperty("test.testFileRoot", defaultTestDir);
		String args[] = new String[] { "-f", testFileRoot + "/test_PromFrameWork_notExecutable.txt" };

		try {
			CLI.main(args);
		} catch (Throwable e) {

			if (e instanceof ScriptExecutionException) {
				return;
			} else {
				throw e;
			}
		}

		// the test-script contains a test that must fail, but the corresponding
		// AssertFailedError was not thrown: so, the test for checking the throwing
		// of failures failed
		Assert.assertTrue("Unknown command in script recoginzed by JUnit", false);
	}
	
	@Test
	public void testPackageManager() throws Throwable {
		String args[] = new String[] { "list" };
		CommandLineInterface cli = new CommandLineInterface(PackageManager.getInstance());
		cli.run(args);
	}
	
	@Test
	public void testPromFramework_invokeOtherPlugins1() throws Throwable {
		String testFileRoot = System.getProperty("test.testFileRoot", defaultTestDir);
		String args[] = new String[] { "-f", testFileRoot + "/test_PromFramework_invokeOtherPlugins1.txt" };
		CLI.main(args);
	}
	
	@Test
	public void testPromFramework_invokeOtherPlugins2() throws Throwable {
		String testFileRoot = System.getProperty("test.testFileRoot", defaultTestDir);
		String args[] = new String[] { "-f", testFileRoot + "/test_PromFramework_invokeOtherPlugins2.txt" };
		for (int i=0; i<15;i++) {
			CLI.main(args);
		}
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PromFrameworkTest.class);
	}

}
