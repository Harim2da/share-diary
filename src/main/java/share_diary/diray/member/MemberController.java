package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
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
    @PostMapping("/signUp")
    public void signUp(@RequestBody @Valid MemberSignUpRequestDTO requestDTO){
//        log.info("requestDTO={}",requestDTO.toString());
        memberService.joinMember(requestDTO);
    }

    /**
     * 소셜 로그인 시도 - 회원가입 - review
     */
    @NoAuth
    @PostMapping("/signUp/social")
    public void signUpSocial(@RequestBody MemberSignUpSocialRequestDTO requestDTO){
//        log.info("requestDTO={}",requestDTO.toString());
        memberService.joinMemberSocial(requestDTO);
    }

    /**
     * 아이디 찾기
     */
    @NoAuth
    @GetMapping("/me/id")
    public MemberResponseDTO findMemberId(@RequestParam("email") String email){
//        log.info("email={}",email);
        MemberResponseDTO memberResponseDTO = memberService.findMemberByEmail(email);
        return memberResponseDTO;
    }

    /**
     * 회원 수정 전 비밀번호 확인
     */
    @PostMapping("/me/pwd")
    public void passwordCheck(@AuthenticationPrincipal LoginSession loginSession, @RequestBody MemberPasswordRequestDTO requestDTO){
//        log.info("requestDTO={}",requestDTO.toString());
        memberService.passwordCheck(loginSession,requestDTO);
    }

    /**
     * 회원 수정
     */
//    @NoAuth
    @PatchMapping("/me")
    public MemberResponseDTO updateMember(@AuthenticationPrincipal LoginSession loginSession, @RequestBody MemberUpdateRequestDTO requestDTO){
//        log.info("requestDTO={}",requestDTO.toString());
        if(!requestDTO.validationPassword()){
            throw new UpdatePasswordNotCoincide();
        }
        return memberService.updateMember(requestDTO);
    }

    /**
     * 아이디 중복 체크
     */
    @NoAuth
    @PostMapping("/loginId/validation")
    public MemberValidationLoginIdResponseDTO validationLoginId(@RequestBody @Valid MemberLoginIdRequestDTO requestDTO){
//        log.info("requestDTO={}",requestDTO.toString());
        return new MemberValidationLoginIdResponseDTO(memberService.validationMemberLoginId(requestDTO));
    }

    /**
     * 이메일 중복 체크
     */
    @PostMapping("/email/validation")
    public MemberValidationEmailResponseDTO validationEmail(@RequestBody @Valid MemberEmailRequestDTO requestDTO){
//        log.info("requestDTO={}",requestDTO.toString());
        return new MemberValidationEmailResponseDTO(memberService.validationMemberEmail(requestDTO));
    }

    //TODO: 비밀번호 재설정 -> 등록된 email 로 인증번호 발송 -> 해당 인증번호를 입력 -> 비밀번호 입력
}