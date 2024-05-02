package gae.piaz.tc.config;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

public class TestContainerConfig {

    public static DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("src/test/docker-compose.yml"))
                    .withExposedService("db-service-test", 5432)
                    .waitingFor(
                            "liquibase-test",
                            Wait.forLogMessage(
                                            ".*Liquibase command 'update' was executed successfully.*\\n",
                                            1)
                                    .withStartupTimeout(java.time.Duration.ofMinutes(1)))
                    .withRemoveImages(DockerComposeContainer.RemoveImages.LOCAL);
}
