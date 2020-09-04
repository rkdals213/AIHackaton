package com.finokeyo.model.service;

import com.finokeyo.model.dto.User;
import com.finokeyo.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepo repo;

    public User findByIdAndPassword(String id, String password){
        User u = repo.findUserByIdAndPassword(id, password);
//        u.setCheckedin(true);
        return u;
    }
    public User getUserInfo(String id){
        return repo.findById(id).get();
    }

    public User regist(User user){
        return repo.save(user);
    }

    public void delete(String id){
        repo.deleteById(id);
    }
    public User checkUserKftc(String id){
        return repo.findUserById(id).get();
    }
    public User CheckUpdate(User user){
        return repo.save(user);
    }
    
}
