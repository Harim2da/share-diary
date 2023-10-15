package share_diary.diray.memberInviteHistory.mapper;

import org.mapstruct.*;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.member.domain.Member;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.dto.MemberInviteHistoryDTO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ReferenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberInviteHistoryMapper extends GenericMapper<MemberInviteHistoryDTO,MemberInviteHistory> {

    @Named(value = "asMemberInviteHistoryDTO")
    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "diaryRoomId", source = "diaryRoom.id")
    MemberInviteHistoryDTO asMemberInviteHistoryDTO(MemberInviteHistory memberInviteHistory);

    @IterableMapping(qualifiedByName = "asMemberInviteHistoryDTO")
    List<MemberInviteHistoryDTO> asMemberInviteHistoryDTOList(List<MemberInviteHistory> memberInviteHistoryDTOList);
}
