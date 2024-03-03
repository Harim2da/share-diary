package share_diary.diray.diaryRoom.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.diaryRoom.domain.DiaryRoom;
import share_diary.diray.diaryRoom.controller.response.DiaryRoomDTO;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiaryRoomMapper extends GenericMapper<DiaryRoomDTO, DiaryRoom> {

}
