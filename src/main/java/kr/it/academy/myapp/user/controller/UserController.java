package kr.it.academy.myapp.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.it.academy.myapp.common.vo.SecureUser;
import kr.it.academy.myapp.user.service.UserService;
import kr.it.academy.myapp.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/register")
    public ModelAndView join() {
        ModelAndView view = new ModelAndView("views/user/register");
        try {
            List<User.UserAuth> authList = userService.getUserAuthList();
            view.addObject("authList", authList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    // 약관 동의 페이지
    @GetMapping("/terms")
    public ModelAndView showTermsPage() {
        return new ModelAndView("views/user/terms");
    }

    // 마이페이지
    @GetMapping("/mypage")
    public ModelAndView myPage(@AuthenticationPrincipal SecureUser loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        try {
            String userId = loginUser.getUserId(); // ✔ 정확히 user_info.user_id 로 조회
            User.Response userInfo = userService.getUserById(userId);

            ModelAndView view = new ModelAndView("views/user/mypage");
            view.addObject("userInfo", userInfo); // ✔ 정상 전달
            return view;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보를 불러올 수 없습니다.", e);
        }
    }

    @GetMapping("/mypageedit")
    public ModelAndView editForm(@AuthenticationPrincipal SecureUser loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        try {
            String userId = loginUser.getUserId();
            User.Response userInfo = userService.getUserById(userId);

            ModelAndView view = new ModelAndView("views/user/mypageedit");
            view.addObject("userInfo", userInfo);
            return view;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보를 불러올 수 없습니다.", e);
        }
    }

    @PostMapping("/mypageedit")
    public String updateUser(@AuthenticationPrincipal SecureUser loginUser,
                             @RequestParam String nickname,
                             @RequestParam String email) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        try {
            userService.updateUserInfo(loginUser.getUserId(), nickname, email);
            return "redirect:/user/mypage"; // 수정 후 마이페이지로 이동
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보 수정에 실패했습니다.", e);
        }
    }

}
