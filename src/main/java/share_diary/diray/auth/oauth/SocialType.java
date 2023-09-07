package share_diary.diray.auth.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum SocialType {
    GITHUB("GITHUB"),
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO")
    ;

    private final static Map<String,SocialType> codeMap = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(socialType -> codeMap.put(socialType.getSocialName(),socialType));
    }

    private String socialName;

    SocialType(String socialName) {
        this.socialName = socialName;
    }

    public static boolean contain(String provider){
        return codeMap.containsKey(provider.toUpperCase());
    }

    public static SocialType getSocialType(String provider){
        SocialType socialType = codeMap.get(provider.toUpperCase());
        return socialType;
    }
}
