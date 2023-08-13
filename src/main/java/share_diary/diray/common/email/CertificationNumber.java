package share_diary.diray.common.email;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "certificationNumber",timeToLive = 1800)
public class CertificationNumber {

    @Id
    int certificationNumber;
    Long memberId;

    public CertificationNumber(int certificationNumber, Long memberId) {
        this.certificationNumber = certificationNumber;
        this.memberId = memberId;
    }

    public static CertificationNumber of(int certificationNumber,Long memberId){
        return new CertificationNumber(certificationNumber,memberId);
    }
}
