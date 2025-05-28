package kr.it.academy.myapp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.it.academy.myapp.common.vo.SecureUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        // 기본 경로 설정 (예외적인 경우 사용)
        setDefaultTargetUrl("/home");

        // 세션 유지 시간 설정
        request.getSession().setMaxInactiveInterval(1800); // 30분

        // 사용자 정보를 세션에 저장
        SecureUser user = (SecureUser) authentication.getPrincipal();
        request.getSession().setAttribute("userInfo", user);

        // 로그인 전 요청 저장 객체
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl;

        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();

            // URL이 비정상적이거나 특정 문자열 포함 시 기본경로로 fallback
            if (targetUrl == null || targetUrl.trim().isEmpty()
                    || targetUrl.equals("/") || targetUrl.equals(".")
                    || targetUrl.contains("error")
                    || targetUrl.contains(".well-known")) {
                targetUrl = getDefaultTargetUrl();
            }

        } else {
            // 이전 경로 없음 → 기본 경로로
            targetUrl = getDefaultTargetUrl();
        }

        // 최종 리디렉션
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
