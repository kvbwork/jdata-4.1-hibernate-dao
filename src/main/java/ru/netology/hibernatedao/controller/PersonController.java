package ru.netology.hibernatedao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.hibernatedao.dao.PersonRepository;
import ru.netology.hibernatedao.entity.Person;

import java.util.List;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping("/by-city")
    public List<Person> getPersonByCity(@RequestParam String city) {
        return personRepository.getPersonsByCity(city);
    }

}
