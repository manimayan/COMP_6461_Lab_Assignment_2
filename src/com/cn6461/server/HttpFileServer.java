package com.cn6461.server;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class HttpFileServer.
 */
public class HttpFileServer {

	/**
	 * The Enum RequestType.
	 */
	public enum RequestType {
		
		/** The get. */
		GET, 
 /** The post. */
 POST
	}

	/** The valid directory. */
	static String validDirectory = "./resources/";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		boolean verbose = false;
		System.out.println("\t##~COMP6461 HTTPFileServer~##\n");
		System.out.println("Enter \"httpfs help\" for more information!");
		while (true) {
			System.out.print("\n6461 HttpFileServer> ");

			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String command = scanner.nextLine();

			if (command.contains("help")) {
				System.out.println("httpfs [-v] [-p PORT] [-d PATH-TO-DIR]");
				System.out.println("-v - Verbose\n-p Port\n-d path of the directory a user wants to connect");
			} else if (command.contains("-d") && command.contains("-p")) {
				if (command.contains("-v")) {
					Server.run(findPort(command), true, findDirectory(command));
				} else {
					Server.run(findPort(command), verbose, findDirectory(command));
				}
				break;
			} else {
				System.out.println("Missing File Server parameters.");
				System.out.println("\nhttpfs [-v] [-p PORT] [-d PATH-TO-DIR]");
				System.out.println("-v - Verbose\n-p Port\n-d path of the directory a user wants to connect");
			}
		}
	}

	/**
	 * Find port.
	 *
	 * @param input the input
	 * @return the int
	 */
	private static int findPort(String input) {
		return Integer.parseInt(StringUtils.substringBetween(input, "-p", "-d").replaceAll("\\s", ""));
	}

	/**
	 * Find directory.
	 *
	 * @param input the input
	 * @return the string
	 */
	private static String findDirectory(String input) {
		String directory = StringUtils.substringAfter(input, "-d").replaceAll("\\s", "");
		if (!directory.startsWith(validDirectory)) {
			System.out.println(
					"Specified Directory is not permitted, access changed to default directory" + validDirectory);
			directory = validDirectory;
		}
		return directory;
	}

}