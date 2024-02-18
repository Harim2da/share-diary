package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import share_diary.diray.member.domain.Member;

@Getter
@NoArgsConstructor
public class MemberSignUpSocialRequestDTO {

    private String id;
    private String email;
    private String nickname;

    public MemberSignUpSocialRequestDTO(String id,String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static Member fromToMember(MemberSignUpSocialRequestDTO dto){
        return Member.builder()
                .loginId(dto.getId())
                .email(dto.getEmail())
                .nickName(dto.getNickname())
                .build();
    }
}