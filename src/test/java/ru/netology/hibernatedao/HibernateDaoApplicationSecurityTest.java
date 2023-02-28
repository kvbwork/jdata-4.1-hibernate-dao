package ru.netology.hibernatedao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.netology.hibernatedao.entity.Person;
import ru.netology.hibernatedao.entity.PersonKey;
import ru.netology.hibernatedao.repository.PersonRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:hibernatedao?MODE=PostgreSQL;",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.show-sql=true"
})
public class HibernateDaoApplicationSecurityTest {

    private static final String PERSONS_ROOT = "/persons";
    private static final Person TEST_PERSON = new Person(new PersonKey("Василий", "Зайцев", 55), "222222", "Tver");

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        if (mockMvc == null) {
            DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(SecurityMockMvcConfigurers.springSecurity());
            mockMvc = mockMvcBuilder.build();
        }
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void findAll_anonymous_success() throws Exception {
        String path = PERSONS_ROOT + "/all";
        mockMvc.perform(get(path))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[*].personKey").value(not(empty()))
                );
    }

    @Test
    void readPerson_anonymous_success() throws Exception {
        String path = PERSONS_ROOT + "/?name=Иван&surname=Иванов&age=27";
        mockMvc.perform(get(path))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.personKey.name").value(equalTo("Иван")),
                        jsonPath("$.personKey.surname").value(equalTo("Иванов")),
                        jsonPath("$.personKey.age").value(is(27)),
                        jsonPath("$.phoneNumber").value(equalTo("1111111")),
                        jsonPath("$.cityOfLiving").value(equalTo("Moscow"))
                );
    }

    @Test
    @WithMockUser
    void createOrUpdatePerson_with_User_success() throws Exception {
        String path = PERSONS_ROOT + "/";

        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_PERSON))
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.personKey.name").value(equalTo("Василий")),
                        jsonPath("$.personKey.surname").value(equalTo("Зайцев")),
                        jsonPath("$.personKey.age").value(is(55)),
                        jsonPath("$.phoneNumber").value(equalTo("222222")),
                        jsonPath("$.cityOfLiving").value(equalTo("Tver"))
                );

        var personEntity = personRepository.findById(TEST_PERSON.getPersonKey()).orElseThrow();
        assertThat(personEntity, equalTo(TEST_PERSON));

        personRepository.delete(TEST_PERSON);
    }

    @Test
    void createOrUpdatePerson_anonymous_failure() throws Exception {
        String path = PERSONS_ROOT + "/";
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_PERSON))
                )
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void deletePerson_with_User_success() throws Exception {
        personRepository.save(TEST_PERSON);
        String path = PERSONS_ROOT + "/?name=Василий&surname=Зайцев&age=55";

        mockMvc.perform(delete(path))
                .andExpectAll(
                        status().isNoContent()
                );

        boolean testRecordExist = personRepository.existsById(TEST_PERSON.getPersonKey());
        assertThat(testRecordExist, is(false));
    }

    @Test
    void deletePerson_anonymous_failure() throws Exception {
        personRepository.save(TEST_PERSON);
        String path = PERSONS_ROOT + "/?name=Василий&surname=Зайцев&age=55";

        mockMvc.perform(delete(path))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void getPersonByCity_with_User_success() throws Exception {
        String path = PERSONS_ROOT + "/by-city?city=Moscow";
        mockMvc.perform(get(path))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].personKey.name").value(equalTo("Иван")),
                        jsonPath("$.[0].personKey.surname").value(equalTo("Иванов")),
                        jsonPath("$.[0].personKey.age").value(is(27)),
                        jsonPath("$.[0].phoneNumber").value(equalTo("1111111")),
                        jsonPath("$.[0].cityOfLiving").value(equalTo("Moscow"))
                );
    }

    @Test
    void getPersonByCity_anonymous_failure() throws Exception {
        String path = PERSONS_ROOT + "/by-city?city=Moscow";
        mockMvc.perform(get(path))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void getPersonByAgeLessThan_with_User_success() throws Exception {
        String path = PERSONS_ROOT + "/by-age-lt?age=29";
        mockMvc.perform(get(path))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[*].personKey.name").value(hasItems("Иван", "Максим"))
                );
    }

    @Test
    void getPersonByAgeLessThan_anonymous_failure() throws Exception {
        String path = PERSONS_ROOT + "/by-age-lt?age=29";
        mockMvc.perform(get(path))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void getPersonsByNameAndSurname_with_User_success() throws Exception {
        String path = PERSONS_ROOT + "/by-name-surname?name=Иван&surname=Иванов";
        mockMvc.perform(get(path))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[*].personKey.name").value(hasItems("Иван"))
                );
    }

    @Test
    void getPersonsByNameAndSurname_anonymous_failure() throws Exception {
        String path = PERSONS_ROOT + "/by-name-surname?name=Иван&surname=Иванов";
        mockMvc.perform(get(path))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void getFirstPersonsByNameAndSurname_with_User_success() throws Exception {
        String path = PERSONS_ROOT + "/by-name-surname-first?name=Иван&surname=Иванов";
        mockMvc.perform(get(path))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.personKey.name").value("Иван")
                );
    }

    @Test
    void getFirstPersonsByNameAndSurname_anonymous_failure() throws Exception {
        String path = PERSONS_ROOT + "/by-name-surname-first?name=Иван&surname=Иванов";
        mockMvc.perform(get(path))
                .andExpect(status().isFound());
    }

}
