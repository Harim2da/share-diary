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
import share_diary.diray.auth.oauth.token.GithubTokenRequest;
import share_diary.diray.auth.oauth.token.GithubTokenResponse;
import share_diary.diray.auth.oauth.userInfo.GithubUserInfoResponse;
import share_diary.diray.auth.oauth.userInfo.OAuthUserInfoResponse;
import share_diary.diray.member.domain.Member;

@Getter
@Component
public class GithubOAuthManager implements OAuthManager{

    @JsonProperty(value = "client_id")
    @Value("${github.client.id}")
    private String clientId;

    @JsonProperty(value = "client_secret")
    @Value("${github.client.secret}")
    private String secret;

    @Value("${github.url.access-token}")
    private String accessTokenUrl;

    @Value("${github.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public GithubOAuthManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Member getMemberInfo(String code){
        UriComponents uri = UriComponentsBuilder
                .fromUriString(profileUrl)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + getAccessToken(code));

        HttpEntity request = new HttpEntity(httpHeaders);

        ResponseEntity<GithubUserInfoResponse> response =
                restTemplate.exchange(uri.toString(), HttpMethod.GET, request, GithubUserInfoResponse.class);

        OAuthUserInfoResponse user = response.getBody();
        return user.toMember();
    }

    @Override
    public String getAccessToken(String code){

        GithubTokenRequest requestEntity = GithubTokenRequest.builder()
                .clientId(clientId)
                .secret(secret)
                .code(code)
                .build();

        UriComponents uri = UriComponentsBuilder
                .fromUriString(accessTokenUrl)
                .build();

        ResponseEntity<GithubTokenResponse> responseEntity =
                restTemplate.postForEntity(uri.toString(), requestEntity, GithubTokenResponse.class);

        return responseEntity.getBody()
                .getAccessToken();
    }

    @Override
    public SocialType getType(){
        return SocialType.GITHUB;
    }
}
