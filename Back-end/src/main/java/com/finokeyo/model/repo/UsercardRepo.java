package com.finokeyo.model.repo;

import com.finokeyo.model.dto.Usercard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsercardRepo extends JpaRepository<Usercard, String> {

}
