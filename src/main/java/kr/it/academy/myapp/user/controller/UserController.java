package kr.it.academy.myapp.user.controller;

import kr.it.academy.myapp.user.service.UserService;
import kr.it.academy.myapp.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public ModelAndView join() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/user/register");
        try{
            List<User.UserAuth> authList = userService.getUserAuthList();
            view.addObject("authList", authList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    @GetMapping("/terms")
    public ModelAndView showTermsPage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/user/terms");
        return view;
    }



}
