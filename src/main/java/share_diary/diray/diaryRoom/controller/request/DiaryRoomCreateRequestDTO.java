package share_diary.diray.diaryRoom.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DiaryRoomCreateRequestDTO {

    private String name;
    // 일기방 설명 추후 상황보고 진행
//    private String description;
    private List<String> emails;    // 초대 메일 보낼 이메일들

    public DiaryRoomCreateRequestDTO(String name, List<String> emails) {
        this.name = name;
        this.emails = emails;
    }
}
