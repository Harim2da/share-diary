package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import java.util.Objects;

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

    /**
     * [비밀번호 재설정]
     * 1. 등록된 email 로 인증번호 발송
     * 2. 입력된 인증번호 유효성 검증
     * 3. 비밀번호 변경
     */

    /**
     * 비밀번호 변경 : 인증번호 email 로 전송
     */
    @PostMapping("/certification-number")
    public void sendToCertificationNumber(@AuthenticationPrincipal LoginSession session){
        memberService.sendCertificationNumber(session);
    }

    /**
     * 비밀번호 변경 : 입력된 인증번호 유효성 검증
     */
    @PostMapping("/validation-certification-number")
    public void validationCertificationNumber(@RequestBody MemberCertificationNumber requestDTO){
//        log.info("request={}", requestDTO.toString());
        memberService.validationCertificationNumber(requestDTO.getCertificationNumber());
    }

    /**
     * 비밀번호 초기화 : 임시비밀번호 발송
     */
    @PostMapping("/tempPassword")
    public void sendToTempPassword(@AuthenticationPrincipal LoginSession session){
        memberService.resetPasswordAndSendEmailToMember(session);
    }

    //TODO: 비밀번호 재설정 -> 등록된 email 로 인증번호 발송 -> 해당 인증번호를 입력 -> 비밀번호 입력

    /**
     * 일기방 만들기 전, 해당 계정이 신규 일기방을 만들 수 있는지
     * 체크 API
     * */
    @GetMapping("/diary-room/validation")
    public ResponseEntity<Boolean> validateCreateDiaryRoom(
            @AuthenticationPrincipal LoginSession auth
    ) {
        return ResponseEntity.ok(memberService.validateCreateDiaryRoom(auth.getId()));
    }
}