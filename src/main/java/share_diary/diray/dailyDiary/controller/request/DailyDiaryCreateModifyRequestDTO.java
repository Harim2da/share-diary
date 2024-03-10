package share_diary.diray.dailyDiary.controller.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.dailyDiary.domain.DiaryStatus;
import share_diary.diray.dailyDiary.domain.MyEmoji;

@Getter
@NoArgsConstructor
public class DailyDiaryCreateModifyRequestDTO {

    private String content; // 일기 내용
    private MyEmoji feeling;   // 오늘 하루 이모지
    private List<Long> diaryRooms;  // 적용할 일기방 번호들
    private DiaryStatus status; // 수정할 때만 필요

    @Builder
    public DailyDiaryCreateModifyRequestDTO(String content, MyEmoji feeling, List<Long> diaryRooms, DiaryStatus status) {
        this.content = content;
        this.feeling = feeling;
        this.diaryRooms = diaryRooms;
        this.status = status;
    }

    public static DailyDiaryCreateModifyRequestDTO of(String content,MyEmoji feeling, List<Long> diaryRooms, DiaryStatus status){
        return DailyDiaryCreateModifyRequestDTO.builder()
                .content(content)
                .feeling(feeling)
                .diaryRooms(diaryRooms)
                .status(status)
                .build();
    }
}
