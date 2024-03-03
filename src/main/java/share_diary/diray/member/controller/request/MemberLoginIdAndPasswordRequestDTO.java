package share_diary.diray.member.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginIdAndPasswordRequestDTO {

    private String loginId;
    private String password;

    public MemberLoginIdAndPasswordRequestDTO(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public static MemberLoginIdAndPasswordRequestDTO of(String loginId,String password){
        return new MemberLoginIdAndPasswordRequestDTO(loginId,password);
    }
}
