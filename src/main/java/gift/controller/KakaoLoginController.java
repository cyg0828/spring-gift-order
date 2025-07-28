package gift.controller;

import gift.service.KakaoLoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class KakaoLoginController {

    KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("kakao/form")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("kakao/login")
    public String redirectToKakaoAuth(@RequestParam String clientId, HttpSession session) {

        session.setAttribute("clientId", clientId);
        session.setAttribute("redirectUri", "http://localhost:8080");

        String kakaoAuthUrl = UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", "http://localhost:8080")
                .queryParam("scope", "talk_message")
                .build()
                .toUriString();

        return "redirect:" + kakaoAuthUrl;
    }

    @GetMapping
    @ResponseBody
    public String redirectToKakaoLogin(@RequestParam String code, HttpSession session) {
        return kakaoLoginService.getAccessToken(code, session);
    }



}
