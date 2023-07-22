package share_diary.diray.common.email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@AllArgsConstructor
@Slf4j
public class EmailSenderComponent {

    private final JavaMailSender mailSender;

    @Async
    public ListenableFuture<Boolean> sendMemberInviteMail(String diaryRoomName, String sendTo, String uuid) {
        // TODO : 보낸 사람 확인이 가능하면 보낸 사람 계정 추가, 초대한 멤버 가입, 로그인 API 확정 후 보내는 url 수정 필요

        String subject = new StringBuilder().append("[itsDiary] ")
                .append(diaryRoomName)
                .append(" 일기방에 초대 받았습니다.")
                .toString();

        String messageBody = new StringBuilder()
                .append(diaryRoomName)
                .append(" 일기방에 초대 받았습니다.\n초대를 수락하시려면 아래 링크를 클릭해주세요.\n")
                .append("http://localhost:8080/uuid=")
                .append(uuid)
                .toString();
        return new AsyncResult<>(send(subject, sendTo, messageBody));
    }

    private boolean send(String subject, String sendTo, String messageBody) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(sendTo);
            message.setSubject(subject);
            message.setText(messageBody);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
