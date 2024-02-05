package share_diary.diray.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member",description = "Member API")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * @author jipdol2
     */
    @Operation(summary = "SignUp Member",description = "회원가입 API")
    @NoAuth
    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody @Valid MemberSignUpRequestDTO requestDTO){
        LocalDateTime now = LocalDateTime.now();
        memberService.joinMember(requestDTO,now);
        return ResponseEntity.ok().build();
    }

    /**
     * 소셜 로그인 시도 - 회원가입
     * @author jipdol2
     */
    @Operation(summary = "Social SignUp Member",description = "(소셜)회원가입 API")
    @NoAuth
    @PostMapping("/signUp/social")
    public ResponseEntity<Void> signUpSocial(@RequestBody MemberSignUpSocialRequestDTO requestDTO){
        memberService.joinMemberSocial(requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 아이디 찾기
     * @author jipdol2
     */
    @Operation(summary = "Find Member",description = "아이디 찾기 API")
    @NoAuth
    @GetMapping("/me/id")
    public MemberDTO findMemberId(@RequestParam("email") String email){
        return memberService.findMemberByEmail(email);
    }

    /**
     * 비밀번호 확인
     * @author jipdol2
     */
    @Operation(summary = "Check Member Password",description = "비밀번호 확인 API")
    @PostMapping("/me/pwd")
    public ResponseEntity<Void> passwordCheck(@AuthenticationPrincipal LoginSession session, @RequestBody MemberPasswordRequestDTO requestDTO){
        memberService.passwordCheck(session.getId(),requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 변경
     * @author jipdol2
     */
    @Operation(summary = "Change Member Password",description = "비밀번호 변경 API")
    @PostMapping("/pwd")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal LoginSession session, @RequestBody MemberPasswordUpdateDTO requestDTO){
        memberService.updatePassword(session.getId(),requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 수정
     * @author jipdol2
     */
    @Operation(summary = "Update Member", description = "회원 수정 API")
    @PatchMapping("/me")
    public MemberDTO updateMember(@AuthenticationPrincipal LoginSession session, @RequestBody MemberUpdateRequestDTO requestDTO){
        return memberService.updateMember(requestDTO);
    }

    /**
     * 아이디 중복 체크
     * @author jipdol2
     */
    @Operation(summary = "Validation Check LoginId",description = "아이디 중복 체크 API")
    @NoAuth
    @PostMapping("/loginId/validation")
    public ResponseEntity<Boolean> validationLoginId(@RequestBody @Valid MemberLoginIdRequestDTO requestDTO){
        return ResponseEntity.ok(memberService.validationMemberLoginId(requestDTO));
    }

    /**
     * 이메일 중복 체크
     * @author jipdol2
     */
    @Operation(summary = "Validation Check Email",description = "이메일 중복 체크 API")
    @NoAuth
    @PostMapping("/email/validation")
    public ResponseEntity<Boolean> validationEmail(@RequestBody @Valid MemberEmailRequestDTO requestDTO){
        return ResponseEntity.ok(memberService.validationMemberEmail(requestDTO));
    }

    /**
     * [비밀번호 재설정]
     * 1. 등록된 email 로 인증번호 발송
     * 2. 입력된 인증번호 유효성 검증
     * 3. 비밀번호 변경
     */

    /**
     * 비밀번호 초기화 : 인증번호 email 로 전송
     * @author jipdol2
     */
    @NoAuth
    @PostMapping("/certification-number")
    public ResponseEntity<Void> sendToCertificationNumber(@RequestBody MemberIdAndEmailRequestDTO requestDTO){
        memberService.sendCertificationNumber(requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 초기화 : 입력된 인증번호 유효성 검증
     * @author jipdol2
     */
    @NoAuth
    @PostMapping("/validation-certification-number")
    public ResponseEntity<Void> validationCertificationNumber(@RequestBody MemberCertificationNumber requestDTO){
        memberService.validationCertificationNumber(requestDTO.getCertificationNumber());
        return ResponseEntity.ok().build();
    }

    @NoAuth
    @PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(
            @RequestBody MemberPasswordRequestDTO requestDTO
    ){
        memberService.resetPassword(requestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 일기방 만들기 전, 해당 계정이 신규 일기방을 만들 수 있는지
     * 체크 API
     * @author harim
     * */
    @Operation(summary = "Validation Check Create DiaryRoom",description = "일기방 생성 가능 여부 확인 API")
    @GetMapping("/diary-room/validation")
    public ResponseEntity<Boolean> validateCreateDiaryRoom(
            @AuthenticationPrincipal LoginSession auth
    ) {
        return ResponseEntity.ok(memberService.validateCreateDiaryRoom(auth.getId()));
    }

    /**
     * 멤버 초대 uuid 유효성 체크 API
     * @author harim
     * */
    @Operation(summary = "UUID Check",description = "uuid 유효성 체크 API")
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
     * @author jipdol2
     */
    @Operation(summary = "Get MyPage",description = "마이페이지 조회 API")
    @GetMapping("/myPage")
    public MemberDTO findByMyInfo(
            @AuthenticationPrincipal LoginSession session
    ){
        return memberService.findMemberById(session.getId());
    }

}
