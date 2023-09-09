package share_diary.diray.member.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDTO {

    protected Long id;
    protected String loginId;
    protected String email;
    protected String password;
    protected String nickName;
    protected String joinStatus; // 이건 좀 더 고민
    protected LocalDateTime joinTime;
    protected LocalDateTime createDate;
    protected String createBy;
    protected LocalDateTime lastModifyDate;
    protected String lastModifiedBy;
}
