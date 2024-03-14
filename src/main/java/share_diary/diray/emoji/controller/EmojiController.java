package share_diary.diray.emoji.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import share_diary.diray.auth.domain.AuthenticationPrincipal;
import share_diary.diray.auth.domain.LoginSession;
import share_diary.diray.emoji.EmojiService;
import share_diary.diray.emoji.controller.response.DiaryEmojiDTO;
import share_diary.diray.emoji.controller.request.DiaryEmojiRequestDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emoji")
@Tag(name = "Emoji",description = "Emoji API")
public class EmojiController {

    private final EmojiService emojiService;

    /**
     * 23/10/14
     * (친구 일기) 이모지 저장
     * @author jipdol2
     */
    @Operation(summary = "Update Emoji",description = "일기 이모지 저장 API")
    @PostMapping("/{diaryId}")
    public DiaryEmojiDTO clickEmoji(
            @AuthenticationPrincipal LoginSession session,
            @PathVariable("diaryId") Long diaryId,
            @RequestBody DiaryEmojiRequestDTO request
    ){
//        MyEmoji emoji = MyEmoji.valueOf(request.getEmoji().toUpperCase());
        return emojiService.clickEmoji(session.getId(),diaryId,request.getEmoji());
    }

    /**
     * (친구 일기) 이모지 조회
     * @author jipdol2
     */
    @Operation(summary = "Get Emoji",description = "이모지 조회 API")
    @GetMapping("/{diaryId}")
    public DiaryEmojiDTO findByMyEmoji(
            @AuthenticationPrincipal LoginSession session,
            @PathVariable("diaryId") Long diaryId
    ){
        return emojiService.findByEmojiCount(diaryId);
    }
}
