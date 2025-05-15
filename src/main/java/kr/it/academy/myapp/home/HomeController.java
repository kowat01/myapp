package kr.it.academy.myapp.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class HomeController {

@RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView view = new ModelAndView();
        view.addObject("msg", "안녕2");
        view.setViewName("views/home");

        return view;
    }
}
