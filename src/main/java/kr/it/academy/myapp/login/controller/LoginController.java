package kr.it.academy.myapp.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/form")
    public ModelAndView form(HttpServletRequest request, HttpSession session) {
        // 이전 페이지가 로그인 관련이 아닐 경우만 저장
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/login") && !referer.contains("/logout")) {
            session.setAttribute("prevPage", referer);
        }

        ModelAndView view = new ModelAndView();
        view.setViewName("views/login/form");
        return view;
    }

    @GetMapping("/error")
    public ModelAndView error() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/login/error");
        return view;
    }

    @GetMapping("/logout")
    public ModelAndView logOut(HttpSession session, HttpServletRequest request) {
        session.removeAttribute("userInfo");

        // 이전 페이지 주소 가져오기
        String referer = request.getHeader("Referer");

        ModelAndView view = new ModelAndView();
        if (referer != null && !referer.contains("/login")) {
            view.setViewName("redirect:" + referer);
        } else {
            view.setViewName("redirect:/home");
        }
        return view;
    }
}
