package hac.controllers;

import hac.repo.User;
import hac.repo.UserRepository;
import hac.repo.UserSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserSession userSession;

    @GetMapping("/login")
    public String userLogin(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @GetMapping("/profiles")
    public String showProfiles(Model model) {

        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/user/login";
        }

        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "user/profiles";
    }

    @PostMapping("/register")
    public String addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/user/register";
        }

        try {
            repository.save(user);
        } catch (DataIntegrityViolationException e) { // Handle duplicate userName
            result.rejectValue("email", "error.email", "email already exists");
            model.addAttribute("user", user);
            return "/user/register";
        }

        return "redirect:/user/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("loginError", "error");
            return "/user/login";
        }

        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser == null) {
            result.rejectValue("email", "error.email", "user not found");
            model.addAttribute("user", user);
            return "/user/login";
        }
        if (!user.getPassword().equals(existingUser.getPassword())) {
            result.rejectValue("password", "error.password", "wrong password");
            model.addAttribute("user", user);
            return "/user/login";
        }

        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        userSession.setLogin();
        return "redirect:/user/profiles";
    }
}
