package gift.controller;

import gift.service.KakaoLoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView redirectToKakaoAuth(@RequestParam String clientId, HttpSession session) {

        session.setAttribute("clientId", clientId);
        session.setAttribute("redirectUri", "http://localhost:8080");

        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=http://localhost:8080"
                + "&scope=talk_message";
        return new RedirectView(kakaoAuthUrl);
    }

    @GetMapping
    @ResponseBody
    public String redirectToKakaoLogin(@RequestParam String code, HttpSession session) {
        return kakaoLoginService.getAccessToken(code, session);
    }



}
