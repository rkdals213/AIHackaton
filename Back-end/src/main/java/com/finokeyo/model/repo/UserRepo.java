package com.finokeyo.model.repo;

import com.finokeyo.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findUserByIdAndPassword(String id, String password);
    Optional<User> findById(String id);
    void deleteById(String id);
    Optional<User> findUserById(String id);
}
