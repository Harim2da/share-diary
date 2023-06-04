package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberPasswordRequestDTO {

    private String password;

    public MemberPasswordRequestDTO() {
    }

    public MemberPasswordRequestDTO(String password) {
        this.password = password;
    }

    public static MemberPasswordRequestDTO from(String password){
        return new MemberPasswordRequestDTO(password);
    }
}
