package gift.service;

import gift.client.KakaoClient;
import gift.client.KakaoClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class KakaoLoginServiceTest {

    @Mock
    private KakaoClient kakaoClient;

    @InjectMocks
    private KakaoLoginService kakaoLoginService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        session = new MockHttpSession();
        session.setAttribute("clientId", "test-client-id");
        session.setAttribute("redirectUri", "http://localhost:8080");
    }

    @Test
    void getAccessToken_success() {
        String code = "auth-code";
        String expectedToken = "access-token";

        when(kakaoClient.getAccessToken(code, "test-client-id", "http://localhost:8080"))
                .thenReturn(expectedToken);

        String actual = kakaoLoginService.getAccessToken(code, session);

        assertEquals(expectedToken, actual);
    }

    @Test
    void getAccessToken_fail() {
        String code = "auth-code";

        when(kakaoClient.getAccessToken(code, "test-client-id", "http://localhost:8080"))
                .thenThrow(new KakaoClientException("실패", new RuntimeException()));

        assertThrows(KakaoClientException.class, () -> {
            kakaoLoginService.getAccessToken(code, session);
        });
    }
}
