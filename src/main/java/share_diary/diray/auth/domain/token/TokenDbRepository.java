package share_diary.diray.auth.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDbRepository extends JpaRepository<RefreshTokenDB,Long> {
}
