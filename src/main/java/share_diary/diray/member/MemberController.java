package share_diary.diray.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.exception.member.UpdatePasswordNotCoincide;
import share_diary.diray.member.dto.MemberDTO;
import share_diary.diray.member.dto.request.*;
import share_diary.diray.member.dto.response.MemberMyPageDTO;
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
    public ResponseEntity<Void> signUp(@RequestBody @Valid MemberSignUpRequestDTO requestDTO){
        memberService.joinMember(requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 소셜 로그인 시도 - 회원가입
     */
    @NoAuth
    @PostMapping("/signUp/social")
    public ResponseEntity<Void> signUpSocial(@RequestBody MemberSignUpSocialRequestDTO requestDTO){
        memberService.joinMemberSocial(requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 아이디 찾기
     */
    @NoAuth
    @GetMapping("/me/id")
    public MemberResponseDTO findMemberId(@RequestParam("email") String email){
        MemberResponseDTO memberResponseDTO = memberService.findMemberByEmail(email);
        return memberResponseDTO;
    }

    /**
     * 비밀번호 확인
     */
    @PostMapping("/me/pwd")
    public ResponseEntity<Void> passwordCheck(@AuthenticationPrincipal LoginSession session, @RequestBody MemberPasswordRequestDTO requestDTO){
        memberService.passwordCheck(session.getId(),requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping("/pwd")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal LoginSession session, @RequestBody MemberPasswordUpdateDTO requestDTO){
        memberService.updatePassword(session.getId(),requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 수정
     */
//    @NoAuth
    @PatchMapping("/me")
    public MemberResponseDTO updateMember(@AuthenticationPrincipal LoginSession session, @RequestBody MemberUpdateRequestDTO requestDTO){
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
        return new MemberValidationLoginIdResponseDTO(memberService.validationMemberLoginId(requestDTO));
    }

    /**
     * 이메일 중복 체크
     */
    @PostMapping("/email/validation")
    public MemberValidationEmailResponseDTO validationEmail(@RequestBody @Valid MemberEmailRequestDTO requestDTO){
        return new MemberValidationEmailResponseDTO(memberService.validationMemberEmail(requestDTO));
    }

    /**
     * [비밀번호 재설정]
     * 1. 등록된 email 로 인증번호 발송
     * 2. 입력된 인증번호 유효성 검증
     * 3. 비밀번호 변경
     */

    /**
     * 비밀번호 초기화 : 인증번호 email 로 전송
     */
    @PostMapping("/certification-number")
    public ResponseEntity<Void> sendToCertificationNumber(@AuthenticationPrincipal LoginSession session){
        memberService.sendCertificationNumber(session);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 초기화 : 입력된 인증번호 유효성 검증
     */
    @PostMapping("/validation-certification-number")
    public ResponseEntity<Void> validationCertificationNumber(@RequestBody MemberCertificationNumber requestDTO){
        memberService.validationCertificationNumber(requestDTO.getCertificationNumber());
        return ResponseEntity.ok().build();
    }

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

    /**
     * 멤버 초대 uuid 유효성 체크 API
     * */
    @GetMapping("/uuid/{uuid}")
    @NoAuth
    public ResponseEntity<MemberDTO> validateMember(
            @PathVariable String uuid
    ) {
        return ResponseEntity.ok(memberService.validateMember(uuid));
    }

    /**
     * 마이페이지 조회 API
     * - 이메일, 닉네임
     * - 추후 : 나의 랭킹, 그 동안 쓴 일기, 메달 획득 개수
     */
    @GetMapping("/myPage")
    public MemberMyPageDTO findByMyInfo(
            @AuthenticationPrincipal LoginSession session
    ){
        return memberService.findMemberById(session.getId());
    }

}
