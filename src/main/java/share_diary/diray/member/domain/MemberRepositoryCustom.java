package share_diary.diray.member.domain;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findAllByEmail(List<String> emails);
}
