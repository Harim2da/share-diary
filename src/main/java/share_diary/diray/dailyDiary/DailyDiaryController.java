package share_diary.diray.dailyDiary;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import share_diary.diray.common.response.PagingResponse;

@RestController
@RequestMapping("/api/v0/daily-diaries")
@RequiredArgsConstructor
@Slf4j
public class DailyDiaryController {
    /**
     * 1차 설계용
     * Response 및 request 작업 전
     * */
    @GetMapping()
    public PagingResponse getDailyDiaries() {
        return null;
    }

    @PostMapping()
    public void createDailyDiary() {

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
