package ru.netology.hibernatedao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PERSONS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {

    @EmbeddedId
    private PersonKey personKey;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "city_of_living")
    private String cityOfLiving;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return personKey.equals(person.personKey);
    }

    @Override
    public int hashCode() {
        return personKey.hashCode();
    }
}
