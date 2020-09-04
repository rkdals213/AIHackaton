package com.finokeyo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finokeyo.model.dto.Card;
import com.finokeyo.model.dto.RecommendCard;
import com.finokeyo.model.service.CardService;
import com.finokeyo.model.service.RecoCardService;

import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/v1/card")
public class CardController {

	static Logger logger = LoggerFactory.getLogger(CardController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	CardService service;
	
	@Autowired
	RecoCardService rcService;
	

	
	@ApiOperation(value = "해당 카드 이미지 및 이름 리턴", response = String.class)
	@GetMapping("CardSearch")
	public ResponseEntity<List<Card>> emailcheck(@RequestParam("cardName") String cardName){
		logger.debug("해당 카드 호출 - 호출");
		System.out.println("해당 카드 호출 ");
		List<Card> card= null;
		try {
			card = service.findByCompany(cardName);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return new ResponseEntity<List<Card>>(card, HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "전체 카드 조회", response = String.class)
	@GetMapping("CardAll")
	public ResponseEntity<List<Card>> cardList(){
		logger.debug("카드 전체 호출 - 호출");
		System.out.println("카드 전체 호출");
		List<Card> card= null;
		try {
			card = service.findAll();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return new ResponseEntity<List<Card>>(card, HttpStatus.OK);
		
	}
	
	
	@ApiOperation(value = "추천 카드 조회", response = String.class)
	@GetMapping("RecoCard")
	public ResponseEntity<Map<String, Object>> recoCard(@RequestParam("userId") String userId){
		List<Card> a = null;
		logger.debug("카드 추천 호출 - 호출");
		System.out.println("카드 추천 호출");
		
		List<RecommendCard> card= null;
		
		ArrayList<Card> reCard= new ArrayList<Card>();
		ArrayList<Card> kftc = new ArrayList<Card>();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			card = rcService.recoFind(userId);
			for(int i =0; i<card.size(); i++) {
				Card c = new Card(); 
				if(card.get(i).getRecommendType()==1) {
					c = service.findByNo(Integer.parseInt(card.get(i).getCardid()));
					reCard.add(c);
				}else if(card.get(i).getRecommendType()==2) {
					c = service.findByNo(Integer.parseInt(card.get(i).getCardid()));
					kftc.add(c);
				}
			}
			result.put("type1", reCard);
			result.put("type2", kftc);
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		
	}

	
}