package hac.controllers;

import hac.repo.User;
import hac.repo.UserRepository;
import hac.repo.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserSession userSession;

    @EventListener
    public void registerAdmin(ApplicationEvent event) {
        if (repository.findByEmail(ADMIN_EMAIL) == null)
            repository.save(new User(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD));
    }

    @GetMapping("/login")
    public String adminLogin(Model model) {
        model.addAttribute("user", new User());
        return "admin/login";
    }

    @GetMapping("/profiles")
    public String showProfiles(Model model) {

        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/admin/login";
        }

        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "admin/profiles";
    }

    @PostMapping("/login")
    public String loginAdmin(@ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("loginError", result.getAllErrors());
            return "user/login";
        }

        if (!(user.getPassword().equals(ADMIN_PASSWORD) && user.getEmail().equals(ADMIN_EMAIL))) {
            result.rejectValue("password", "error.password", "wrong password or userName");
            model.addAttribute("user", user);
            return "admin/login";
        }

        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        userSession.setLogin();
        return "redirect:/admin/profiles";
    }

//    @PostMapping("/profiles/update/{id}/{newName}")
//    public String updateProfile(@PathVariable("id") Long id, @PathVariable("newName") String newName, Model model) {
//        User user = repository.findById(id).orElse(null);
//        if (user != null) {
//            user.setUserName(newName);
//            repository.save(user);
//        }
//        return showProfiles(model);
//    }

    @PostMapping("/profiles/update")
    public String updateProfile(@ModelAttribute("user") User user, @RequestParam("newName") String newName, Model model) {
        user = repository.findById(user.getId()).orElse(null);
        System.out.println("newName issssssssssssß: " + newName);
        if (user != null) {
            user.setUserName(newName);
            repository.save(user);
        }
        return "redirect:/admin/profiles";
    }

    @PostMapping("/profiles/delete/{id}")
    public String deleteProfile(@PathVariable("id") Long id,  Model model) {
        repository.deleteById(id);
        return "redirect:/admin/profiles";
    }
}
