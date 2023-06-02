package share_diary.diray.member.dto.request;

import lombok.Getter;
import lombok.ToString;
import share_diary.diray.member.domain.Member;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class MemberSignUpRequestDTO {

    @NotEmpty(message = "Id 정보가 올바르지 않습니다.")
    private String memberId;
    @NotEmpty(message = "email 정보가 올바르지 않습니다.")
    private String email;
    @NotEmpty(message = "비밀번호 정보가 올바르지 않습니다.")
    private String password;
    @NotEmpty(message = "닉네임 정보가 올바르지 않습니다.")
    private String nickName;

    public MemberSignUpRequestDTO() {
    }

    public MemberSignUpRequestDTO(String memberId, String email, String password, String nickName) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    public static MemberSignUpRequestDTO of(String memberId, String email, String password, String nickName){
        return new MemberSignUpRequestDTO(memberId,email,password,nickName);
    }

    public static Member fromToMember(MemberSignUpRequestDTO signUpRequestDTO){
        return Member.builder()
                .memberId(signUpRequestDTO.getMemberId())
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .nickName(signUpRequestDTO.getNickName())
                .build();
    }
}