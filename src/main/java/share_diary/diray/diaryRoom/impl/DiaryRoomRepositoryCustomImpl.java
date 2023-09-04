package share_diary.diray.diaryRoom.impl;

import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.diaryRoom.DiaryRoom;
import share_diary.diray.diaryRoom.DiaryRoomRepositoryCustom;
import static share_diary.diray.diaryRoom.QDiaryRoom.diaryRoom;
import static share_diary.diray.memberDiaryRoom.domain.QMemberDiaryRoom.memberDiaryRoom;

public class DiaryRoomRepositoryCustomImpl extends QuerydslRepositorySupport implements DiaryRoomRepositoryCustom {

    public DiaryRoomRepositoryCustomImpl() {
        super(DiaryRoom.class);
    }

    @Override
    public List<DiaryRoom> findAllByMemberIdWithMemberDiaryRoom(Long memberId) {
        return from(diaryRoom)
                .join(diaryRoom.memberDiaryRooms, memberDiaryRoom).fetchJoin()
                .where(
                        memberDiaryRoom.member.id.eq(memberId)
                )
                .fetch();
    }
}
