package hac.controllers;

import hac.repo.User;
import hac.repo.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final String ADMIN_USERNAME = "admin@gmail.com";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin@gmail.com";

    @Autowired
    private UserRepository repository;

    @EventListener
    public void registerAdmin(ApplicationEvent event) {
        if (repository.findByUserName(ADMIN_USERNAME) == null) {
            repository.save(new User(ADMIN_USERNAME, ADMIN_USERNAME, "1234"));
        }
    }

    @GetMapping("/")
    public String login() {return "welcome";}

    @GetMapping("/adminLogin")
    public String adminLogin(Model model) {
        model.addAttribute("user", new User());
        return "admin/login";
    }

    @GetMapping("/userLogin")
    public String userLogin(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult result, Model model) {

       // validate the object and get the errors
        if (result.hasErrors()) {
            // errors will be displayed in the view
            model.addAttribute("user", user);
            return "user/register";
        }

        try {
            repository.save(user);
        } catch (DataIntegrityViolationException e) { // Handle duplicate userName
            result.rejectValue("userName", "error.userName", "Username already exists");
            model.addAttribute("user", user);
            return "user/register";
        }

       repository.save(user);
        return "user/login";
    }

    @PostMapping("/loginUser")
    public String loginUser(@ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("loginError", result.getAllErrors());
            return "user/login";
        }
        // extract user from table
        User existingUser = repository.findByUserName(user.getUserName());

        if (existingUser == null) {
            result.rejectValue("userName", "error.userName", "user not found");
            model.addAttribute("user", user);
            return "user/login";
        }
        if (!user.getPassword().equals(existingUser.getPassword())) {
            result.rejectValue("password", "error.password", "wrong password");
            model.addAttribute("user", user);
            return "user/login";
        }


        // Successful login
        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "user/profiles";
    }

    @PostMapping("/loginAdmin")
    public String loginAdmin(@ModelAttribute("user") User user, BindingResult result, Model model) {

        if (!(user.getPassword().equals(ADMIN_PASSWORD) && user.getUserName().equals(ADMIN_USERNAME))) {
            result.rejectValue("password", "error.password", "wrong password or userName");
            model.addAttribute("user", user);
            return "admin/login";
        }

        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "admin/profiles";
    }


        @GetMapping("/admin/profiles")
    public String editProfiles(Model model) {
        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "admin/profiles";
    }

    @GetMapping("/user/profiles")
    public String showProfiles(Model model) {
        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "user/profiles";
    }
}
