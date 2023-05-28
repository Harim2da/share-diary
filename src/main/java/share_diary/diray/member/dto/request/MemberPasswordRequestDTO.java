package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class MemberPasswordRequestDTO {

    private String memberId;
    private String password;

    public MemberPasswordRequestDTO() {
    }

    public MemberPasswordRequestDTO(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    public static MemberPasswordRequestDTO of(String memberId,String password){
        return new MemberPasswordRequestDTO(memberId,password);
    }
}
