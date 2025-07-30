package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtProvider;
import gift.client.KakaoClient;
import gift.domain.Member;
import gift.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KakaoLoginService {

    private final KakaoClient kakaoClient;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public KakaoLoginService(KakaoClient kakaoClient,
                             ObjectMapper objectMapper,
                             MemberRepository memberRepository,
                             JwtProvider jwtProvider) {
        this.kakaoClient = kakaoClient;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String loginAndIssueJwt(String authorizationCode, HttpSession session) {
        String clientId = (String) session.getAttribute("clientId");
        String redirectUri = (String) session.getAttribute("redirectUri");

        String accessTokenJson = kakaoClient.getAccessToken(authorizationCode, clientId, redirectUri);
        String accessToken = extractAccessToken(accessTokenJson);
        session.setAttribute("kakaoAccessToken", accessToken);

        String userInfoJson = kakaoClient.getUserInfo(accessToken);
        String kakaoId = extractKakaoId(userInfoJson);

        String email = "kakao_" + kakaoId + "@email.com";
        String password = UUID.randomUUID().toString();

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, password)));

        return jwtProvider.createToken(member.getId());

    }

    private String extractAccessToken(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            return node.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token JSON", e);
        }
    }

    private String extractKakaoId(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            return node.path("id").asText(); // 카카오 고유 ID
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Kakao ID", e);
        }
    }
}
