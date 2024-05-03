package gae.piaz.tc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gae.piaz.tc.SpringBootApp;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootApp.class)
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    static {
        File dockerComposeFile = new File("src/test/docker-compose.yml");

        if (dockerComposeFile.exists()) {
            logger.info("Docker-compose file found in {}", dockerComposeFile.getAbsolutePath());
            logger.info("Starting docker-compose container. This should happen only once before all tests.");

            DockerComposeContainer<?> container = new DockerComposeContainer<>(dockerComposeFile)
                    .withExposedService("db-service-test", 5432)
                    .waitingFor(
                            "liquibase-test",
                            Wait.forLogMessage(
                                            ".*Liquibase command 'update' was executed successfully.*\\n",
                                            1)
                                    .withStartupTimeout(java.time.Duration.ofMinutes(1)))
                    .withRemoveImages(DockerComposeContainer.RemoveImages.LOCAL);

            logger.info("Starting docker-compose container.");
            container.start();

            logger.info("Docker-compose container started.");

        } else {
            logger.warn("Docker-compose file not found");
        }
    }

}
