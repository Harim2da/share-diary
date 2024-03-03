package share_diary.diray.dailyDiary.controller.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;

@Getter
@AllArgsConstructor
public class DailyDiaryDTO {
    private Long id;
    private String content;
    private MyEmoji feeling;
    private DiaryStatus status;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime modifyDate;
    private String modifyBy;
}
