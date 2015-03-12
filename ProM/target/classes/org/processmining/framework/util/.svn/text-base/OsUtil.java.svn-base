package org.processmining.framework.util;

import java.io.File;
import java.io.IOException;

import org.processmining.framework.boot.Boot;

import sun.management.ManagementFactoryHelper;

import com.sun.management.OperatingSystemMXBean;

public class OsUtil {

	public static final String OS_WIN32 = "Windows 32 bit";
	public static final String OS_WIN64 = "Windows 64 bit";
	public static final String OS_MACOSX = "Mac OS X";
	public static final String OS_MACOSCLASSIC = "Mac OS 7-9";
	public static final String OS_LINUX = "Linux";
	public static final String OS_BSD = "BSD";
	public static final String OS_RISCOS = "RISC OS";
	public static final String OS_BEOS = "BeOS";
	public static final String OS_UNKNOWN = "unknown";

	private static String currentOs = null;

	public static String determineOS() {
		if (currentOs == null) {
			String osString = System.getProperty("os.name").trim().toLowerCase();
			if (osString.startsWith("windows")) {
				currentOs = OS_WIN32;
			} else if (osString.startsWith("mac os x")) {
				currentOs = OS_MACOSX;
			} else if (osString.startsWith("mac os")) {
				currentOs = OS_MACOSCLASSIC;
			} else if (osString.startsWith("risc os")) {
				currentOs = OS_RISCOS;
			} else if ((osString.indexOf("linux") >= 0) || (osString.indexOf("debian") >= 0)
					|| (osString.indexOf("redhat") >= 0) || (osString.indexOf("lindows") >= 0)) {
				currentOs = OS_LINUX;
			} else if ((osString.indexOf("freebsd") >= 0) || (osString.indexOf("openbsd") >= 0)
					|| (osString.indexOf("netbsd") >= 0) || (osString.indexOf("irix") >= 0)
					|| (osString.indexOf("solaris") >= 0) || (osString.indexOf("sunos") >= 0)
					|| (osString.indexOf("hp/ux") >= 0) || (osString.indexOf("risc ix") >= 0)
					|| (osString.indexOf("dg/ux") >= 0)) {
				currentOs = OS_BSD;
			} else if (osString.indexOf("beos") >= 0) {
				currentOs = OS_BEOS;
			} else {
				currentOs = OS_UNKNOWN;
			}
		}
		return currentOs;
	}

	public static boolean is64Bit() {
		return System.getProperty("sun.arch.data.model").equals("64");
	}

	public static boolean is32Bit() {
		return System.getProperty("sun.arch.data.model").equals("32");
	}

	public static boolean isRunningWindows() {
		return determineOS() == OS_WIN32;
	}

	public static boolean isRunningMacOsX() {
		return determineOS() == OS_MACOSX;
	}

	public static boolean isRunningLinux() {
		return determineOS() == OS_LINUX;
	}

	public static boolean isRunningUnix() {
		String os = determineOS();
		return (os == OS_BSD) || (os == OS_LINUX) || (os == OS_MACOSX);
	}

	public static void setWorkingDirectoryAtStartup() {
		if (isRunningMacOsX()) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			File here = new File(".");
			try {
				if (new File(here.getAbsolutePath() + "/ProM.app").exists()) {
					System.out.println("--> Mac OS X: running from application bundle (1).");
					File nextHere = new File(here.getCanonicalPath() + "/ProM.app/Contents/Resources/ProMhome");
					System.setProperty("user.dir", nextHere.getCanonicalPath());
				} else if (here.getAbsolutePath().matches("^(.*)ProM\\.app(/*)$")) {
					System.out.println("--> Mac OS X: running from application bundle (2).");
					File nextHere = new File(here.getCanonicalPath() + "/Contents/Resources/ProMhome");
					System.setProperty("user.dir", nextHere.getCanonicalPath());
				}
				System.out.println("Mac OS X: Working directory set to " + System.getProperty("user.dir") + " (from "
						+ here.getAbsolutePath() + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Mac OS X: Working directory set to " + System.getProperty("user.dir") + " (from "
					+ here.getAbsolutePath() + ")");
		}
	}

	//	public static File getProMUserDirectory() {
	//		File dir = new File(Boot.PROM_USER_FOLDER);
	//		//System.getProperty("user.home", ""), ".ProM");
	//		dir.mkdirs();
	//		return dir;
	//	}

	public static File getProMPackageDirectory() {
		File dir = new File(Boot.PACKAGE_FOLDER);
		dir.mkdirs();
		return dir;
	}

	public static File getProMWorkspaceDirectory() {
		File dir = new File(Boot.WORKSPACE_FOLDER);
		dir.mkdirs();
		return dir;
	}

	public static long getPhysicalMemory() {
		OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactoryHelper.getOperatingSystemMXBean();
		return operatingSystemMXBean.getTotalPhysicalMemorySize();		
	}
}
