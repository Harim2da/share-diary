package share_diary.diray.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import share_diary.diray.member.domain.Member;

@Getter
@ToString
public class MemberResponseDTO {

    private String memberId;
    private String email;
    private String nickName;

    public MemberResponseDTO(String memberId, String email, String nickName) {
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
    }

    public static MemberResponseDTO from(Member member){
        return new MemberResponseDTO(
                member.getMemberId(),
                member.getEmail(),
                member.getNickName());
    }
}
