package com.finokeyo;

import com.finokeyo.model.dto.User;
import com.finokeyo.model.repo.UserRepo;
import com.finokeyo.model.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinokeyoApplicationTests {

    @Autowired
    UserRepo userService;

    @Test
    void contextLoads() {
        User user = new User();
//        user.setId("asdasd");
//        user.setPassword("asdasd");
//        user.setCheck(false);
//        user.setName("asdasd");
//        user.setCirclekind(1);
//        user.setCirclemember(11);
//        user.setCirclename("Asdasd");
        userService.save(user);
    }

}
