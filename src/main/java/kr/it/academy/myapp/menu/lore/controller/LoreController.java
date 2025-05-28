package kr.it.academy.myapp.menu.lore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/lore")
public class LoreController {

    @GetMapping("/universe")
    public ModelAndView introAbout() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/lore/universe");
        return view;
    }

    @GetMapping("/faction")
    public ModelAndView introCategory() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/lore/faction");
        return view;
    }


}
