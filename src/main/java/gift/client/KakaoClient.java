package gift.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .build();

    public String requestAccessToken(String code, String clientId, String redirectUri) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String response = restClient.post()
                .uri("/oauth/token")
                .headers(h -> h.addAll(headers))
                .body(body)
                .retrieve()
                .body(String.class);

        return response;
    }
}
