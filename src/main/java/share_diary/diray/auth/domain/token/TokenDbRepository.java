package share_diary.diray.auth.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenDbRepository extends JpaRepository<RefreshTokenDB,Long> {

    Optional<RefreshTokenDB> findByRefreshToken(@Param("refreshToken") String refreshToken);
}
