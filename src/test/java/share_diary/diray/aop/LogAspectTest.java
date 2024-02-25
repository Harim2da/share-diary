package share_diary.diray.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import share_diary.diray.member.MemberController;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(LogAspect.class)
@SpringBootTest
class LogAspectTest {

    @Autowired
    MemberController memberController;

    @Test
    @DisplayName("Controller request test")
    void controllerAopTest(){
        MemberSignUpRequestDTO request = MemberSignUpRequestDTO.of("jipdol2", "jipdol2@gmail.com", "1234", "jipdol2");
        memberController.signUp(request);
    }

}