package share_diary.diray.emoji;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.shaded.freemarker.template.utility.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.emoji.domain.MyEmoji;
import share_diary.diray.emoji.dto.request.DiaryEmojiRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emoji")
public class EmojiController {

    private final EmojiService emojiService;

    /**
     * 23/10/14
     * (나의 일기) 이모지 저장
     * @author jipdol2
     */
    @PostMapping("/{diaryId}")
    public ResponseEntity<Void> saveMyEmoji(
            @AuthenticationPrincipal LoginSession session,
            @PathVariable("diaryId") Long diaryId,
            @RequestBody DiaryEmojiRequest request
    ){
        MyEmoji emoji = MyEmoji.valueOf(request.getEmoji().toUpperCase());
        return null;
    }

    /**
     * (나의 일기) 이모지 조회
     * @author jipdol2
     */
    @GetMapping("/{diaryId}")
    public ResponseEntity<Void> findByMyEmoji(
            @AuthenticationPrincipal LoginSession session,
            @PathVariable("diaryId") Long diaryId
    ){

        return ResponseEntity.ok().build();
    }
}
