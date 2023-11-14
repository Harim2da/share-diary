package share_diary.diray.dailyDiary.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.common.response.PagingResponse;
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
     * 1차 설계용
     * Response 및 request 작업 전
     * */
    @GetMapping()
    public PagingResponse getDailyDiaries() {
        return null;
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

    @DeleteMapping("/{diaryId}")
    public void deleteDailyDiary(@PathVariable Long diaryId) {

    }
}
