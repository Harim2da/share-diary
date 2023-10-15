package share_diary.diray.memberInviteHistory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.memberInviteHistory.domain.InviteAcceptStatus;
import share_diary.diray.memberInviteHistory.domain.MemberInviteHistory;
import share_diary.diray.memberInviteHistory.dto.MemberInviteHistoryDTO;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {
        ReferenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberInviteHistoryMapper extends GenericMapper<MemberInviteHistoryDTO,MemberInviteHistory> {

    default MemberInviteHistoryDTO toDto(MemberInviteHistory entity){
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String uuid = null;
        String email = null;
        String hostUserId = null;
        InviteAcceptStatus status = null;
        LocalDateTime createDate = null;
        Long memberId = null;
        Long diaryRoomId = null;

        id = entity.getId();
        uuid = entity.getUuid();
        email = entity.getEmail();
        hostUserId = entity.getHostUserId();
        status = entity.getStatus();
        createDate = entity.getCreateDate();
        memberId = entity.getMember().getId();
        diaryRoomId = entity.getDiaryRoom().getId();

        MemberInviteHistoryDTO memberInviteHistoryDTO = new MemberInviteHistoryDTO( id, uuid, email, hostUserId, status, memberId, diaryRoomId, createDate );

        return memberInviteHistoryDTO;
    }
}
