package com.cn6461.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Server {

	public static final String STATUS_OK = "200 OK";
	public static final String STATUS_CREATED = "201 Created";
	static final String STATUS_FORBIDDEN = "403 Forbidden";
	static final String STATUS_NOT_FOUND = "404 Not Found";
	static final String STATUS_ERROR = "500 Internal Server Error";

	/**
	 * Run.
	 *
	 * @param port      the port
	 * @param verbose   the verbose
	 * @param directory the directory
	 */
	public static void run(int port, boolean verbose, String directory) {
		System.out.println((!verbose) ? "" : "Server starting...");
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server started");
			System.out.println((!verbose) ? "" : "Server listening on port " + port);
			while (true) {
				try (Socket client = serverSocket.accept();) {

					System.out.println((!verbose) ? "" : "Client Connected\r\n");
					List<String> request = getRequest(client);
					handleClient(client, request, directory);

					System.out.println((!verbose) ? "" : "Client Disonnected\r\n");
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static List<String> getRequest(Socket client) throws IOException {
		StringBuilder requestBuilder = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String line;
		while (!(line = br.readLine()).isEmpty()) {
			requestBuilder.append(line + "\r\n");
		}
		return Arrays.asList(requestBuilder.toString().split("\r\n"));
	}

	public static void handleClient(Socket client, List<String> request, String directory) throws IOException {
		if (request.get(0).contains(("GET")) || request.get(0).contains(("get"))) {
			handleGET(client, request, directory);
		} else if (request.get(0).contains(("POST")) || request.get(0).contains(("post"))) {
			handlePOST(client, request, directory);
		}
	}

	public static void handleGET(Socket client, List<String> request, String directory) throws IOException {
		String returnCode = STATUS_OK;
		File file = new File(makeGetUrl(request, directory));
		String directoryContents[] = file.list();
		String content = new String();
		try {
			if (file.isDirectory()) {
				System.out.println("Opening directory: " + directory);
				System.out.println("File Count: " + file.list().length);
				for (int i = 0; i < directoryContents.length; i++) {
					content += directoryContents[i] + "\n";
				}
			} else if (file.isFile()) {
				content = readFile(file);
				System.out.println("File contents: \n" + content);
			} else if (!file.isFile()) {
				returnCode = STATUS_NOT_FOUND;
			} else if (!file.canRead()) {
				returnCode = STATUS_FORBIDDEN;
			} else {
				returnCode = STATUS_ERROR;
			}
		} catch (FileNotFoundException fe) {
			returnCode = STATUS_NOT_FOUND;
		} catch (Exception e) {
		}
		String updatedContent = null;
		if (!StringUtils.isEmpty(content) && request.size() > 2) {
			String contentType = createContentType(request, content);
			updatedContent = createContentDisposition(request, contentType, HttpFileServer.validDirectory);
		}
		Server.sendResponse(client, returnCode,
				StringUtils.isEmpty(content) ? "File not found" : updatedContent != null ? updatedContent : content,
				request.size() > 2 ? request.get(2) : "Content-Type: text/plain");
	}

	private static String createContentType(List<String> request, String content) {
		String contentType = request.get(2);
		if (contentType.contains("application/json")) {
			String[] contentArray = content.split("\n");
			String updatedContent = new String();
			for (String string : contentArray) {
				updatedContent += string + ", ";
			}
			content = "data : { " + updatedContent + "}";
		}
		return content;
	}

	private static String createContentDisposition(List<String> request, String content, String validDirectory)
			throws FileNotFoundException {
		String contentDisposition = request.get(3);
		String fileName = null;
		if (contentDisposition.contains("attachment")) {
			fileName = StringUtils.substringBetween(contentDisposition, "^", "^");
			try (PrintWriter pw = new PrintWriter(new FileOutputStream(validDirectory + fileName, true));) {
				pw.write(content);
			}
			return "data fetched in " + fileName;
		}
		return content;
	}

	private static String makeGetUrl(List<String> request, String directory) {
		String[] splitURL = request.get(0).split(" ");
		splitURL = splitURL[0].split("/", 3);
		return directory + splitURL[2].trim();
	}

	public static void handlePOST(Socket client, List<String> request, String directory) throws IOException {
		String returnCode = STATUS_CREATED;
		File file = new File(makePostUrl(request, directory));
		try {
			if (file.isFile()) {
				try (PrintWriter pw = new PrintWriter(new FileOutputStream(makePostUrl(request, directory), true));) {
					pw.write(request.get(3));
				}
			} else if (!file.isFile()) {
				returnCode = STATUS_CREATED;
				try (PrintWriter pw = new PrintWriter(new FileOutputStream(makePostUrl(request, directory)));) {
					pw.write(request.get(3));
				}
			} else if (!file.canRead()) {
				returnCode = STATUS_FORBIDDEN;
			} else {
				returnCode = STATUS_ERROR;
			}
		} catch (FileNotFoundException fe) {
			returnCode = STATUS_NOT_FOUND;
		} catch (Exception e) {
			returnCode = STATUS_ERROR;
		}
		Server.sendResponse(client, returnCode, "Data Created:\r\n" + request.get(3), "Content-Type: text/plain");
	}

	private static String makePostUrl(List<String> request, String directory) {
		String[] splitURL = request.get(0).split(" ");
		splitURL = splitURL[0].split("/", 3);
		return directory + splitURL[2].trim();
	}

	public static void sendResponse(Socket client, String responseCode, String content, String contentType)
			throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		OutputStream clientOutput = client.getOutputStream();
		StringBuilder clientResponse = new StringBuilder();
		clientResponse.append("HTTP/1.0 " + responseCode + "\r\n");
		clientResponse.append("Server: " + client.getInetAddress() + "\r\n");
		clientResponse.append(sdf.format(Calendar.getInstance().getTime()));
		clientResponse.append("\r\n" + contentType + "\r\n");
		clientResponse.append("Content-Length:" + content.getBytes().length + "\r\n");
		clientResponse.append("\n" + content + "\n");
		clientOutput.write(clientResponse.toString().getBytes());
		clientOutput.flush();
	}

	public static String readFile(String filePath) throws FileNotFoundException {
		StringBuilder data = new StringBuilder();
		File file = new File(filePath);

		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			data.append(scanner.nextLine());
		}
		scanner.close();
		return data.toString();
	}

	public static String readFile(File file) throws FileNotFoundException {
		return readFile(file.getAbsolutePath());
	}
}
