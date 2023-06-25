package hac.controllers;

import hac.InvalidPasswordException;
import hac.UserNotFoundException;
import hac.repo.*;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
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
        User user = userSession.getUser();
        addUserDetails(model, user);
        return "user/profile";
    }

    @PostMapping("/register")
    public String addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/user/register";
        }

        try {
            userService.saveNewUser(user);
        } catch (DataIntegrityViolationException e) { // Handle duplicate userName
            result.rejectValue("email", "error.email", "email already exists");
            model.addAttribute("user", user);
            return "/user/register";
        }

        return "redirect:/user/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user) {

        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser == null)
            throw new UserNotFoundException("User not found");

        if (!BCrypt.checkpw(user.getPassword(), existingUser.getPassword()))
            throw new InvalidPasswordException("Wrong password");

        userSession.setLogin(existingUser);

        userService.createVisit(existingUser);

        return "redirect:/user/profile";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {

        // Retrieve the currently logged-in user
        User user = userSession.getUser();

        // Validate old password and generate massage accordingly
        if (BCrypt.checkpw(oldPassword, user.getPassword())){
            model.addAttribute("success", "password change successfully");
            user.setPassword((BCrypt.hashpw(newPassword, BCrypt.gensalt())));  // Update the user's password
            userService.updateUser(user);
        }
        else
            model.addAttribute("error", "wrong password");

        addUserDetails(model, user);

        return "/user/profile";
    }

    private void addUserDetails(Model model, User user){

        List<Visit> lastVisits = userService.findAllVisitsByEmail(user.getEmail());

        if (lastVisits.size() > 1)
            model.addAttribute("lastVisit", lastVisits.get(lastVisits.size()-2).getLastVisit());
        else
            model.addAttribute("lastVisit", "first visit");

        model.addAttribute("user", user);
    }
}
