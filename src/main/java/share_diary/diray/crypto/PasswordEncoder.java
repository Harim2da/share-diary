package share_diary.diray.crypto;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Primary
@Component
public class PasswordEncoder{

    SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
            32,8,1,32,16);

    public String encode(String password){
        return encoder.encode(password);
    }

    //(평문 패스워드,암호화된 패스워드)
    public boolean matches(String rowPassword,String encryptPassword){
        return encoder.matches(rowPassword,encryptPassword);
    }
}
