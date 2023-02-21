package ru.netology.hibernatedao.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.netology.hibernatedao.entity.Person;
import ru.netology.hibernatedao.entity.PersonKey;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, PersonKey> {
    @Query("select p from Person p where p.cityOfLiving = :city")
    List<Person> findByCityOfLiving(@Param("city") String city);

    @Query("select p from Person p where p.personKey.age < :age")
    List<Person> findByPersonKeyAgeLessThan(@Param("age") int age, Sort sort);

    @Query("select p from Person p where p.personKey.name = :name and p.personKey.surname = :surname")
    List<Person> findByPersonKeyNameAndPersonKeySurname(@Param("name") String name, @Param("surname") String surname);

    @Query("select p from Person p where p.personKey.name = :name and p.personKey.surname = :surname")
    Optional<Person> findFirstByPersonKeyNameAndPersonKeySurname(@Param("name") String name, @Param("surname") String surname);

}
