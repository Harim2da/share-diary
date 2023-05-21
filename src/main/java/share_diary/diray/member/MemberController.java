package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.common.dto.EmptyJsonDTO;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;
import share_diary.diray.member.dto.response.MemberResponseDTO;

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
        log.info("signUpRequestDTO = {}",signUpRequestDTO.toString());
        memberService.joinMember(signUpRequestDTO);
        return new EmptyJsonDTO();
    }

    @NoAuth
    @GetMapping("/me/id")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponseDTO findMemberId(@RequestParam("email") String email){
        log.info("email = {}",email);
        MemberResponseDTO memberResponseDTO = memberService.findMemberByEmail(email);
        return memberResponseDTO;
    }

    //TODO: 비밀번호 재설정 -> 등록된 email 로 인증번호 발송 -> 해당 인증번호를 입력 -> 비밀번호 입력
}