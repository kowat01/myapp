package kr.it.academy.myapp.login.service;

import jakarta.servlet.http.HttpSession;
import kr.it.academy.myapp.login.mapper.LoginMapper;
import kr.it.academy.myapp.login.vo.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Map<String, Object> login(LoginUser.LoginRequest loginRequest, HttpSession session) throws Exception {
        Map<String, Object> result = new HashMap<>();
        LoginUser.UserInfo user = loginMapper.login(loginRequest);

        if (user ==null || !passwordEncoder.matches(loginRequest.getPasswd(), user.getPasswd())){
            throw new Exception("로그인 실패");
        }

        boolean isAuth = user.getAuthList()
                .stream()
                .allMatch(auth -> auth.getAuthId().equals("ADMIN"));

        //패스워드 숨기기
        LoginUser.UserInfo logined = LoginUser.UserInfo
                .builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .nickname(user.getNickname())
                .authList(user.getAuthList())
                .isAuth(isAuth)
                .build();


        //세션 저장
        session.setAttribute("userInfo", logined);
        session.setMaxInactiveInterval(1800);
        result.put("resultCode", 200);
        return result;
    }

}
