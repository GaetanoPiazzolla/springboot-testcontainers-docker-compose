package gae.piaz.tc.controller;

import gae.piaz.tc.domain.Person;
import gae.piaz.tc.domain.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonCrudRepoIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected PersonRepository personRepository;

    @Test
    public void savePerson() throws Exception {
        Person person = Person.builder()
                .name("John Doe")
                .email("john.doe@gmail.com")
                .age(25).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person))
        ).andExpect(status().isCreated());

        // check data in repository
        Optional<Person> personInRepo = personRepository.findByEmail(person.getEmail());
        Assertions.assertTrue(personInRepo.isPresent());
        Assertions.assertEquals(person.getName(), personInRepo.get().getName());
        Assertions.assertEquals(person.getEmail(), personInRepo.get().getEmail());
        Assertions.assertEquals(person.getAge(), personInRepo.get().getAge());
    }
}
