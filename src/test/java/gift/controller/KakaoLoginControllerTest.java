package gift.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class KakaoLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void KakaoLoginTest() throws Exception {
        String clientId = "test-client-id";

        mockMvc.perform(get("/kakao/login")
                        .param("clientId", clientId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://kauth.kakao.com/oauth/authorize"
                        + "?response_type=code"
                        + "&client_id=" + clientId
                        + "&redirect_uri=http://localhost:8080"
                        + "&scope=talk_message"));

    }
}

