package hac.controllers;

import hac.InvalidPasswordException;
import hac.UserNotFoundException;
import hac.repo.User;
import hac.repo.UserService;
import hac.repo.UserSession;
import hac.repo.Visit;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class for handling user-related routes and actions.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession userSession;

    /**
     * Handles the user login route ("/user/login") and returns the user login view.
     *
     * @param model the model for the view
     * @return the user login view
     */
    @GetMapping("/login")
    public String userLogin(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    /**
     * Handles the user registration route ("/user/register") and returns the user registration view.
     *
     * @param model the model for the view
     * @return the user registration view
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    /**
     * Handles the user profile route ("/user/profile") and returns the user profile view.
     *
     * @param model the model for the view
     * @return the user profile view
     */
    @GetMapping("/profile")
    public String showProfiles(Model model) {
        User user = userSession.getUser();
        addUserDetails(model, user);
        return "user/profile";
    }

    /**
     * Handles the user registration form submission and creates a new user.
     * Redirects to the user login view if successful, or returns the user registration view with validation errors.
     *
     * @param user   the user to be registered
     * @param result the binding result for validation
     * @param model  the model for the view
     * @return the user login view or the user registration view with validation errors
     */
    @PostMapping("/register")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/user/register";
        }

        try {
            userService.saveNewUser(user);
        } catch (DataIntegrityViolationException e) { // Handle duplicate email
            result.rejectValue("email", "error.email", "email already exists");
            model.addAttribute("user", user);
            return "/user/register";
        }

        return "redirect:/user/login";
    }

    /**
     * Handles the user login form submission and performs user authentication.
     * Redirects to the user profile view if successful.
     * Throws a UserNotFoundException or InvalidPasswordException if the login is unsuccessful.
     *
     * @param user the user login credentials
     * @return the user profile view
     * @throws UserNotFoundException    if the user is not found
     * @throws InvalidPasswordException if the password is invalid
     */
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

    /**
     * Handles the user password change form submission and updates the user's password.
     * Redirects to the user profile view with a success or error message.
     *
     * @param oldPassword the old password
     * @param newPassword the new password
     * @param model       the model for the view
     * @return the user profile view
     */
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        User user = userSession.getUser();

        if (BCrypt.checkpw(oldPassword, user.getPassword())) {
            model.addAttribute("success", "Password changed successfully");
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userService.updateUser(user);
        } else {
            model.addAttribute("error", "Wrong password");
        }

        addUserDetails(model, user);

        return "/user/profile";
    }

    /**
     * Helper method to add user details to the model.
     *
     * @param model the model for the view
     * @param user  the user object
     */
    private void addUserDetails(Model model, User user) {
        List<Visit> lastVisits = userService.findAllVisitsByEmail(user.getEmail());

        if (lastVisits.size() > 1) {
            model.addAttribute("lastVisit", lastVisits.get(lastVisits.size() - 2).getLastVisit());
        } else {
            model.addAttribute("lastVisit", "first visit");
        }

        model.addAttribute("user", user);
    }
}
