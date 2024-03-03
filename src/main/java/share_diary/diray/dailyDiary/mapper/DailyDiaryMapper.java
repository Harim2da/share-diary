package share_diary.diray.dailyDiary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.dailyDiary.domain.DailyDiary;
import share_diary.diray.dailyDiary.controller.response.DailyDiaryDTO;

@Mapper(componentModel = "spring", uses = {
        ReferenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DailyDiaryMapper extends GenericMapper<DailyDiaryDTO, DailyDiary> {

}
