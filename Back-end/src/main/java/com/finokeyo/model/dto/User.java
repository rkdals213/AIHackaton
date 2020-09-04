package com.finokeyo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Setter
@Getter
public class User {
    @Id
    private String id;
    private String password;
    private String name;
    private int age;
    private int sex;
    private String circlename;
    private int circlemember;
    private int circlekind;
    private boolean checkedin;
    private int keyid;
}
