package com.finokeyo.model.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finokeyo.model.dto.Card;
import com.finokeyo.model.dto.RecommendCard;
import com.finokeyo.model.dto.User;
import com.finokeyo.model.repo.CardRepo;
import com.finokeyo.model.repo.RecoCardRepo;

import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class RecoCardService {
    @Autowired
    RecoCardRepo repo;

    public RecommendCard recoCard(RecommendCard card){
        return repo.save(card);
    }
    public int recoDel(String id, int type){
        return repo.deleteByUseridAndRecommendType(id, type);
    }
    public List<RecommendCard> recoFind(String id){
        return repo.findByUserid(id);
    }
}
