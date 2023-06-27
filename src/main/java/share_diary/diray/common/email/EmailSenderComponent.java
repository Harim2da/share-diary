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

    // 테스트 위해서 임시로 작성
    @Async
    public ListenableFuture<Boolean> sendTest(String sendTo) {
        String messageBody = "테스트입니다22";
        return new AsyncResult<>(send("test2", sendTo, messageBody));
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
