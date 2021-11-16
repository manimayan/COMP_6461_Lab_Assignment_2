package com.cn6461.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class HttpMethods.
 */
public class HttpMethods {

	/** The Constant PORT. */
	static final Integer PORT = 6876;
	
	/** The Constant GETURL. */
	static final String GETURL = "http://httpbin.org/get?course=networking&assignment=1";
	
	/** The Constant POSTURL. */
	static final String POSTURL = "http://httpbin.org/post";
	
	/** The Constant REDIRECT_URL. */
	static final String REDIRECT_URL = "https://httpbingo.org/redirect-to?url=http://www.example.com";

	/**
	 * API call.
	 *
	 * @param url the url
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, String> APICall(String url) throws IOException {

		Map<String, String> responseMap = new HashMap<>();
		URL urlObject = new URL(url);
		try (Socket socket = new Socket(InetAddress.getByName(urlObject.getHost()), PORT)) {
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println("GET /" + urlObject.getFile() + " HTTP/1.0");
			printWriter.println("Host: " + urlObject.getHost());
			printWriter.println("");
			printWriter.flush();
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
				String str;
				StringBuilder response = new StringBuilder();
				boolean header = false;

				while ((str = bufferedReader.readLine()) != null) {
					response.append(str + "\n");
					if (str.isEmpty() && !header) {
						responseMap.put("header", response.toString());
						header = true;
						response = new StringBuilder();
					}
				}
				responseMap.put("body", response.toString());
			}
		} catch (IOException io) {
			//
		}
		return responseMap;
	}

	/**
	 * API post call.
	 *
	 * @param url the url
	 * @param data the data
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, String> APIPostCall(String url, String data) throws IOException {
		Map<String, String> responseMap = new HashMap<>();
		URL urlObject = new URL(url);

		try (Socket socket = new Socket(InetAddress.getByName(urlObject.getHost()), PORT);) {
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println("POST /" + urlObject.getFile() + " HTTP/1.0");
			printWriter.println("Host: " + urlObject.getHost());
			printWriter.println("Content-Length: " + data.length());
			printWriter.println();
			printWriter.println(data);
			printWriter.println();
			printWriter.flush();
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
				String str;
				StringBuilder response = new StringBuilder();
				boolean header = false;

				while ((str = bufferedReader.readLine()) != null) {
					response.append(str + "\n");
					if (str.isEmpty() && !header) {
						responseMap.put("header", response.toString());
						header = true;
						response = new StringBuilder();
					}
				}
				responseMap.put("body", response.toString());
			}
		} catch (IOException io) {
			//
		}
		return responseMap;
	}

	/**
	 * File server get call.
	 *
	 * @param url the url
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, String> fileServerGetCall(String url) throws IOException {

		Map<String, String> responseMap = new HashMap<>();
		String[] urlPort = url.split(" ");
		URL urlObject = new URL(urlPort[0]);
		try (Socket socket = new Socket(InetAddress.getByName(urlObject.getHost()), PORT)) {
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(urlObject.getFile() + " HTTP/1.0");
			printWriter.println("Host: " + urlObject.getHost());
			printWriter.println("");
			printWriter.flush();
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
				String str;
				StringBuilder response = new StringBuilder();
				boolean header = false;

				while ((str = bufferedReader.readLine()) != null) {
					response.append(str + "\n");
					if (str.isEmpty() && !header) {
						responseMap.put("header", response.toString());
						header = true;
						response = new StringBuilder();
					}
					responseMap.put("body", response.toString());
				}
			}
		} catch (IOException io) {
			//
		}
		return responseMap;
	}

	/**
	 * File server post call.
	 *
	 * @param url the url
	 * @param data the data
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, String> fileServerPostCall(String url, String data) throws IOException {

		Map<String, String> responseMap = new HashMap<>();
		String[] urlPort = url.split(" ");
		URL urlObject = new URL(urlPort[0]);
		try (Socket socket = new Socket(InetAddress.getByName(urlObject.getHost()), PORT)) {
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(urlObject.getFile() + " HTTP/1.0");
			printWriter.println("Host: " + urlObject.getHost());
			printWriter.println("Content-Length: " + data.length());
			printWriter.println("data :" + data);
			printWriter.println("");
			printWriter.flush();
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
				String str;
				StringBuilder response = new StringBuilder();
				boolean header = false;
				while ((str = bufferedReader.readLine()) != null) {
					response.append(str + "\n");
					if (str.isEmpty() && !header) {
						responseMap.put("header", response.toString());
						header = true;
						response = new StringBuilder();
					}

				}
				responseMap.put("body", response.toString());
			}
		} catch (IOException io) {
//
		}
		return responseMap;
	}
}
