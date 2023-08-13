package share_diary.diray.diaryRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.auth.domain.NoAuth;
import share_diary.diray.auth.domain.token.AccessToken;
import share_diary.diray.auth.domain.token.RefreshToken;
import share_diary.diray.diaryRoom.DiaryRoomService;
import share_diary.diray.diaryRoom.controller.request.DiaryRoomCreateRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/diary-rooms")
public class DiaryRoomController {

    private final DiaryRoomService diaryRoomService;

    /**
     * 일기방 생성
     * - 토큰에서 생성하는 사람 정보 뽑아내는 방법 알아보기
     * - 일기방 만들 수 있는지 여부는 member controller에서 뽑아내기
     * */
    @PostMapping
    public ResponseEntity<HttpStatus> createDiaryRoom(
            @RequestBody DiaryRoomCreateRequest request,
            @AuthenticationPrincipal LoginSession session
            ) {
        diaryRoomService.createDiaryRoom(session.getId(), request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
