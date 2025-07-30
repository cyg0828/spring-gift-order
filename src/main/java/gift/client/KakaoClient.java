package gift.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoClient {

    private final WebClient webClient;

    public KakaoClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    public String getAccessToken(String code, String clientId, String redirectUri) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        try {
            return webClient.post()
                    .uri("/oauth/token")
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new KakaoClientException("카카오 토큰 요청 중 오류 발생", e);
        }
    }

    public String getUserInfo(String accessToken) {
        try {
            return webClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new KakaoClientException("카카오 사용자 정보 요청 중 오류 발생", e);
        }
    }
}
