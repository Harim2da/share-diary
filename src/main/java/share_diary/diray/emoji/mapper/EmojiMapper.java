package share_diary.diray.emoji.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.emoji.domain.Emoji;
import share_diary.diray.emoji.dto.DiaryEmojiDTO;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmojiMapper extends GenericMapper<DiaryEmojiDTO, Emoji> {

    @Mapping(target = "heart",source = "heartEmojiNumber")
    @Mapping(target = "thumb",source = "thumbSupEmojiNumber")
    @Mapping(target = "party",source = "partyPopperEmojiNumber")
    @Mapping(target = "cake",source = "cakeEmojiNumber")
    @Mapping(target = "devil",source = "devilEmojiNumber")
    DiaryEmojiDTO asDiaryEmojiDTO(Emoji emoji);
}
