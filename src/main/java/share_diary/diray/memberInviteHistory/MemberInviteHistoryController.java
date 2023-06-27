package share_diary.diray.memberInviteHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.common.email.EmailSenderComponent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/member-invite-histories")
@Slf4j
public class MemberInviteHistoryController {

    private final EmailSenderComponent emailSender;
    /**
     * 초대 관련 기획
     * - 초대는 방장만 가능
     * - 재초대 횟수 제한 없음 (상대 거절 여부 무관)
     * - 수락, 거절 이력은 계속 보임
     * */
// 매일 발송 테스트 위해서 임시 작성
//    @GetMapping
//    @NoAuth
//    public void test(String sendTo) {
//        emailSender.sendTest(sendTo);
//    }
}
