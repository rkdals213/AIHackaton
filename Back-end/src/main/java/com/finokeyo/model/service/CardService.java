package com.finokeyo.model.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finokeyo.model.dto.Card;
import com.finokeyo.model.dto.User;
import com.finokeyo.model.repo.CardRepo;

import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class CardService {
    @Autowired
    CardRepo repo;

    public List<Card> findByCompany(String card){
        return repo.findByCompany(card);
    }
    public List<Card> findAll(){
        return repo.findAll();
    }
    public Card findByNo(int no){
        return repo.findByNo(no).get();
    }
    
}
