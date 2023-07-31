package share_diary.diray.diaryRoom;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import share_diary.diray.member.domain.Member;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoom;
import share_diary.diray.memberDiaryRoom.domain.MemberDiaryRoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiaryRoomService {

    private final MemberDiaryRoomRepository memberDiaryRoomRepository;

    public void joinNewMember(Member member, DiaryRoom diaryRoom) {
        Optional<MemberDiaryRoom> memberDiaryRoom = memberDiaryRoomRepository.findByMemberIdAndDiaryRoomIdWithDiaryRoom(member.getId(), diaryRoom.getId());
        if(Objects.isNull(memberDiaryRoom)) {
            memberDiaryRoomRepository.save(MemberDiaryRoom.of(member, diaryRoom));
        }
    }
}
