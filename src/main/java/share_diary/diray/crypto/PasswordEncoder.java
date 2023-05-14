package share_diary.diray.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder{

    SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
            32,8,1,32,16);

    public String encode(String password){
        return encoder.encode(password);
    }

    public boolean match(String rowPassword,String encryptPassword){
        return encoder.matches(rowPassword,encryptPassword);
    }
}
