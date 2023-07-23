package share_diary.diray.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,
        MemberRepositoryCustom {

    //    @Query("SELECT m FROM Member m WHERE m.memberId = :memberId")
    Optional<Member> findByMemberId(@Param("memberId") String memberId);

    //    @Query("SELECT count(m) FROM Member m WHERE m.memberId = :memberId")
    Long countByMemberId(@Param("memberId") String memberId);

    //    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    Long countByEmail(@Param("email") String email);
}
