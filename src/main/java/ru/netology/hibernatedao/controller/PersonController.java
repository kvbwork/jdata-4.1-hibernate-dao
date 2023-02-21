package ru.netology.hibernatedao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.netology.hibernatedao.entity.Person;
import ru.netology.hibernatedao.entity.PersonKey;
import ru.netology.hibernatedao.exception.NotFoundException;
import ru.netology.hibernatedao.repository.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping("/all")
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @GetMapping("/")
    public Person readPerson(@RequestParam String name, @RequestParam String surname, @RequestParam short age) {
        return personRepository.findById(new PersonKey(name, surname, age))
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping("/")
    public Person createOrUpdatePerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@RequestParam String name, @RequestParam String surname, @RequestParam short age) {
        personRepository.deleteById(new PersonKey(name, surname, age));
    }

    @GetMapping("/by-city")
    public List<Person> findPersonsByCity(@RequestParam String city) {
        return personRepository.findByCityOfLiving(city);
    }

    @GetMapping("/by-age-lt")
    public List<Person> findPersonsByAgeLessThan(@RequestParam short age) {
        return personRepository.findByPersonKeyAgeLessThan(age, Sort.by("personKey.age").ascending());
    }

    @GetMapping("/by-name-surname")
    public List<Person> findPersonsByNameAndSurname(@RequestParam String name, @RequestParam String surname) {
        return personRepository.findByPersonKeyNameAndPersonKeySurname(name, surname);
    }

    @GetMapping("/by-name-surname-first")
    public Person findFirstPersonByNameAndSurname(@RequestParam String name, @RequestParam String surname) {
        return personRepository.findFirstByPersonKeyNameAndPersonKeySurname(name, surname)
                .orElseThrow(NotFoundException::new);
    }

}
