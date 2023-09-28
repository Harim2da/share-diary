package share_diary.diray.member.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.member.domain.Member;

@Getter
@NoArgsConstructor
public class MemberMyPageDTO {

    private String email;
    private String nickName;

    public MemberMyPageDTO(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

    public static MemberMyPageDTO toDto(Member member){
        return new MemberMyPageDTO(member.getEmail(),member.getNickName());
    }
}
