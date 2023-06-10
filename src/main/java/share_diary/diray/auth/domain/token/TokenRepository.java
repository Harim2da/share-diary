package share_diary.diray.auth.domain.token;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<RefreshToken,String> {
}
