package com.finokeyo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Usercard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String userid;
    int cardid;
    String cardname;
    int rating;
}
