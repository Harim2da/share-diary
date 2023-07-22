package share_diary.diray.memberInviteHistory.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInviteHistoryRepository extends JpaRepository<MemberInviteHistory, Long>,
        MemberInviteHistoryRepositoryCustom {

}
