package com.finokeyo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finokeyo.model.dto.RecommendCard;
import com.finokeyo.model.dto.User;
import com.finokeyo.model.service.RecoCardService;
import com.finokeyo.model.service.TokenStore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finokeyo.model.service.UserService;
import com.finokeyo.util.KftcAPI;


import io.swagger.annotations.ApiOperation;


@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/v1/kftc")
public class KftcController {

	private static final Logger logger = LoggerFactory.getLogger(KftcController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	private static KftcAPI api = new KftcAPI();

	@Autowired
	UserService uService;

	@Autowired
	TokenStore tokenStore;
	
	@Autowired
	RecoCardService cdService;
	
	static String name=null;
	
	
    @ApiOperation(value = "금융결제원 핸드폰 인증 리다이렉트, header에 access-token,refresh-token,seq-token,use-token"
    		+ "값 넘어갑니다")
	@GetMapping("{id}")
	public void phonedRedirect(HttpServletResponse res, @PathVariable String id) throws Exception {
		logger.debug("phoneRedirect - 호출");
		System.out.println("phoneRedirect - 호출");
		name= id;
		String phoneRE = api.callktfc();		
		res.sendRedirect(phoneRE);
	}

    
    @ApiOperation(value = "이건 신경 안써도 됩니다")
	@GetMapping("callback.html")
	public  void codeRedirect(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.debug("codeRedirect - 호출");
		System.out.println("codeRedirect - 호출");
		String code = req.getParameter("code");
		
		ArrayList<Map<String, String>> tokenList = new ArrayList<Map<String,String>>();
		tokenList.add(api.getToken(code));
		tokenList.add(api.getClientUseCode());
		
		String accessToken =String.valueOf(tokenList.get(0).get("accessToken"));
		String refreshToken =String.valueOf(tokenList.get(0).get("refreshToken"));
		String userSeqNo =String.valueOf(tokenList.get(0).get("userSeqNo"));
		String clientUseCode =String.valueOf(tokenList.get(1).get("clientUseCode"));
		
		System.out.println(accessToken);
		System.out.println(clientUseCode);

		tokenStore.storeToken(accessToken, refreshToken, userSeqNo, clientUseCode);
		
//		JSONObject account = api.accountList(tokenList);
		
		User user = uService.checkUserKftc(name);
		user.setCheckedin(true);
		uService.CheckUpdate(user);
	
		
		Map<String, Object> result = new HashMap<>();
		JSONObject accounts = api.accountList(accessToken,clientUseCode);   		
   		JSONArray arr = (JSONArray)accounts.get("res_list");
   		
   		ArrayList<Map<String, String>> refine = new ArrayList<Map<String,String>>();

		for(int i=0;i<arr.size();i++){
			
			JSONObject tmp = (JSONObject)arr.get(i);//인덱스 번호로 접근해서 가져온다.
			JSONObject json = new JSONObject();
			Map<String, String> list = new HashMap<>();
			String tran_date = (String)tmp.get("tran_date");
			String tran_time = (String)tmp.get("tran_time");
			String inout_type = (String)tmp.get("inout_type");
			String tran_type = (String)tmp.get("tran_type");
			String print_content = (String)tmp.get("print_content");
			String tran_amt = (String)tmp.get("tran_amt");
			String after_balance_amt = (String)tmp.get("after_balance_amt");
			String branch_name = (String)tmp.get("branch_name");
		
			if(inout_type.contains("출금")) {
				if(branch_name.contains("나이키")) branch_name="쇼핑";
				else if(branch_name.contains("스타벅스")) branch_name="커피";
				else if(branch_name.contains("포장마차")) branch_name="주점";
				else if(branch_name.contains("체육시설")) branch_name="레저";
				else if(branch_name.contains("음식점")) branch_name="음식";
			}
		
			list.put("tran_date",tran_date);
			list.put("branch_name",branch_name);
			list.put("tran_amt",tran_amt);
			json.putAll(list);
		    refine.add(json);
		}
		
		
		
		User uuser = uService.getUserInfo(name);
		
	
		
		
	
		String apiURL = " http://127.0.0.1:5000/kftcAI";
		URL url = new URL(apiURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
 
        String param = "list="+refine;
        
        OutputStream wr = conn.getOutputStream();
        wr.write(param.getBytes("UTF-8"));
        wr.flush();
        wr.close();

		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    
        String line = null;
        StringBuffer buff = new StringBuffer();
        while ((line = in.readLine()) != null) {
			buff.append(line + "\n");
		}
//		//{"cardid":{"0":1,"1":2,"2":3,"3":4,"4":5},//1,2,3,4,5  card 디비 no 값
//		//"name":{"0":"탄탄대로 Miz&Mr 티타늄카드","1":"NH올원 파이카드\n","2":"Easy all 티타늄카드","3":"현대카드M Edition3","4":"굿데이올림카드"},
//		//"predict":{"0":0.0,"1":0.0,"2":0.0,"3":0.0,"4":0.0},
//		//"company":{"0":"KB국민카드","1":"NH농협카드","2":"KB국민카드","3":"현대카드","4":"KB국민카드\n\n"}}
        
        
        //{"no":{"0":1,"1":6,"2":9,"3":14,"4":19,"5":29,"6":39,"7":59,"8":65,"9":75,"10":80,"11":106,"12":107,"13":108},
        //"name":{"0":"탄탄대로 Miz&Mr 티타늄카드","1":"현대카드M3 Edition3","2":"하나멤버스 1Q카드 내맘대로\n","3":"굿데이카드\n","4":"삼성카드 5V3\n","5":"삼성카드 & MILEAGE PLATINUM (스카이패스)\n","6":"네이버페이 taptap\n","7":"FINETECH카드(대한항공)\n","8":"스카이패스카드\n","9":"삼성카드 4 V4\n","10":"FINETECH카드(아시아나)\n","11":"아시아나클럽카드\n","12":"국민행복카드(신용)\n","13":"I'm Powerful\n"}}
        String data = buff.toString().trim();
        System.out.println(data);
        
		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(data);
		JSONObject array = (JSONObject)json.get("no");
		
		cdService.recoDel(name,2);
	
		for(int i =0 ;i<array.size();i++) {
			long no = (long) array.get(i+"");
			String cardNo = Long.toString(no);
			
			RecommendCard card = new RecommendCard();
			card.setCardid(cardNo);
			card.setRecommendType(2);
			card.setUserid(name);
			cdService.recoCard(card);
		}
		
		name=null;
		res.sendRedirect("http://finokeyo.s3-website.ap-northeast-2.amazonaws.com/next");
//		res.sendRedirect("http://localhost:8081/next");
//		res.sendRedirect("localhost:8081/join");
//		res.sendRedirect("http://localhost:8081/next");
	}

	@GetMapping("getToken")
	@ApiOperation(value = "getToken")
	public ResponseEntity<Map<String, Object>> getToken(HttpServletResponse res, HttpServletRequest req){
		logger.debug("getToken");
		System.out.println("getToken");
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, String> map = new HashMap<>();
			map = tokenStore.getToken();
			result.put("token", map);
			entity = handleSuccess(result);
		} catch (RuntimeException e){
			entity = handleException(e);
		}
		return entity;
	}
    
    
    @ApiOperation(value = "거래내역조회")
   	@GetMapping("accountList")
   	public  ResponseEntity<JSONArray> accountList(@RequestHeader(value="access-token") String accessToken,
    		@RequestHeader(value="use-token") String clientUseCode, HttpServletRequest req) throws Exception {
   		logger.debug("accountList - 호출");
   		System.out.println("accountList - 호출");
   		
   		JSONObject account = api.accountList(accessToken,clientUseCode);
   		System.out.println("==============accountList==============");
   		System.out.println(account);
   		System.out.println("==============accountList==============");
   		
   		JSONArray arr = (JSONArray)account.get("res_list");
   		System.out.println(arr);
   		return new ResponseEntity<JSONArray>(arr, HttpStatus.OK);
   	}
    
    @ApiOperation(value = "소비패턴분석, 리턴 예제 = [{스포츠매장=1105000, 편의점=167600, 체육시설=91000, 유흥시설=458000, 음식점=878000, 커피전문점=296900}]")
   	@GetMapping("ConsumptionPattern")
   	public  ResponseEntity<ArrayList<Map<String, Integer>>> ConsumptionPattern(@RequestHeader(value="access-token") String accessToken,
    		@RequestHeader(value="use-token") String clientUseCode, HttpServletRequest req) throws Exception {
   		System.out.println("소비패턴분석 호출");
//   		
//   		System.out.println("access-token"+accessToken);
//   		System.out.println("use-token"+clientUseCode);
//   		
   		JSONObject account = api.accountList(accessToken,clientUseCode);   		
   		JSONArray arr = (JSONArray)account.get("res_list");
//		System.out.println("arr"+arr);
		ArrayList<Map<String, Integer>> pattern = new ArrayList<Map<String,Integer>>();
		Map<String, Integer> list = new HashMap<String, Integer>();
		
		JSONObject json = new JSONObject();
		for(int i=0;i<arr.size();i++){
			JSONObject tmp = (JSONObject)arr.get(i);//인덱스 번호로 접근해서 가져온다.
						
			String tran_date = (String)tmp.get("tran_date");
			String tran_time = (String)tmp.get("tran_time");
			String inout_type = (String)tmp.get("inout_type");
			String tran_type = (String)tmp.get("tran_type");
			String print_content = (String)tmp.get("print_content");
			String tran_amt = (String)tmp.get("tran_amt");
			String after_balance_amt = (String)tmp.get("after_balance_amt");
			String branch_name = (String)tmp.get("branch_name");
			
			if(inout_type.contains("출금")) {
				if(branch_name.contains("나이키")) {
					branch_name="스포츠매장";
					int money = Integer.parseInt(tran_amt);
					if(list.get(branch_name)==null) {
						list.put(branch_name, money);
					}else {
						int temp = list.get(branch_name);
						list.replace(branch_name, temp+money);
					}
				}
				else if(branch_name.contains("스타벅스")) {
					branch_name="커피전문점";
					int money = Integer.parseInt(tran_amt);
					if(list.get(branch_name)==null) {
						list.put(branch_name, money);
					}else {
						int temp = list.get(branch_name);
						list.replace(branch_name, temp+money);
					}
				}
				else if(branch_name.contains("포장마차")) {
					branch_name="유흥시설";  //음식점
					int money = Integer.parseInt(tran_amt);
					if(list.get(branch_name)==null) {
						list.put(branch_name, money);
					}else {
						int temp = list.get(branch_name);
						list.replace(branch_name, temp+money);
					}
				}else {
					int money = Integer.parseInt(tran_amt);
					if(list.get(branch_name)==null) {
						list.put(branch_name, money);
					}else {
						int temp = list.get(branch_name);
						list.replace(branch_name, temp+money);
					}
				}
			}											
		}
		json.putAll( list );
		pattern.add(json);
		System.out.println("pattern : "+pattern);
    
   		return new ResponseEntity<ArrayList<Map<String, Integer>>>(pattern, HttpStatus.OK);
   	}

    
//    @ApiOperation(value = "AI연결")
//   	@GetMapping("aiPython")
//   	public  ResponseEntity<Map<String, Object>> aiPython(@RequestHeader(value="access-token") String accessToken,
//    		@RequestHeader(value="use-token") String clientUseCode, HttpServletRequest req, @RequestParam("uId") String uId) throws Exception {
//   		System.out.println("aiPython 호출");
// 
//		Map<String, Object> result = new HashMap<>();
// 		
//		JSONObject account = api.accountList(accessToken,clientUseCode);   		
//   		JSONArray arr = (JSONArray)account.get("res_list");
////   		System.out.println("==============AI연결==============");
////   		System.out.println(arr);
////   		System.out.println("==============AI연결==============");
//   		
//   		ArrayList<Map<String, String>> refine = new ArrayList<Map<String,String>>();
//
//		for(int i=0;i<arr.size();i++){
//			
//			JSONObject tmp = (JSONObject)arr.get(i);//인덱스 번호로 접근해서 가져온다.
//			JSONObject json = new JSONObject();
//			Map<String, String> list = new HashMap<>();
//			String tran_date = (String)tmp.get("tran_date");
//			String tran_time = (String)tmp.get("tran_time");
//			String inout_type = (String)tmp.get("inout_type");
//			String tran_type = (String)tmp.get("tran_type");
//			String print_content = (String)tmp.get("print_content");
//			String tran_amt = (String)tmp.get("tran_amt");
//			String after_balance_amt = (String)tmp.get("after_balance_amt");
//			String branch_name = (String)tmp.get("branch_name");
//		
//			if(inout_type.contains("출금")) {
//				if(branch_name.contains("나이키")) branch_name="쇼핑";
//				else if(branch_name.contains("스타벅스")) branch_name="커피";
//				else if(branch_name.contains("포장마차")) branch_name="주점";
//				else if(branch_name.contains("체육시설")) branch_name="레저";
//				else if(branch_name.contains("음식점")) branch_name="음식";
//			}
//		
//			list.put("tran_date",tran_date);
//			list.put("branch_name",branch_name);
//			list.put("tran_amt",tran_amt);
//			json.putAll(list);
//		    refine.add(json);
//		}
//		
//		
//		
//		User uuser = uService.getUserInfo(uId);
//		
//		result.put("userId", uuser.getKeyid());
//		result.put("account", refine);
//		
//	
//		String apiURL = " http://127.0.0.1:5000/test";
//		URL url = new URL(apiURL);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        //add reuqest header
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
// 
//        String param = "list="+result;
//        
//        OutputStream wr = conn.getOutputStream();
//        wr.write(param.getBytes("UTF-8"));
//        wr.flush();
//        wr.close();
//
//		
//		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//    
//        String line = null;
//        StringBuffer buff = new StringBuffer();
//        while ((line = in.readLine()) != null) {
//			buff.append(line + "\n");
//		}
//        String data = buff.toString().trim();
//		JSONParser jsonParser = new JSONParser();
//		JSONObject json = (JSONObject) jsonParser.parse(data);
//		//System.out.println(json);
//		//{"cardid":{"0":1,"1":2,"2":3,"3":4,"4":5},//1,2,3,4,5  card 디비 no 값
//		//"name":{"0":"탄탄대로 Miz&Mr 티타늄카드","1":"NH올원 파이카드\n","2":"Easy all 티타늄카드","3":"현대카드M Edition3","4":"굿데이올림카드"},
//		//"predict":{"0":0.0,"1":0.0,"2":0.0,"3":0.0,"4":0.0},
//		//"company":{"0":"KB국민카드","1":"NH농협카드","2":"KB국민카드","3":"현대카드","4":"KB국민카드\n\n"}}
//		JSONObject array = (JSONObject)json.get("cardid");
//		
//		cdService.recoDel(name,2);
//		
//		for(int i =0 ;i<=json.size();i++) {
//			long cardid = (long) array.get(i+"");
//			String cardNo = Long.toString(cardid);
//			
//			RecommendCard card = new RecommendCard();
//			card.setCardid(cardNo);
//			card.setRecommendType(2);
//			card.setUserid(uId);
//			cdService.recoCard(card);
//		}
//		result=null;
//   		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//   	}

	private ResponseEntity<Map<String, Object>> handleSuccess(Map<String, Object> data) {
		data.put("status", true);
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> handleException(Exception e) {
		logger.error("예외 발생 : ", e);
		System.out.println("예외 발생 : "+e);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", false);
		resultMap.put("data", e.getMessage());
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}