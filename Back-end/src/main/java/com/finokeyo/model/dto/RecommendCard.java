package com.finokeyo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="table_namerecommendation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int no;
    String userid;
    int recommendType;
    String cardid;
}
