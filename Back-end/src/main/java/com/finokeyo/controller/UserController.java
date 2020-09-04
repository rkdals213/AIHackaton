package com.finokeyo.controller;



import com.finokeyo.model.dto.RecommendCard;
import com.finokeyo.model.dto.User;
import com.finokeyo.model.dto.Usercard;
import com.finokeyo.model.service.UsercardService;
import com.google.gson.JsonObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finokeyo.model.service.RecoCardService;
import com.finokeyo.model.service.UserService;
import com.finokeyo.model.dto.User;

import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/v1/user")
public class UserController {

	static Logger logger = LoggerFactory.getLogger(RestController.class);

	@Autowired
	UserService uService;

	@Autowired
	UsercardService ucService;
	
	@Autowired
	RecoCardService cdService;

	@PostMapping("/login")
	@ApiOperation(value = "login")
	public ResponseEntity<Map<String, Object>> login(HttpServletResponse res, HttpServletRequest req, @RequestBody User input){
		logger.debug("login");
		System.out.println("login");
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> result = new HashMap<>();
		try {
			User user = uService.findByIdAndPassword(input.getId(), input.getPassword());
			result.put("id", user.getId());
			result.put("name", user.getName());
			entity = handleSuccess(result);
		} catch (RuntimeException e){
			entity = handleException(e);
		}
		return entity;
	}

	@PostMapping("/regist")
	@ApiOperation(value = "regist")
	public ResponseEntity<Map<String, Object>> regist(HttpServletResponse res, HttpServletRequest req, @RequestBody User input){
		logger.debug("regist");
		System.out.println("regist");
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> result = new HashMap<>();
		try {
			User user = uService.regist(input);
			result.put("id", user.getId());
			result.put("name", user.getName());
			entity = handleSuccess(result);
		} catch (RuntimeException e){
			entity = handleException(e);
		}
		return entity;
	}

	@PostMapping("/registCard") //ai접근
	@ApiOperation(value = "registCard")
	public ResponseEntity<Map<String, Object>> registCard(HttpServletResponse res, HttpServletRequest req, 
			@RequestBody Usercard [] usercard, @RequestHeader(value="uId") String uId)  throws IOException, ParseException  {
		logger.debug("registCard");
		System.out.println("registCard");
		System.out.println(uId);
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> result = new HashMap<>();
		
		try {
			for (Usercard uc: usercard) {
				ucService.regist(uc);
			}
			User user = uService.getUserInfo(uId);
			
			BufferedReader in = null;
			URL obj = new URL("http://127.0.0.1:5000/users/"+user.getKeyid()); // 호출할 url
	        HttpURLConnection con = (HttpURLConnection)obj.openConnection();

	        con.setRequestMethod("GET");
	        in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	    
	        String line = null;
	        StringBuffer buff = new StringBuffer();
	        while ((line = in.readLine()) != null) {
				buff.append(line + "\n");
			}
	        String data = buff.toString().trim();
			JSONParser jsonParser = new JSONParser();
			JSONObject json = (JSONObject) jsonParser.parse(data);
			//System.out.println(json);
			//{"cardid":{"0":1,"1":2,"2":3,"3":4,"4":5},//1,2,3,4,5  card 디비 no 값
			//"name":{"0":"탄탄대로 Miz&Mr 티타늄카드","1":"NH올원 파이카드\n","2":"Easy all 티타늄카드","3":"현대카드M Edition3","4":"굿데이올림카드"},
			//"predict":{"0":0.0,"1":0.0,"2":0.0,"3":0.0,"4":0.0},
			//"company":{"0":"KB국민카드","1":"NH농협카드","2":"KB국민카드","3":"현대카드","4":"KB국민카드\n\n"}}
			JSONObject arr = (JSONObject)json.get("cardid");
			
			cdService.recoDel(uId,1);
			
			for(int i =0 ;i<arr.size();i++) {
				long cardid = (long) arr.get(i+"");
				String cardNo = Long.toString(cardid);
				
				RecommendCard card = new RecommendCard();
				card.setCardid(cardNo);
				card.setRecommendType(1);
				card.setUserid(uId);
//				System.out.println(card);
				cdService.recoCard(card);
			}
			entity = handleSuccess(result);
		} catch (RuntimeException e){
			entity = handleException(e);
		}
		return entity;
	}
	
	
	@DeleteMapping("/delete")
	@ApiOperation(value = "delete")
	public ResponseEntity<Map<String, Object>> delete(HttpServletResponse res, HttpServletRequest req, @RequestBody User input){
		logger.debug("delete");
		System.out.println("delete");
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> result = new HashMap<>();
		try {
			uService.delete(input.getId());
			result.put("id", input.getId());
			result.put("name", input.getName());
			entity = handleSuccess(result);
		} catch (RuntimeException e){
			entity = handleException(e);
		}
		return entity;
	}

	@PostMapping("/getUserInfo")
	@ApiOperation(value = "getUserInfo")
	public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletResponse res, HttpServletRequest req, @RequestBody String id){
		logger.debug("getUserInfo");
		System.out.println("getUserInfo");
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> result = new HashMap<>();
		try {
			User user = uService.getUserInfo(id);
			user.setPassword("");
			result.put("user", user);
			entity = handleSuccess(result);
		} catch (RuntimeException e){
			entity = handleException(e);
		}
		return entity;
	}

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