package share_diary.diray.auth.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import share_diary.diray.auth.oauth.token.GoogleTokenRequest;
import share_diary.diray.auth.oauth.token.GoogleTokenResponse;
import share_diary.diray.auth.oauth.userInfo.GoogleUserInfoResponse;
import share_diary.diray.member.domain.Member;

@Getter
@Component
public class GoogleOAuthManager implements OAuthManager{

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.url.access-token}")
    private String code;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Value("${google.grant-type}")
    private String grantType;

    @Value("${google.url.access-token}")
    private String accessTokenUrl;

    @Value("${google.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public GoogleOAuthManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Member getMemberInfo(String code) {
        UriComponents uri = UriComponentsBuilder
                .fromUriString(profileUrl)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION,"Bearer "+getAccessToken(code));

        HttpEntity<Object> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<GoogleUserInfoResponse> response =
                restTemplate.exchange(uri.toString(), HttpMethod.GET, request, GoogleUserInfoResponse.class);

        GoogleUserInfoResponse body = response.getBody();
        return body.toMember();
    }

    @Override
    public String getAccessToken(String code) {
        GoogleTokenRequest requestEntity = GoogleTokenRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .redirectUri(redirectUri)
                .grantType(grantType)
                .build();

        UriComponents uri = UriComponentsBuilder
                .fromUriString(accessTokenUrl)
                .build();

        ResponseEntity<GoogleTokenResponse> responseEntity =
                restTemplate.postForEntity(uri.toString(), requestEntity, GoogleTokenResponse.class);
        GoogleTokenResponse body = responseEntity.getBody();
        return body.getAccessToken();
    }

    @Override
    public SocialType getType() {
        return SocialType.GOOGLE;
    }
}
