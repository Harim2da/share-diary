package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;
import share_diary.diray.member.domain.Member;

@Getter
@ToString
public class MemberSignUpRequestDTO {

    private String memberId;

    private String email;

    private String password;

    private String nickName;

    public static Member fromToMember(MemberSignUpRequestDTO signUpRequestDTO){
        return Member.builder()
                .memberId(signUpRequestDTO.getMemberId())
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .nickName(signUpRequestDTO.getNickName())
                .build();
    }
}
