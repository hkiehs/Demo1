package com.sheikhmuneeb.demo.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpManager {
	private static final String DEBUG_TAG = "HttpManager";
	private static final String GET_COUNTRY_DETAILS_URL = "http://bit.ly/1CgxlqK";

	public static String getCountryDetails() {
		return getRequestServer(GET_COUNTRY_DETAILS_URL);
	}

	// API Helpers
	private static String getRequestServer(String url) {
		return httpRequest(url, false, null);
	}

	private static String httpRequest(String url, boolean isPost, String postDataJson) {
		String responseStr = null;
		HttpURLConnection urlConnection = null;
		try {
			URL urlObj = new URL(url);
			System.setProperty("http.keepAlive", "false");
			urlConnection = (HttpURLConnection) urlObj.openConnection();
			// if POST request
			urlConnection.setDoOutput(isPost);
			// if post data provided, write to request
			if (postDataJson != null) {
				urlConnection.setRequestProperty("Content-Type",
						"application/json");

				urlConnection
						.setFixedLengthStreamingMode(postDataJson.length());
				OutputStreamWriter osw = new OutputStreamWriter(
						urlConnection.getOutputStream());
				osw.write(postDataJson);
				osw.close();
			}
			Log.d(DEBUG_TAG, "" + urlConnection.getResponseCode());
			// read response
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			responseStr = readStream(in);
		} catch (MalformedURLException e) {
			Log.e(DEBUG_TAG, "httpRequest", e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "httpRequest", e);
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return responseStr;
	}

	private static String readStream(InputStream in) {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				in));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {
			while ((line = bufferReader.readLine()) != null)
				stringBuilder.append(line);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "readStream", e);
		}
		return stringBuilder == null ? null : stringBuilder.toString();
	}
}
