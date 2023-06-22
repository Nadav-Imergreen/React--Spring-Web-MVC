package hac.controllers;

import hac.repo.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private UserSession userSession;

    @EventListener({ApplicationStartedEvent.class})
    public void registerAdmin() {
        if (userRepository.findByEmail(ADMIN_EMAIL) == null)
            userRepository.save(new User(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD));
    }

    @GetMapping("/login")
    public String adminLogin(Model model) {
        model.addAttribute("user", new User());
        return "admin/login";
    }

    @GetMapping("/profiles")
    public String showProfiles(Model model) {

//        if (userSession == null || !userSession.isAuthenticated()) {
//            return "redirect:/admin/login";
//        }

        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);

        List<Visit> visits = visitRepository.findAll();
        model.addAttribute("visits", visits);

        return "admin/profiles";
    }

    @PostMapping("/login")
    public String loginAdmin(@ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("loginError", result.getAllErrors());
            return "user/login";
        }

        User admin = userRepository.findByEmail(ADMIN_EMAIL);

        if (!BCrypt.checkpw(user.getPassword(), admin.getPassword()) && user.getEmail().equals(ADMIN_EMAIL)) {
            result.rejectValue("password", "error.password", "wrong password or userName");
            model.addAttribute("user", user);
            return "admin/login";
        }

        LocalDateTime currentDate = LocalDateTime.now();
        Visit visit = new Visit(user.getEmail(), currentDate);
        visitRepository.save(visit);

        userSession.setLogin(user);
        return "redirect:/admin/profiles";
    }

    @PostMapping("/profiles/delete/{id}")
    public String deleteProfile(@PathVariable("id") Long id) {

        if(id != 1)  // delete user if it's nut admin
            userRepository.deleteById(id);

        return "redirect:/admin/profiles";
    }
}