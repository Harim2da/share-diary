package share_diary.diray.dailyDiary.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.dailyDiary.DailyDiaryService;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;
import share_diary.diray.dailyDiary.dto.DailyDiaryDTO;

@RestController
@RequestMapping("/api/v0/daily-diaries")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "DailyDiary",description = "DailyDiary API")
public class DailyDiaryController {

    private final DailyDiaryService dailyDiaryService;

    /**
     * 한 번에 한 개의 일기만 조회하는 것으로 결정해
     * 일기는 단건 조회로 개발
     * */
    @GetMapping()
    public ResponseEntity<DailyDiaryDTO> getDailyDiary(
            @RequestParam Long diaryRoomId,
            @RequestParam String searchDate,
            @RequestParam Long memberId,
            @AuthenticationPrincipal LoginSession auth
    ) {
        return ResponseEntity.ok(dailyDiaryService.getDailyDiary(auth.getId(), diaryRoomId, searchDate, memberId));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createDailyDiary(
            @AuthenticationPrincipal LoginSession auth,
            @RequestBody DailyDiaryCreateModifyRequest request
    ) {
        dailyDiaryService.createDailyDiary(auth.getId(), request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{diaryId}")
    public ResponseEntity<DailyDiaryDTO> modifyDailyDiary(
            @PathVariable Long diaryId,
            @RequestBody DailyDiaryCreateModifyRequest request,
            @AuthenticationPrincipal LoginSession auth
    ) {
        return ResponseEntity.ok(dailyDiaryService.modifyDailyDiary(diaryId, request, auth.getId()));
    }
}
