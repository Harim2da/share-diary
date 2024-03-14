package share_diary.diray.member.domain.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import share_diary.diray.member.domain.JoinStatus;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.MemberRepositoryCustom;

import java.util.List;

import static share_diary.diray.member.domain.QMember.member;
public class MemberRepositoryCustomImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryCustomImpl() {
        super(Member.class);
    }


    @Override
    public List<Member> findAllByEmail(List<String> emails) {
        return from(member)
                .where(
                        member.email.in(emails)
                )
                .fetch();
    }

    /**
     * 일기방 초대를 통해 생성된 비회원 상태를 수락을 통해 회원으로 진행
     */
    @Override
    public boolean isJoinedMember(String email){
        return from(member)
                .where(
                        member.email.eq(email)
                                .and(member.joinStatus.eq(JoinStatus.WAITING))
                )
                .fetchCount()>0;
    }
}
