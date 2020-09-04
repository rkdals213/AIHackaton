package com.finokeyo.model.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class TokenStore {
    static String access_token;
    static String refresh_token;
    static String seq_token;
    static String use_token;

    public void storeToken(String access_token, String refresh_token, String seq_token, String use_token){
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.seq_token = seq_token;
        this.use_token = use_token;
    }

    public Map<String, String> getToken(){
        Map<String, String> map = new HashMap<>();
        map.put("access-token", this.access_token);
        map.put("refresh-token", this.refresh_token);
        map.put("seq-token", this.seq_token);
        map.put("use-token", this.use_token);
        return map;
    }
}
