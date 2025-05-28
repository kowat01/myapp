package kr.it.academy.myapp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AfterLogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        // ✅ 세션 정보 제거
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("userInfo");
            session.invalidate();
        }

        // ✅ 리퍼러 검사
        String referer = request.getHeader("Referer");

        if (referer == null || referer.trim().isEmpty() || referer.equals(".") || referer.equals("/")) {
            response.sendRedirect("/home");
        } else if (referer.contains("/board")) {
            response.sendRedirect("/home");
        } else {
            response.sendRedirect(referer);
        }
    }
}
