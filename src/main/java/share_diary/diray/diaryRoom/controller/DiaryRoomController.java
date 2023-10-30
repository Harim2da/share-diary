package share_diary.diray.diaryRoom.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import share_diary.diray.diaryRoom.controller.response.DiaryRoomMembersResponse;
import share_diary.diray.diaryRoom.dto.DiaryRoomDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/diary-rooms")
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
        diaryRoomService.createDiaryRoom(session.getId(), request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 참여하고 있는 일기방 목록 조회 api
     * @author harim
     */
    // TODO : 일기방 정렬 방식 논의 필요
    @GetMapping
    public ResponseEntity<List<DiaryRoomDTO>> getDiaryRooms(
            @AuthenticationPrincipal LoginSession session
    ) {
        return ResponseEntity.ok(diaryRoomService.getDiaryRooms(session.getId()));
    }

    @GetMapping("/{diaryRoomId}/members")
    public ResponseEntity<DiaryRoomMembersResponse> getDiaryRoomMembers(
            @PathVariable Long diaryRoomId,
            @RequestParam String searchDate,
            @AuthenticationPrincipal LoginSession session
    ) {
        return ResponseEntity.ok(diaryRoomService.getDiaryRoomMembers(diaryRoomId, searchDate, session.getId()));
    }
}
