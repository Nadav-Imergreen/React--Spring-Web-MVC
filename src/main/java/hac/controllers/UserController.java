package hac.controllers;

import hac.repo.User;
import hac.repo.UserRepository;
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

@Controller
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult result, Model model) {

       // validate the object and get the errors
        if (result.hasErrors()) {
            // errors will be displayed in the view
            System.out.println("validation errors: " + result.getAllErrors());
            model.addAttribute("user", user);
            return "register";
        }

        try {
            repository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Handle constraint violation error (duplicate userName)
            result.rejectValue("userName", "error.userName", "Username already exists");
            model.addAttribute("user", user);
            return "register";
        }

       repository.save(user);
        return "login";
    }

    @PostMapping("/loginUser")
    public String loginUser(@ModelAttribute("user") User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("loginError", result.getAllErrors());
            return "login";
        }

        User existingUser = repository.findByUserName(user.getUserName());
        System.out.println(existingUser);

        if (existingUser == null) {
            result.rejectValue("userName", "error.userName", "user not found");
            model.addAttribute("user", user);
            System.out.println("in not-found");
            return "login";
        }
        if (!user.getPassword().equals(existingUser.getPassword())) {
            result.rejectValue("password", "error.password", "wrong password");
            model.addAttribute("user", user);
            System.out.println("in wrong password");
            return "login";
        }

        // Successful login

        List<User> users = repository.findAll();
        System.out.println(users);
        model.addAttribute("users", users);
        System.out.println("in secssess");
        return "users";
    }

    @GetMapping("/users")
    public String showProfile(Model model) {
        List<User> users = repository.findAll();
        System.out.println(users);
        model.addAttribute("users", users);
        return "users";
    }
}
