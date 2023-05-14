package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.common.dto.EmptyJsonDTO;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @NoAuth
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public EmptyJsonDTO signUp(@RequestBody MemberSignUpRequestDTO signUpRequestDTO){
        log.info(signUpRequestDTO.toString());
        memberService.joinMember(signUpRequestDTO);
        return new EmptyJsonDTO();
    }

}
