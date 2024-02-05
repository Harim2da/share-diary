package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class MemberPasswordRequestDTO {

    @NotEmpty(message = "아이디의 정보가 올바르지 않습니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호 정보가 올바르지 않습니다.")
    private String password;

    public MemberPasswordRequestDTO() {
    }

    public MemberPasswordRequestDTO(String loginId,String password) {
        this.password = password;
    }

    public static MemberPasswordRequestDTO from(String loginId,String password){
        return new MemberPasswordRequestDTO(loginId,password);
    }
}
