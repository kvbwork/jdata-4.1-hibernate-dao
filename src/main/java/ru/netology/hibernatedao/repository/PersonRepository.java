package ru.netology.hibernatedao.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.hibernatedao.entity.Person;
import ru.netology.hibernatedao.entity.PersonKey;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, PersonKey> {
    List<Person> findByCityOfLiving(String city);

    List<Person> findByPersonKeyAgeLessThan(int age, Sort sort);

    List<Person> findByPersonKeyNameAndPersonKeySurname(String name, String surname);

    Optional<Person> findFirstByPersonKeyNameAndPersonKeySurname(String name, String surname);

}
