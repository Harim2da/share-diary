package share_diary.diray.dailyDiary.controller.request;

import java.util.List;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyDiaryCreateModifyRequest {

    private String content; // 일기 내용
    private MyEmoji feeling;   // 오늘 하루 이모지
    private List<Long> diaryRooms;  // 적용할 일기방 번호들
    private DiaryStatus status; // 수정할 때만 필요
}
