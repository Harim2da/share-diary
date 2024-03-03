package share_diary.diray.emoji.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryEmojiRequestDTO {
    private String emoji;

    public DiaryEmojiRequestDTO(String emoji) {
        this.emoji = emoji;
    }
}
