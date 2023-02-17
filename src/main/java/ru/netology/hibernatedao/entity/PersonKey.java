package ru.netology.hibernatedao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class PersonKey implements Serializable {

    private String name;

    private String surname;

    private int age;

}
