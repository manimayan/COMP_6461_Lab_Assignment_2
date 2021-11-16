package com.cn6461.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Httpclient {

	static final String HEADER = "header";
	static final String BODY = "body";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println("\t##~COMP6461 HTTPCLIENT~##\n");
		System.out.println("Enter \"httpc help\" for more information!");
		while (true) {
			Map<String, String> responseMap = null;
			System.out.print("\n6461 HttpClient> ");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String command = scanner.nextLine();
			if (command.contentEquals("httpc help")) {
				System.out.println("\nhttpc is a curl-like application but supports HTTP protocol only.");
				System.out.println("usage: httpc command [arguments]");
				System.out.println("\tThe commands are:");
				System.out.println("\t\tget : executes a HTTP GET request and prints the response."
						+ "\n\t\tpost : executes a HTTP POST request and prints the response."
						+ "\n\t\thelp : prints this screen");
				System.out.println("\tUse \"httpc help [command]\" for more information about a command.\n");
			} else if (command.contentEquals("httpc help get")) {
				System.out.println("\nusage: httpc get [-v] [-h key:value] URL");
				System.out.println("\tget executes a HTTP GET request for a given URL.");
				System.out.println("\t\t-v \t\tPrints the detail of the response such as protocol, status, and headers."
						+ "\n\t\t-h key:value\tAssociates headers to HTTP Request with the format 'key:value'\n");
			} else if (command.contentEquals("httpc help post")) {
				System.out.println("\nusage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL");
				System.out
						.println("\tPost executes a HTTP POST request for a given URL with inline data or from file.");
				System.out.println("\t\t-v\tPrints the detail of the response such as protocol, status, and headers."
						+ "\n\t\t-h\tkey:value Associates headers to HTTP Request with the format 'key:value'"
						+ "\n\t\t-d\tstringAssociates an inline data to the body HTTP POST request."
						+ "\n\t\t-f\tfileAssociates the content of a file to the body HTTP POST request.\n");

			} else if (command.contains("-t")) {
				// httpc -t 'http://google.com'
				try {
					String URL = null;
					String[] input = command.split(" ");
					for (String string : input) {
						if (string.startsWith("\'") && string.endsWith("\'")) {
							URL = string.replace("\'", "");
						}
					}
					responseMap = HttpMethods.APICall(URL);
					System.out.println("\n" + responseMap.get(HEADER));
					System.out.println("\n" + responseMap.get(BODY));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (command.contains("-l") && (command.contains("get") || command.contains("post"))) {
				// httpc get -l 'http://localhost:8086/get/'
				// httpc get -l 'http://localhost:8086/get/foo'
				// httpc post -l --d {"Assignment": 1} 'http://localhost:8086/post/bar'
				try {
					String URL = null;
					String[] input = command.split(" ");
					for (String string : input) {
						if (string.startsWith("\'") && string.endsWith("\'")) {
							URL = string.replace("\'", "");
						}
					}
					String method = input[1].equalsIgnoreCase("get") ? "GET" : input[1].equalsIgnoreCase("post") ? "POST" : "";
					if(method.equalsIgnoreCase("GET")) {
						responseMap = HttpMethods.fileServerGetCall(URL+" "+method,command);
					} if(method.equalsIgnoreCase("POST")) {
						responseMap = HttpMethods.fileServerPostCall(URL+" "+method, StringUtils.substringBetween(command, "{", "}"));
					}
					System.out.println("\n" + responseMap.get(HEADER));
					System.out.println("\n" + responseMap.get(BODY));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (command.contains("-o") && (command.contains(HttpMethods.GETURL))) {
				// httpc -v 'http://httpbin.org/get?course=networking&assignment=1' -o 6461.txt
				try {
					String fileName = null;
					String[] input = command.split(" ");
					for (String string : input) {
						if (string.endsWith(".txt")) {
							fileName = string;
						}
					}
					responseMap = HttpMethods.APICall(HttpMethods.GETURL);
					PrintWriter write = new PrintWriter(new FileOutputStream(fileName));
					write.println(responseMap.get(HEADER));
					write.println(responseMap.get(BODY));
					write.close();
					System.out.println("File Write Completed");
				} catch (IOException e) {
					System.out.println("Exception Ocuured: Problem in wrting file");
				}
			} else if (command.contains("get") && command.contains("-r")
					&& (command.contains(HttpMethods.REDIRECT_URL))) {
				// httpc get -r 'https://httpbingo.org/redirect-to?url=http://www.example.com'
				try {
					responseMap = HttpMethods.APICall(HttpMethods.REDIRECT_URL);
					System.out.println("\n" + responseMap.get(HEADER));
					System.out.println("\n" + responseMap.get(BODY));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if ((command.contains("get")) && (command.contains("-v"))
					&& (command.contains(HttpMethods.GETURL))) {
				// httpc get -v 'http://httpbin.org/get?course=networking&assignment=1'
				try {
					responseMap = HttpMethods.APICall(HttpMethods.GETURL);
					System.out.println("\n" + responseMap.get(HEADER));
					System.out.println("\n" + responseMap.get(BODY));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if ((command.contains("get")) && (command.contains(HttpMethods.GETURL))) {
				// httpc get 'http://httpbin.org/get?course=networking&assignment=1'
				try {
					responseMap = HttpMethods.APICall(HttpMethods.GETURL);
					System.out.println("\n" + responseMap.get(BODY));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if ((command.contains("post")) && (command.contains("-h")) && (command.contains("-d"))
					&& (command.contains(HttpMethods.POSTURL))) {
				// httpc post -h Content-Type:application/json --d '{"Assignment": 1}'
				// http://httpbin.org/post
				try {
					responseMap = HttpMethods.APIPostCall(HttpMethods.POSTURL, "{\"Assignment\": 1}");
					System.out.println("\n" + responseMap.get(BODY));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Missing or error HTTP protocol.");
				System.out.println("httpc (get|post) [-v] (-h \"k:v\")* [-d inline-data] [-f file] URL");
			}
		}
	}
}