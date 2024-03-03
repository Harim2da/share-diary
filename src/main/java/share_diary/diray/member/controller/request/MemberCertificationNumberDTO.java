package share_diary.diray.member.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberCertificationNumberDTO {

    int certificationNumber;

    public MemberCertificationNumberDTO(int certificationNumber){
        this.certificationNumber = certificationNumber;
    }

    public static MemberCertificationNumberDTO of(int certificationNumber){
        return new MemberCertificationNumberDTO(certificationNumber);
    }
}
