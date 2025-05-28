package kr.it.academy.myapp.login.controller;

import jakarta.servlet.http.HttpSession;
import kr.it.academy.myapp.login.service.LoginService;
import kr.it.academy.myapp.login.vo.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginAPIController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(LoginUser.LoginRequest loginRequest,
                                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            response = loginService.login(loginRequest, session);
            response.put("resultCode", 200);

            // 이전 페이지가 있으면 해당 주소로 리디렉션, 없으면 /home
            String prevPage = (String) session.getAttribute("prevPage");
            response.put("redirectUrl", (prevPage != null) ? prevPage : "/home");
            session.removeAttribute("prevPage"); // 사용 후 삭제

        } catch (Exception e) {
            response.put("resultCode", 500);
            response.put("message", "로그인 실패");
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
