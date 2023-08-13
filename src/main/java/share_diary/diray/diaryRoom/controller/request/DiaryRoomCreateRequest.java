package share_diary.diray.diaryRoom.controller.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DiaryRoomCreateRequest {

    private String name;
    // 일기방 설명 추후 상황보고 진행
//    private String description;
    private List<String> emails;    // 초대 메일 보낼 이메일들
}
