package com.finokeyo.model.service;

import com.finokeyo.model.dto.Usercard;
import com.finokeyo.model.repo.UsercardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UsercardService {
    @Autowired
    UsercardRepo repo;

    public Usercard regist(Usercard usercard){
        return repo.save(usercard);
    }
}
