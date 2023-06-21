package hac.controllers;

import hac.repo.*;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitRepository visitRepository;

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

    @GetMapping("/profile")
    public String showProfiles(Model model) {

        if (userSession == null || !userSession.isAuthenticated())
            return "redirect:/user/login";

        User user = userSession.getUser();

        System.out.println(user);

        List<Visit> lastVisits = visitRepository.findLatestByEmail(user.getEmail());

        if (lastVisits.size() > 1)
            model.addAttribute("lastVisit", lastVisits.get(0).getLastVisit());
        else
            model.addAttribute("lastVisit", "first visit");

        model.addAttribute("user", user);

        return "user/profile";
    }

    @PostMapping("/register")
    public String addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/user/register";
        }

        try {
            userRepository.save(new User(user.getUserName(), user.getEmail(), user.getPassword()));
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

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            result.rejectValue("email", "error.email", "user not found");
            model.addAttribute("user", user);
            return "/user/login";
        }

        if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword())) {
            result.rejectValue("password", "error.password", "wrong password");
            model.addAttribute("user", user);
            return "/user/login";
        }

        userSession.setLogin(existingUser);

        LocalDateTime currentDate = LocalDateTime.now();
        Visit visit = new Visit(user.getEmail(), currentDate);
        visitRepository.save(visit);

        return "redirect:/user/profile";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {

        // Retrieve the currently logged-in user
        User user = userSession.getUser();

        // Validate the old password

        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            //model.addAttribute("error", "Incorrect old password");
            System.out.println("wronggggggg");
            return "redirect:/user/profile";
        }

        // Update the user's password
        user.setPassword((BCrypt.hashpw(newPassword, BCrypt.gensalt())));
        userRepository.save(user);

        // Redirect to a success page or display a success message
        return "redirect:/user/profile";
    }
}
