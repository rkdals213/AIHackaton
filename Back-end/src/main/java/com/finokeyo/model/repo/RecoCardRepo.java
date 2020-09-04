package com.finokeyo.model.repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finokeyo.model.dto.RecommendCard;




@Repository
public interface RecoCardRepo extends JpaRepository<RecommendCard, Integer> {
	public int deleteByUseridAndRecommendType(String id, int RecommendType);
	List<RecommendCard> findByUserid(String id);
}
