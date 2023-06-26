package hac.controllers;

import hac.repo.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The controller class that handles administrative operations.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserSession userSession;

    /**
     * Displays the admin login page.
     *
     * @param model the model for the view
     * @return the admin login view
     */
    @GetMapping("/login")
    public String adminLogin(Model model) {
        model.addAttribute("user", new User());
        return "admin/login";
    }

    /**
     * Displays the user profiles page for the admin.
     *
     * @param model the model for the view
     * @return the user profiles view
     */
    @GetMapping("/profiles")
    public String showProfiles(Model model) {
        List<User> users = userService.findAllUsers();
        List<Visit> visits = userService.findAllVisits();

        model.addAttribute("users", users);
        model.addAttribute("visits", visits);

        return "admin/profiles";
    }

    /**
     * Handles the admin login request.
     *
     * @param user   the user object containing login credentials
     * @param result the binding result for validation
     * @param model  the model for the view
     * @return redirect to the user profiles page if login is successful, otherwise returns to the login page with an error
     */
    @PostMapping("/login")
    public String loginAdmin(@ModelAttribute("user") User user, BindingResult result, Model model) {
        User admin = userService.findByEmail(userService.getAdminEmail());

        if (!(BCrypt.checkpw(user.getPassword(), admin.getPassword()) && user.getEmail().equals(admin.getEmail()))) {
            result.rejectValue("password", "error.password", "wrong password or username");
            model.addAttribute("user", user);
            return "admin/login";
        }

        // Successful login
        userSession.setLogin(admin);

        // Save last user login
        userService.createVisit(admin);
        return "redirect:/admin/profiles";
    }

    /**
     * Handles the deletion of a user profile by the admin.
     *
     * @param id the ID of the user profile to delete
     * @return redirect to the user profiles page
     */
    @PostMapping("/profiles/delete/{id}")
    public String deleteProfile(@PathVariable("id") Long id) {
        if (id != 1) { // Delete user if it's not the admin
            userService.deleteById(id);
        }

        return "redirect:/admin/profiles";
    }

    /**
     * logout handler
     * @return redirect to the user home page
     */
    @PostMapping("/logout")
    public String logout() {
        userSession.setLogout(userSession.getUser());
        return "redirect:/";
    }
}
