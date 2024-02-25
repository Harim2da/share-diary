package share_diary.diray.apiTest;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@Configuration
public class RedisTestContainers {

    private static final String REDIS_DOCKER_IMAGE = "redis:5.0.3-alpine";
    private static final int REDIS_PORT = 6379;

    static {    // (1)
        GenericContainer<?> REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                        .withExposedPorts(REDIS_PORT)
                        .withReuse(true);

        REDIS_CONTAINER.start();    // (2)

        // (3)
        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString());
    }
}
