package com.finokeyo.model.repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finokeyo.model.dto.Card;



@Repository
public interface CardRepo extends JpaRepository<Card, Integer> {
	
	
	@Query(value="select * from card c where c.company like CONCAT('%',:card,'%')", nativeQuery = true)
	List<Card> findByCompany(String card);
	List<Card> findAll();
	Optional<Card> findByNo(int no);
	
}
