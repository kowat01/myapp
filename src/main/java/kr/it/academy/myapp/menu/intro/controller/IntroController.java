package kr.it.academy.myapp.menu.intro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/intro")
public class IntroController {

    @GetMapping("/about")
    public ModelAndView introAbout() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/intro/about");
        return view;
    }

    @GetMapping("/category")
    public ModelAndView introCategory() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/intro/category");
        return view;
    }

    @GetMapping("/need")
    public ModelAndView introNeed() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/intro/need");
        return view;
    }
}
