package gae.piaz.tc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gae.piaz.tc.SpringBootApp;
import gae.piaz.tc.config.TestContainerConfig;
import gae.piaz.tc.domain.Person;
import gae.piaz.tc.domain.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootApp.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonCrudRepoIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PersonRepository personRepository;

    static {
        System.out.println("Starting docker-compose container. This should happen only once before all tests.");
        TestContainerConfig.dockerComposeContainer.start();
        System.out.println("Done - Starting docker-compose container.");
    }

    @Test
    public void savePerson() throws Exception {
        Person person = Person.builder()
                .name("John Doe")
                .email("john.doe@gmail.com")
                .age(25).build();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/repository/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person))
        ).andExpect(status().isCreated()).andReturn();

        // check data returned
        Person personReturned = objectMapper.readValue(result.getResponse().getContentAsString(), Person.class);
        Assertions.assertNotNull(personReturned);
        Assertions.assertNotNull(personReturned.getId());
        Assertions.assertEquals(person.getName(), personReturned.getName());
        Assertions.assertEquals(person.getEmail(), personReturned.getEmail());
        Assertions.assertEquals(person.getAge(), personReturned.getAge());

        // check data in repository
        Optional<Person> personInRepo = personRepository.findById(personReturned.getId());
        Assertions.assertTrue(personInRepo.isPresent());
        Assertions.assertEquals(person.getName(), personInRepo.get().getName());
        Assertions.assertEquals(person.getEmail(), personInRepo.get().getEmail());
        Assertions.assertEquals(person.getAge(), personInRepo.get().getAge());
    }
}
