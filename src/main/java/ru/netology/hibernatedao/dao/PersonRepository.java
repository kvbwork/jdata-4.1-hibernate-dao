package ru.netology.hibernatedao.dao;

import ru.netology.hibernatedao.entity.Person;

import java.util.List;

public interface PersonRepository {
    List<Person> getPersonsByCity(String city);

}
