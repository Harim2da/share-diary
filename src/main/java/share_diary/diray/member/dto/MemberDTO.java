package share_diary.diray.member.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public class MemberDTO {

    protected Long id;
    protected String loginId;
    protected String email;
    protected String nickName;
    protected String joinStatus; // 이건 좀 더 고민
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Seoul")
    protected LocalDateTime joinTime;
//    protected LocalDateTime createDate;
//    protected String createBy;
//    protected LocalDateTime lastModifyDate;
//    protected String lastModifiedBy;

    @Builder
    public MemberDTO(Long id, String loginId, String email, String nickName, String joinStatus, LocalDateTime joinTime) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.nickName = nickName;
        this.joinStatus = joinStatus;
        this.joinTime = joinTime;
//        this.createDate = createDate;
//        this.createBy = createBy;
//        this.lastModifyDate = lastModifyDate;
//        this.lastModifiedBy = lastModifiedBy;
    }
}
