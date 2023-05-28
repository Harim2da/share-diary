package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.common.dto.EmptyJsonDTO;
import share_diary.diray.exception.member.PasswordNotCoincide;
import share_diary.diray.exception.member.UpdatePasswordNotCoincide;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.dto.request.MemberPasswordRequestDTO;
import share_diary.diray.member.dto.request.MemberSignUpRequestDTO;
import share_diary.diray.member.dto.request.MemberUpdateRequestDTO;
import share_diary.diray.member.dto.response.MemberResponseDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @NoAuth
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public EmptyJsonDTO signUp(@RequestBody MemberSignUpRequestDTO signUpRequestDTO){
        log.info("signUpRequestDTO = {}",signUpRequestDTO.toString());
        memberService.joinMember(signUpRequestDTO);
        return new EmptyJsonDTO();
    }

    /**
     * 아이디 찾기
     */
//    @NoAuth
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me/id")
    public MemberResponseDTO findMemberId(@RequestParam("email") String email){
        log.info("email = {}",email);
        MemberResponseDTO memberResponseDTO = memberService.findMemberByEmail(email);
        return memberResponseDTO;
    }

    /**
     * 회원 수정 전 비밀번호 확인
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/me/pwd")
    public EmptyJsonDTO passwordCheck(@RequestBody MemberPasswordRequestDTO memberPasswordRequestDTO){
        log.info("memberPasswordRequestDTO={}",memberPasswordRequestDTO.toString());
        memberService.passwordCheck(memberPasswordRequestDTO);
        return new EmptyJsonDTO();
    }

    /**
     * 회원 수정
     */
//    @NoAuth
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/me")
    public MemberResponseDTO updateMember(@RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO){
        log.info("memberUpdateDTO={}",memberUpdateRequestDTO.toString());
        if(!memberUpdateRequestDTO.validationPassword()){
            throw new UpdatePasswordNotCoincide();
        }
        return memberService.updateMember(memberUpdateRequestDTO);
    }

    //TODO: 비밀번호 재설정 -> 등록된 email 로 인증번호 발송 -> 해당 인증번호를 입력 -> 비밀번호 입력

}