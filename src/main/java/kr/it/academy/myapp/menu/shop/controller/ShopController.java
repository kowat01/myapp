package kr.it.academy.myapp.menu.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @GetMapping("/official")
    public ModelAndView officialShopList() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/shop/official");
        return view;
    }

    @GetMapping("/used")
    public ModelAndView usedShopList() {
        ModelAndView view = new ModelAndView();
        view.setViewName("views/shop/used");
        return view;
    }
}