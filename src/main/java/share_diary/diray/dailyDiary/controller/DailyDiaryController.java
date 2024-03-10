package share_diary.diray.dailyDiary.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.dailyDiary.DailyDiaryService;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequestDTO;
import share_diary.diray.dailyDiary.controller.response.DailyDiaryDTO;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v0/daily-diaries")
@RequiredArgsConstructor
@Slf4j
public class DailyDiaryController {

    private final DailyDiaryService dailyDiaryService;

    /**
     * 한 번에 한 개의 일기만 조회하는 것으로 결정해
     * 일기는 단건 조회로 개발
     */
    @GetMapping
    public ResponseEntity<DailyDiaryDTO> getDailyDiary(
            @RequestParam Long diaryRoomId,
            @RequestParam String searchDate,
            @RequestParam Long memberId,
            @AuthenticationPrincipal LoginSession auth
    ) {
        return ResponseEntity.ok(dailyDiaryService.getDailyDiary(auth.getId(), diaryRoomId, searchDate, memberId));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> writeDailyDiary(
            @AuthenticationPrincipal LoginSession auth,
            @RequestBody DailyDiaryCreateModifyRequestDTO request
    ) {
        LocalDateTime now = LocalDateTime.now();
        dailyDiaryService.writeDailyDiary(auth.getId(), request, now);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{diaryId}")
    public ResponseEntity<DailyDiaryDTO> modifyDailyDiary(
            @PathVariable Long diaryId,
            @RequestBody DailyDiaryCreateModifyRequestDTO request,
            @AuthenticationPrincipal LoginSession auth
    ) {
        return ResponseEntity.ok(dailyDiaryService.modifyDailyDiary(diaryId, request, auth.getId()));
    }
}
