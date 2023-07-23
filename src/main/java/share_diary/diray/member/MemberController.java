package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.common.dto.EmptyJsonDTO;
import share_diary.diray.exception.member.UpdatePasswordNotCoincide;
import share_diary.diray.member.dto.request.*;
import share_diary.diray.member.dto.response.MemberResponseDTO;
import share_diary.diray.member.dto.response.MemberValidationEmailResponseDTO;
import share_diary.diray.member.dto.response.MemberValidationLoginIdResponseDTO;

import javax.validation.Valid;

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
    @PostMapping("/signUp")
    public EmptyJsonDTO signUp(@RequestBody @Valid MemberSignUpRequestDTO signUpRequestDTO){
        log.info("signUpRequestDTO = {}",signUpRequestDTO.toString());
        memberService.joinMember(signUpRequestDTO);
        return new EmptyJsonDTO();
    }

    /**
     * 아이디 찾기
     */
    @NoAuth
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
    public EmptyJsonDTO passwordCheck(@AuthenticationPrincipal LoginSession loginSession, @RequestBody MemberPasswordRequestDTO memberPasswordRequestDTO){
        log.info("memberPasswordRequestDTO={}",memberPasswordRequestDTO.toString());
        memberService.passwordCheck(loginSession,memberPasswordRequestDTO);
        return new EmptyJsonDTO();
    }

    /**
     * 회원 수정
     */
//    @NoAuth
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/me")
    public MemberResponseDTO updateMember(@AuthenticationPrincipal LoginSession loginSession, @RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO){
        log.info("memberUpdateDTO={}",memberUpdateRequestDTO.toString());
        if(!memberUpdateRequestDTO.validationPassword()){
            throw new UpdatePasswordNotCoincide();
        }
        return memberService.updateMember(memberUpdateRequestDTO);
    }

    /**
     * 아이디 중복 체크
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/loginId/validation")
    public MemberValidationLoginIdResponseDTO validationLoginId(@RequestBody @Valid MemberLoginIdRequestDTO loginIdDTO){
        log.info("memberEmail={}",loginIdDTO.toString());
        return new MemberValidationLoginIdResponseDTO(memberService.validationMemberLoginId(loginIdDTO));
    }

    /**
     * 이메일 중복 체크
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/email/validation")
    public MemberValidationEmailResponseDTO validationEmail(@RequestBody @Valid MemberEmailRequestDTO emailDTO){
        log.info("memberEmail={}",emailDTO.toString());
        return new MemberValidationEmailResponseDTO(memberService.validationMemberEmail(emailDTO));
    }

    //TODO: 비밀번호 재설정 -> 등록된 email 로 인증번호 발송 -> 해당 인증번호를 입력 -> 비밀번호 입력
}