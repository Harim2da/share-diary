package share_diary.diray.apiTest;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import share_diary.diray.DatabaseCleanup;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        if(RestAssured.port == RestAssured.UNDEFINED_PORT){
            RestAssured.port = port;
            databaseCleanup.afterPropertiesSet();
        }
//        databaseCleanup.execute();
    }
    @AfterEach
    void cleanUp(){
        databaseCleanup.execute();
    }
}
