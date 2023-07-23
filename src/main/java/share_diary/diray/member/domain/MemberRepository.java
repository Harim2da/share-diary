package share_diary.diray.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByLoginId(@Param("loginId")String loginId);

    Optional<Member> findByEmail(@Param("email")String email);

    boolean existsByLoginId(@Param("loginId")String loginId);

    boolean existsByEmail(@Param("email") String email);
}
