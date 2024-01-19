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
    public ListenableFuture<Boolean> sendMemberInviteMail(String diaryRoomName, String hostName, String sendTo, String uuid) {
        String subject = new StringBuilder().append("[itsDiary] ")
                .append(hostName)
                .append("님이 ")
                .append(diaryRoomName)
                .append(" 일기방에 초대 했습니다.")
                .toString();

        String messageBody = new StringBuilder()
                .append(hostName)
                .append("님이 ")
                .append(diaryRoomName)
                .append(" 일기방에 초대 했습니다.\n초대를 수락하시려면 아래 링크를 클릭해주세요.\n")
                .append("http://localhost:8080/uuid/") // TODO : 도메인 혹은 ip 나오면 추가 수정 필요
                .append(uuid)
                .toString();
        return new AsyncResult<>(send(subject, sendTo, messageBody));
    }

    /**
     * 인증번호 이메일로 발송
     * certificationNumber : 인증번호
     * sendTo : 수신 이메일
     */
    @Async
    public ListenableFuture<Boolean> sendCertificationNumber(int certificationNumber, String sendTo){

        String subject = new StringBuilder().append("[itsDiary] ")
                .append(" 인증번호가 발송되었습니다")
                .toString();

        String messageBody = new StringBuilder()
                .append("인증번호 : ")
                .append(String.valueOf(certificationNumber))
                .toString();

        return new AsyncResult<>(send(subject,sendTo,messageBody));
    }

    /**
     * 임시 비밀번호 전송
     * tempPassword : 임시 비밀번호
     * sendTo : 수신 이메일
     */
    @Async
    public ListenableFuture<Boolean> sendTempPasswordMail(String tempPassword, String sendTo){

        String subject = new StringBuilder().append("[itsDiary] ")
                .append(" 임시 비밀번호가 발급되었습니다")
                .toString();

        String messageBody = new StringBuilder()
                .append("임시 비밀번호 : ")
                .append(tempPassword)
                .toString();
        return new AsyncResult<>(send(subject,sendTo,messageBody));
    }

    /**
     * 메일 전송 내용
     * subject : 메일 제목
     * sendTo : 수신 이메일
     * messageBody : 메일 내용
     */
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
