package com.finokeyo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonValue;
import com.finokeyo.model.dto.KftcApi;


public class KftcAPI {

	public KftcAPI() {

	}
	
	private static KftcApi kftc = new KftcApi();
	private static String clientId = kftc.getClient_id();
	private static String clientSecret = kftc.getClient_secret();
	private static String redirectUri = kftc.getRedirect_uri();
	private static String fintechUseNum = kftc.getFintechUseNum();
	

	public static String callktfc() {
		String URI = "https://testapi.openbanking.or.kr/oauth/2.0/authorize?" 
				+ "&response_type=code"
				+ "&client_id="+clientId
				+ "&redirect_uri="+redirectUri
				+ "&scope=login+inquiry+transfer" 
				+ "&state=12345678901234567890123456789012" 
				+ "&auth_type=0";
		return URI;
	}

	public static Map<String, String> getToken(String authorization_code) throws IOException, ParseException {

		String apiURL = "https://testapi.openbanking.or.kr/oauth/2.0/token";
		URL url = new URL(apiURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setDoOutput(true);
		
		String param = "code=" + authorization_code + "&client_id="+clientId
				+ "&client_secret="+clientSecret
				+ "&redirect_uri="+redirectUri
				+ "&grant_type=authorization_code";
		OutputStream wr = conn.getOutputStream();
		wr.write(param.getBytes("UTF-8"));
		wr.flush();
		wr.close();
		
		InputStream is = conn.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

		String line = null;
		StringBuffer buff = new StringBuffer();

		while ((line = in.readLine()) != null) {
			buff.append(line + "\n");
		}

		String data = buff.toString().trim();
		
		

		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(data);

		String accessToken = String.valueOf(json.get("access_token"));
		String tokenType = String.valueOf(json.get("token_type"));
		String refreshToken = String.valueOf(json.get("refresh_token"));
		String expiresIn = String.valueOf(json.get("expires_in"));
		String scope = String.valueOf(json.get("scope"));
		String userSeqNo = String.valueOf(json.get("user_seq_no"));

		Map<String, String> token = new HashMap<String, String>();
		token.put("accessToken", accessToken);
		token.put("refreshToken", refreshToken);
		token.put("userSeqNo", userSeqNo);
		return token;

	}

	public static Map<String, String> getClientUseCode() throws IOException, ParseException {
		String apiURL = "https://testapi.openbanking.or.kr/oauth/2.0/token";
		URL url = new URL(apiURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setDoOutput(true);

		String param = "client_id=" + clientId + "&client_secret=" + clientSecret + "&scope=oob"
				+ "&grant_type=client_credentials";
		OutputStream wr = conn.getOutputStream();
		wr.write(param.getBytes("UTF-8"));
		wr.flush();
		wr.close();

		InputStream is = conn.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

		String line = null;
		StringBuffer buff = new StringBuffer();

		while ((line = in.readLine()) != null) {
			buff.append(line + "\n");
		}
		String data = buff.toString().trim();

		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(data);

		String clientUseCode = String.valueOf(json.get("client_use_code"));

		Map<String, String> token = new HashMap<String, String>();
		token.put("clientUseCode", clientUseCode);
		return token;
	}

	public static JSONObject accountList(ArrayList<Map<String, String>> tokenList)
			throws UnsupportedEncodingException, IOException, ParseException {
		
		int countnum = (int) (Math.floor(Math.random() * (999999999 - 100000000 + 1)) + 100000000);

		String accessToken = String.valueOf(tokenList.get(0).get("accessToken"));
		String clientUseCode = String.valueOf(tokenList.get(1).get("clientUseCode"));


		String param = "?bank_tran_id=" + clientUseCode + "U" + countnum + "&fintech_use_num=" + fintechUseNum
				+ "&inquiry_type=A" + "&inquiry_base=D" + "&from_date=20160101" + "&to_date=20191101" + "&sort_order=D"
				+ "&tran_dtime=20191114182544";

		String apiURL = "https://testapi.openbanking.or.kr/v2.0/account/transaction_list/fin_num" + param;
		URL url = new URL(apiURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setDoOutput(true);

		InputStream is = conn.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

		String line = null;
		StringBuffer buff = new StringBuffer();

		while ((line = in.readLine()) != null) {
			buff.append(line + "\n");
		}
		String data = buff.toString().trim();
		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(data);
		//json = (JSONObject) jsonParser.parse(String.valueOf(json.get("res_list")));
		return json;
	}
	
	public static JSONObject accountList(String accessToken, String clientUseCode)
			throws UnsupportedEncodingException, IOException, ParseException {
		
		int countnum = (int) (Math.floor(Math.random() * (999999999 - 100000000 + 1)) + 100000000);




		String param = "?bank_tran_id=" + clientUseCode + "U" + countnum + "&fintech_use_num=" + fintechUseNum
				+ "&inquiry_type=A" + "&inquiry_base=D" + "&from_date=20160101" + "&to_date=20191101" + "&sort_order=D"
				+ "&tran_dtime=20191114182544";

		String apiURL = "https://testapi.openbanking.or.kr/v2.0/account/transaction_list/fin_num" + param;
		URL url = new URL(apiURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setDoOutput(true);

		InputStream is = conn.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

		String line = null;
		StringBuffer buff = new StringBuffer();

		while ((line = in.readLine()) != null) {
			buff.append(line + "\n");
		}
		String data = buff.toString().trim();
		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(data);
		//json = (JSONObject) jsonParser.parse(String.valueOf(json.get("res_list")));
		return json;
	}


}