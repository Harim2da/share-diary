package share_diary.diray.diaryRoom.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.diaryRoom.DiaryRoomRepositoryCustom;

public class DiaryRoomRepositoryCustomImpl extends QuerydslRepositorySupport implements DiaryRoomRepositoryCustom {

    public DiaryRoomRepositoryCustomImpl() {
        super(DiaryRoom.class);
    }
}
