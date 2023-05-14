package share_diary.diray.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberResponseDTO {

    private String userId;

    private String email;

    private String password;

    private String nickName;

    @Builder
    public MemberResponseDTO(String userId, String email, String password, String nickName) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
}
