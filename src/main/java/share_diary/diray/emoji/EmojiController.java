package share_diary.diray.emoji;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.emoji.dto.request.DiaryEmojiRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emoji")
public class EmojiController {

    private final EmojiService emojiService;

    /**
     * 23/10/14
     * (친구 일기) 이모지 저장
     * @author jipdol2
     */
    //TODO : 내일기 감정이모지 -> daliyDiary 로 이전으로 인한 수정 필요
    @PostMapping("/{diaryId}")
    public ResponseEntity<Void> clickEmoji(
            @AuthenticationPrincipal LoginSession session,
            @PathVariable("diaryId") Long diaryId,
            @RequestBody DiaryEmojiRequest request
    ){
//        MyEmoji emoji = MyEmoji.valueOf(request.getEmoji().toUpperCase());
        return ResponseEntity.ok().build();
    }

    /**
     * (친구 일기) 이모지 조회
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
