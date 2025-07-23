package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoLoginService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String authorizationCode, HttpSession session) {
        String clientId = (String) session.getAttribute("clientId");
        String redirectUri = (String) session.getAttribute("redirectUri");

        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(URI.create(url), request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                String accessToken = (String) responseMap.get("access_token");
                return "accessToken: " + accessToken;
            } catch (Exception e) {
                throw new RuntimeException("토큰 응답 파싱 중 오류 발생", e);
            }
        } else {
            throw new RuntimeException("카카오 토큰 요청 실패: " + response.getStatusCode() + " - " + response.getBody());
        }
    }
}
