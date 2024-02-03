package share_diary.diray.diaryRoom.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
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
    public List<DiaryRoom> findAllByMemberIdWithMemberDiaryRoom(Long memberId, Long diaryRoomId, int limit) {
        return from(diaryRoom)
                .join(diaryRoom.memberDiaryRooms, memberDiaryRoom).fetchJoin()
                .where(
                        eqMemberId(memberId),
                        ltDiaryRoomId(diaryRoomId),
                        memberDiaryRoom.exitDate.isNotNull()
                )
                .orderBy(diaryRoom.registeredDate.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression eqMemberId(Long memberId){
        if(memberId == null){
            return null;
        }
        return memberDiaryRoom.member.id.eq(memberId);
    }

    private BooleanExpression ltDiaryRoomId(Long diaryRoomId){
        if(diaryRoomId == null){
            return null;
        }
        return diaryRoom.id.lt(diaryRoomId);
    }
}
