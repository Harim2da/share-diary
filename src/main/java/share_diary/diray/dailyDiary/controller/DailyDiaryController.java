package share_diary.diray.dailyDiary.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.common.response.PagingResponse;
import share_diary.diray.dailyDiary.DailyDiaryService;
import share_diary.diray.dailyDiary.controller.request.DailyDiaryCreateModifyRequest;

@RestController
@RequestMapping("/api/v0/daily-diaries")
@RequiredArgsConstructor
@Slf4j
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
    public void createDailyDiary(
            @AuthenticationPrincipal LoginSession auth,
            DailyDiaryCreateModifyRequest request
    ) {
        dailyDiaryService.createDailyDiary(auth.getId(), request);
    }

    @PatchMapping()
    public void modifyDailyDiary(
            @PathVariable Long diaryId
    ) {

    }

    @DeleteMapping("/{diaryId}")
    public void deleteDailyDiary(@PathVariable Long diaryId) {

    }
}
