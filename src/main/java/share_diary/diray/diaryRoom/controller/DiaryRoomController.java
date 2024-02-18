package share_diary.diray.diaryRoom.controller;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.diaryRoom.DiaryRoomService;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomHostModifyRequest;
import share_diary.diray.diaryRoom.controller.response.DiaryRoomMembersResponse;
import share_diary.diray.diaryRoom.dto.DiaryRoomDTO;
import share_diary.diray.exception.http.ForbiddenException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/diary-rooms")
@Tag(name = "DiaryRoom",description = "DiaryRoom API")
public class DiaryRoomController {

    private final DiaryRoomService diaryRoomService;

    /**
     * 일기방 생성
     * - 일기방 만들 수 있는지 여부는 member controller에서 뽑아내기
     * @author harim
     * */
    @PostMapping
    public ResponseEntity<HttpStatus> createDiaryRoom(
            @RequestBody DiaryRoomCreateRequest request,
            @AuthenticationPrincipal LoginSession session
            ) {
        LocalDateTime now = LocalDateTime.now();
        diaryRoomService.createDiaryRoom(session.getId(), request,now);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 참여하고 있는 일기방 목록 조회 api
     * @author harim
     */
    // TODO : 일기방 정렬 방식 논의 필요
    @GetMapping
    public ResponseEntity<List<DiaryRoomDTO>> getDiaryRooms(
            @AuthenticationPrincipal LoginSession session,
            @RequestParam(required = false) Long lastDiaryId,
            @RequestParam Integer limit
            ) {
        return ResponseEntity.ok(diaryRoomService.getDiaryRooms(session.getId(),lastDiaryId,limit));
    }

    /**
     * 일기방에 속해 있는
     * @param diaryRoomId
     * @param searchDate
     * @param session
     * @return
     */
    @GetMapping("/{diaryRoomId}/members")
    public ResponseEntity<DiaryRoomMembersResponse> getDiaryRoomMembers(
            @PathVariable Long diaryRoomId,
            @RequestParam String searchDate,
            @AuthenticationPrincipal LoginSession session
    ) {
        return ResponseEntity.ok(diaryRoomService.getDiaryRoomMembers(diaryRoomId, searchDate, session.getId()));
    }

    /**
     * 내가 속한 특정 일기방에서 나가기
     * @author harim
     * */
    @DeleteMapping("/{diaryRoomId}")
    public ResponseEntity<Boolean> deleteDiaryRoomMember(
            @PathVariable Long diaryRoomId,
            @AuthenticationPrincipal LoginSession session
    ) {
        return ResponseEntity.ok(diaryRoomService.deleteDiaryRoomMember(diaryRoomId, session.getId()));
    }

    /**
     * 내가 일기방 Host인 경우, 다른 사람에게 Host를 넘기고
     * 특정 일기방 탈퇴하기
     * */
    @PostMapping("/{diaryRoomId}")
    public ResponseEntity<HttpStatus> modifyDiaryRoomHost(
            @PathVariable Long diaryRoomId,
            @RequestBody DiaryRoomHostModifyRequest request,
            @AuthenticationPrincipal LoginSession session
    ) {
        if(!session.getId().equals(request.getAsIsHostId())) {
            throw new ForbiddenException();
        }
        diaryRoomService.modifyDiaryRoomHost(diaryRoomId, request, session.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
