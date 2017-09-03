package utils;

public class Debug {
	/**
	 * This class is used for all output to allow for saving to a log file should that be implemented
	 * @param str
	 */
	public static void print(String str) {
		System.err.print(str);
	}
	public static void println(String str) {
		System.err.println(str);
	}
	
}
