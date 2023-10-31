package share_diary.diray.diaryRoom.controller.response;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.domain.Role;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;

@Getter
@RequiredArgsConstructor
public class DiaryRoomMembersResponse {
    private Long diaryRoomId;
    private List<MemberInfos> memberInfos = Lists.newArrayList();

    @Getter
    @AllArgsConstructor
    public class MemberInfos {
        // 필요시 값 추가
        private Long memberId;
        private Role role;
        private String nickName;
        private LocalDate joinDate;
        private LocalDate exitDate;
    }

    public DiaryRoomMembersResponse of(Long diaryRoomId, List<MemberDiaryRoom> memberDiaryRooms) {
        DiaryRoomMembersResponse instance = new DiaryRoomMembersResponse();
        instance.diaryRoomId = diaryRoomId;
        List<MemberInfos> memberInfosList = new ArrayList<>();
        for(MemberDiaryRoom md : memberDiaryRooms) {
            Member member = md.getMember();
            memberInfosList.add(new MemberInfos(member.getId(), md.getRole(), member.getNickName(), md.getJoinDate(), md.getExitDate()));
        }
        instance.memberInfos = memberInfosList;

        return instance;
    }
}
