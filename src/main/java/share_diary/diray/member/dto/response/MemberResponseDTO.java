package share_diary.diray.member.dto.response;

import lombok.Getter;
import lombok.ToString;
import share_diary.diray.member.domain.Member;

@Getter
@ToString
public class MemberResponseDTO {

    private String loginId;
    private String email;
    private String nickName;

    public MemberResponseDTO(String loginId, String email, String nickName) {
        this.loginId = loginId;
        this.email = email;
        this.nickName = nickName;
    }

    public static MemberResponseDTO from(Member member){
        return new MemberResponseDTO(
                member.getLoginId(),
                member.getEmail(),
                member.getNickName());
    }
}
