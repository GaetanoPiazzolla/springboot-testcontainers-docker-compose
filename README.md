## TestContainers with DockerCompose in SpringBoot 3
It's possible to run the tests by executing the following command:
```shell
./gradlew clean test
```
Otherwise, you can run the tests from your IDE.

The central part of tests is in the `AbstractIntegrationTest` class.
Other details can be found in the following article: [https://gaetanopiazzolla.github.io/java/docker/springboot/2024/05/16/compose-testcont.html](
https://gaetanopiazzolla.github.io/java/docker/springboot/2024/05/16/compose-testcont.html).