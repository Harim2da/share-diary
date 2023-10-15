package share_diary.diray.emoji.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryEmojiRequest {
    private String emoji;

    public DiaryEmojiRequest(String emoji) {
        this.emoji = emoji;
    }
}
