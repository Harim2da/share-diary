package share_diary.diray.memberDiaryRoom.domain.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepositoryCustom;

import static share_diary.diray.memberDiaryRoom.domain.QMemberDiaryRoom.memberDiaryRoom;
import static share_diary.diray.diaryRoom.QDiaryRoom.diaryRoom;

public class MemberDiaryRoomRepositoryCustomImpl extends QuerydslRepositorySupport implements MemberDiaryRoomRepositoryCustom {

    public MemberDiaryRoomRepositoryCustomImpl() {
        super(MemberDiaryRoom.class);
    }

    @Override
    public Optional<MemberDiaryRoom> findByMemberIdAndDiaryRoomIdWithDiaryRoom(
            Long memberId,
            Long diaryRoomId
    ) {
        return Optional.ofNullable(
                from(memberDiaryRoom)
                        .join(memberDiaryRoom.diaryRoom).fetchJoin()
                        .where(memberDiaryRoom.diaryRoom.id.eq(diaryRoomId)
                                .and(memberDiaryRoom.member.id.eq(memberId))
                                .and(memberDiaryRoom.role.eq(Role.HOST)))
                        .fetchOne()
        );
    }

    @Override
    public List<MemberDiaryRoom> findAllByMemberId(Long memberId) {
        return from(memberDiaryRoom)
                .join(memberDiaryRoom.member).fetchJoin()
                .join(memberDiaryRoom.diaryRoom).fetchJoin()
                .where(memberDiaryRoom.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<MemberDiaryRoom> findAllByDiaryRoomIdAndSearchDateWithMember(
            Long diaryRoomId,
            LocalDate searchDate
    ) {
        return from(memberDiaryRoom)
                .join(memberDiaryRoom.member).fetchJoin()
                .where(memberDiaryRoom.diaryRoom.id.eq(diaryRoomId)
                        .and(memberDiaryRoom.joinDate.loe(searchDate)))
                .fetch();
    }

    @Override
    public Optional<MemberDiaryRoom> findByDiaryRoomIdAndSearchDateAndMemberId(
            Long diaryRoomId,
            LocalDate searchDate,
            Long memberId
    ) {
        return Optional.ofNullable(
                from(memberDiaryRoom)
                        .join(memberDiaryRoom.member).fetchJoin()
                        .where(memberDiaryRoom.diaryRoom.id.eq(diaryRoomId)
                                .and(memberDiaryRoom.member.id.eq(memberId))
                                .and(memberDiaryRoom.joinDate.loe(searchDate)))
                        .fetchOne()
        );
    }
}
