package share_diary.diray.memberInviteHistory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.MemberInviteRequest;
import share_diary.diray.memberInviteHistory.controller.request.InviteUpdateRequest;
import share_diary.diray.memberInviteHistory.dto.MemberInviteHistoryDTO;
import share_diary.diray.common.response.ResultList;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/member-invite-histories")
@Slf4j
@Tag(name = "MemberInviteHistory",description = "MemberInviteHistory API")
public class MemberInviteHistoryController {

    private final MemberInviteHistoryService memberInviteHistoryService;

    /**
     * 초대 관련 기획
     * - 초대는 방장만 가능
     * - 재초대 횟수 제한 없음 (상대 거절 여부 무관)
     * - 수락, 거절 이력은 계속 보임
     * @author harim
     * */
    @PostMapping
    public ResponseEntity<HttpStatus> inviteRoomMembers(
            @RequestBody MemberInviteRequest request,
            @AuthenticationPrincipal LoginSession session
    ) {
        // 초대하는 사람 본인 Id 추가
        request.updateHostId(session.getId());
        memberInviteHistoryService.inviteRoomMembers(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 일기방 초대 수락 api
     * @author harim
     */
    @PatchMapping("/{historyId}")
    @NoAuth
    public ResponseEntity<HttpStatus> updateInviteHistory(
            @PathVariable Long historyId,
            @RequestBody InviteUpdateRequest request
            // 계정 토큰값 활용
    ) {
        memberInviteHistoryService.updateInviteHistory(historyId, request.getStatus());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 23/09/17
     * 알림 내역 조회 api
     * @author jipdol2
     */
    @Operation(summary = "Get Invite History",description = "알림 내역 조회 API")
    @GetMapping
    @NoAuth
    //TODO[jipdol2] : MemberInviteHistory Entity 에 '누가' 초대를 했는지 알 수 있도록 필드 추가 필요...
    public ResponseEntity<ResultList<MemberInviteHistoryDTO>> findByInviteHistory(@AuthenticationPrincipal LoginSession loginSession){
        List<MemberInviteHistoryDTO> byLoginUserInviteHistory = memberInviteHistoryService.findByLoginUserInviteHistory(loginSession.getId());
        return ResponseEntity.ok(new ResultList<>(byLoginUserInviteHistory));
    }

}
