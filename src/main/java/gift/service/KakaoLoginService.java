package gift.service;

import gift.client.KakaoClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class KakaoLoginService {

    private final KakaoClient kakaoClient;

    public KakaoLoginService(KakaoClient kakaoClient) {
        this.kakaoClient = kakaoClient;
    }

    public String getAccessToken(String authorizationCode, HttpSession session) {
        String clientId = (String) session.getAttribute("clientId");
        String redirectUri = (String) session.getAttribute("redirectUri");

        return kakaoClient.requestAccessToken(authorizationCode, clientId, redirectUri);
    }
}
