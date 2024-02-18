package share_diary.diray.auth.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import share_diary.diray.auth.oauth.token.KakaoTokenRequest;
import share_diary.diray.auth.oauth.token.KakaoTokenResponse;
import share_diary.diray.auth.oauth.userInfo.KakaoUserInfoResponse;
import share_diary.diray.member.domain.Member;

import java.nio.charset.Charset;

@Component
public class KakaoOAuthManager implements OAuthManager {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.url.access-token}")
    private String code;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.grant-type}")
    private String grantType;

    @Value("${kakao.url.access-token}")
    private String accessTokenUrl;

    @Value("${kakao.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public KakaoOAuthManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Member getMemberInfo(String code) {
        UriComponents uri = UriComponentsBuilder
                .fromUriString(profileUrl)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(code));

        HttpEntity<Object> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<KakaoUserInfoResponse> response =
                restTemplate.exchange(uri.toString(), HttpMethod.GET, request, KakaoUserInfoResponse.class);

        KakaoUserInfoResponse body = response.getBody();
        return body.toMember();
    }

    @Override
    public String getAccessToken(String code) {

        //x-www-formdata-urlencoded 는 MultiValueMap 을 사용해야 한다.
        //https://okky.kr/questions/717506
        MultiValueMap<String,String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("client_id",clientId);
        requestEntity.add("client_secret",clientSecret);
        requestEntity.add("code",code);
        requestEntity.add("redirect_uri",redirectUri);
        requestEntity.add("grant_type",grantType);

        UriComponents uri = UriComponentsBuilder
                .fromUriString(accessTokenUrl)
                .build();

        //header 생성 : application/x-www-form-urlencoded;charset=utf-8
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //HttpEntity 생성
        HttpEntity<Object> entity = new HttpEntity<>(requestEntity,headers);

        ResponseEntity<KakaoTokenResponse> responseEntity =
                restTemplate.postForEntity(uri.toString(), entity, KakaoTokenResponse.class);
        KakaoTokenResponse body = responseEntity.getBody();
        return body.getAccessToken();
    }

    @Override
    public SocialType getType() {
        return SocialType.KAKAO;
    }
}
