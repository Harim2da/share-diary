package share_diary.diray.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import share_diary.diray.common.mapper.GenericMapper;
import share_diary.diray.common.mapper.ReferenceMapper;
import share_diary.diray.member.domain.Member;
import share_diary.diray.member.controller.response.MemberDTO;

@Mapper(componentModel = "spring", uses = {
        ReferenceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper extends GenericMapper<MemberDTO, Member> {

}
