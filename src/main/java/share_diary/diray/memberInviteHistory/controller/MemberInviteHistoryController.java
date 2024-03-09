package share_diary.diray.memberInviteHistory.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.common.response.ApiResponse;
import share_diary.diray.memberInviteHistory.MemberInviteHistoryService;
import share_diary.diray.memberInviteHistory.controller.request.InviteUpdateRequestDTO;
import share_diary.diray.memberInviteHistory.controller.request.MemberInviteRequestDTO;
import share_diary.diray.memberInviteHistory.controller.response.MemberInviteHistoryDTO;

import java.time.LocalDateTime;
import java.util.List;

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
     * @author harim
     * */
    @PostMapping
    public ResponseEntity<Void> inviteRoomMembers(
            @RequestBody MemberInviteRequestDTO request,
            @AuthenticationPrincipal LoginSession session
    ) {
        LocalDateTime now = LocalDateTime.now();
        // 초대하는 사람 본인 Id 추가
        request.updateHostId(session.getId());
        memberInviteHistoryService.inviteRoomMembers(request,now);
        return ResponseEntity.ok().build();
    }

    /**
     * 일기방 초대 수락 api
     * @author harim
     */
    @PatchMapping("/{historyId}")
    @NoAuth
    public ResponseEntity<Void> updateInviteHistory(
            @PathVariable Long historyId,
            @RequestBody InviteUpdateRequestDTO request
            // 계정 토큰값 활용
    ) {
        memberInviteHistoryService.updateInviteHistory(historyId, request.getStatus());
        return ResponseEntity.ok().build();
    }

    /**
     * 23/09/17
     * 알림 내역 조회 api
     * @author jipdol2
     */
    @GetMapping
    @NoAuth
    public ApiResponse<List<MemberInviteHistoryDTO>> findByInviteHistory(
            @AuthenticationPrincipal LoginSession loginSession,
            @RequestParam(required = false) Long inviteHistoryId,
            @RequestParam Integer limit
    ){
        List<MemberInviteHistoryDTO> byLoginUserInviteHistory = memberInviteHistoryService.findByLoginUserInviteHistory(loginSession.getId(),inviteHistoryId,limit);
        return ApiResponse.ok(byLoginUserInviteHistory);
    }

}
