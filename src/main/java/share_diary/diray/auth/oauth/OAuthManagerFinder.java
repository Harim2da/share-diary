package share_diary.diray.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthManagerFinder {

    private final List<OAuthManager> oAuthManager;

    public OAuthManager findByOAuthManager(SocialType socialType){
        return oAuthManager.stream()
                .filter(manager -> manager.getType().equals(socialType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
