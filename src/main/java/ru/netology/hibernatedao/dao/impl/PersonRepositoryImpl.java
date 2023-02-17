package ru.netology.hibernatedao.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.netology.hibernatedao.dao.PersonRepository;
import ru.netology.hibernatedao.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Person> getPersonsByCity(String city) {
        String query = "select p from Person p where lower(p.cityOfLiving)=lower(:city)";
        return entityManager.createQuery(query, Person.class)
                .setParameter("city", city)
                .getResultList();
    }

}
