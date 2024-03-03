package share_diary.diray.memberInviteHistory.mapper;

import org.mapstruct.*;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.controller.response.MemberInviteHistoryDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ReferenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberInviteHistoryMapper extends GenericMapper<MemberInviteHistoryDTO,MemberInviteHistory> {

    @Named(value = "asMemberInviteHistoryDTO")
    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "diaryRoomId", source = "diaryRoom.id")
    @Mapping(target = "diaryRoomName",source = "diaryRoom.name")
    MemberInviteHistoryDTO asMemberInviteHistoryDTO(MemberInviteHistory memberInviteHistory);

    @IterableMapping(qualifiedByName = "asMemberInviteHistoryDTO")
    List<MemberInviteHistoryDTO> asMemberInviteHistoryDTOList(List<MemberInviteHistory> memberInviteHistoryDTOList);
}
