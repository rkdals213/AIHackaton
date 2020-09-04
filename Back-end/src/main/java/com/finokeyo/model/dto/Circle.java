package com.finokeyo.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class Circle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String kindname;
}
