package com.finokeyo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    int no;
    String name;
    String company;
    String benefits;
    String images;
}
