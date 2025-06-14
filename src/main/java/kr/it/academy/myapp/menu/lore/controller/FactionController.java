package kr.it.academy.myapp.menu.lore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/lore/faction")
public class FactionController {

    // ===== Imperial Factions =====
    @GetMapping("/spacemarine")
    public ModelAndView spacemarine() {
        return new ModelAndView("views/lore/faction/spacemarine");
    }

    @GetMapping("/darkangel")
    public ModelAndView darkangel() {
        return new ModelAndView("views/lore/faction/darkangel");
    }

    @GetMapping("/bloodangel")
    public ModelAndView bloodangel() {
        return new ModelAndView("views/lore/faction/bloodangel");
    }

    @GetMapping("/spacewolf")
    public ModelAndView spacewolf() {
        return new ModelAndView("views/lore/faction/spacewolf");
    }

    @GetMapping("/blacktemplar")
    public ModelAndView blacktemplar() {
        return new ModelAndView("views/lore/faction/blacktemplar");
    }

    @GetMapping("/deathwatch")
    public ModelAndView deathwatch() {
        return new ModelAndView("views/lore/faction/deathwatch");
    }

    @GetMapping("/greyknight")
    public ModelAndView greyknight() {
        return new ModelAndView("views/lore/faction/greyknight");
    }

    @GetMapping("/imperialguard")
    public ModelAndView imperialguard() {
        return new ModelAndView("views/lore/faction/imperialguard");
    }

    @GetMapping("/imperialagent")
    public ModelAndView imperialagent() {
        return new ModelAndView("views/lore/faction/imperialagent");
    }

    @GetMapping("/mechanicus")
    public ModelAndView mechanicus() {
        return new ModelAndView("views/lore/faction/mechanicus");
    }

    @GetMapping("/sistersofbattle")
    public ModelAndView sistersofbattle() {
        return new ModelAndView("views/lore/faction/sistersofbattle");
    }

    @GetMapping("/imperialknight")
    public ModelAndView imperialknight() {
        return new ModelAndView("views/lore/faction/imperialknight");
    }

    @GetMapping("/custodes")
    public ModelAndView custodes() {
        return new ModelAndView("views/lore/faction/custodes");
    }

    @GetMapping("/titanlegion")
    public ModelAndView titanlegion() {
        return new ModelAndView("views/lore/faction/titanlegion");
    }

    // ===== Chaos Factions =====
    @GetMapping("/demons")
    public ModelAndView demons() {
        return new ModelAndView("views/lore/faction/demons");
    }

    @GetMapping("/chaosmarine")
    public ModelAndView chaosmarine() {
        return new ModelAndView("views/lore/faction/chaosmarine");
    }

    @GetMapping("/worldeater")
    public ModelAndView worldeater() {
        return new ModelAndView("views/lore/faction/worldeater");
    }

    @GetMapping("/deathguard")
    public ModelAndView deathguard() {
        return new ModelAndView("views/lore/faction/deathguard");
    }

    @GetMapping("/thousandson")
    public ModelAndView thousandson() {
        return new ModelAndView("views/lore/faction/thousandson");
    }

    @GetMapping("/emperorschildren")
    public ModelAndView emperorschildren() {
        return new ModelAndView("views/lore/faction/emperorschildren");
    }

    @GetMapping("/chaosknight")
    public ModelAndView chaosknight() {
        return new ModelAndView("views/lore/faction/chaosknight");
    }

    // ===== Xeno Factions =====
    @GetMapping("/eldar")
    public ModelAndView eldar() {
        return new ModelAndView("views/lore/faction/eldar");
    }

    @GetMapping("/darkeldar")
    public ModelAndView darkeldar() {
        return new ModelAndView("views/lore/faction/darkeldar");
    }

    @GetMapping("/necron")
    public ModelAndView necron() {
        return new ModelAndView("views/lore/faction/necron");
    }

    @GetMapping("/orks")
    public ModelAndView orks() {
        return new ModelAndView("views/lore/faction/orks");
    }

    @GetMapping("/tau")
    public ModelAndView tau() {
        return new ModelAndView("views/lore/faction/tau");
    }

    @GetMapping("/tyranid")
    public ModelAndView tyranid() {
        return new ModelAndView("views/lore/faction/tyranid");
    }

    @GetMapping("/geanstealercult")
    public ModelAndView geanstealercult() {
        return new ModelAndView("views/lore/faction/geanstealercult");
    }

    @GetMapping("/votann")
    public ModelAndView votann() {
        return new ModelAndView("views/lore/faction/votann");
    }
}
