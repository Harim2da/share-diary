package share_diary.diray.memberInviteHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import share_diary.diray.auth.domain.NoAuth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/member-invite-histories")
@Slf4j
public class MemberInviteHistoryController {

    private final MemberInviteHistoryService memberInviteHistoryService;

    /**
     * 초대 관련 기획
     * - 초대는 방장만 가능
     * - 재초대 횟수 제한 없음 (상대 거절 여부 무관)
     * - 수락, 거절 이력은 계속 보임
     * */
    @PostMapping
    @NoAuth // 수정 예정
    public ResponseEntity<HttpStatus> inviteRoomMembers(
            @RequestBody MemberInviteRequest request
    ) {
        //TODO [하림] : 초대하는 사람이 방장인지 체크하는 로직 필요 (프론트와 논의), 보낸 사람 이름도 뽑아낼 방법 확인 (논의)
        memberInviteHistoryService.inviteRoomMembers(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}