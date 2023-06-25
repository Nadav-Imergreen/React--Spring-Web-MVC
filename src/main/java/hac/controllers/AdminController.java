package hac.controllers;

import hac.repo.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserSession userSession;

    @GetMapping("/login")
    public String adminLogin(Model model) {
        model.addAttribute("user", new User());
        return "admin/login";
    }

    @GetMapping("/profiles")
    public String showProfiles(Model model) {

        List<User> users = userService.findAllUsers();
        List<Visit> visits = userService.findAllVisits();

        model.addAttribute("users", users);
        model.addAttribute("visits", visits);

        return "admin/profiles";
    }

    @PostMapping("/login")
    public String loginAdmin(@ModelAttribute("user") User user, BindingResult result, Model model) {

        User admin = userService.findByEmail(userService.getAdminEmail());

        if (!(BCrypt.checkpw(user.getPassword(), admin.getPassword()) && user.getEmail().equals(admin.getEmail()))){
            result.rejectValue("password", "error.password", "wrong password or userName");
            model.addAttribute("user", user);
            return "admin/login";
        }

        // successful login
        userSession.setLogin(admin);

        // save last user login
        userService.createVisit(admin);
        return "redirect:/admin/profiles";
    }

    @PostMapping("/profiles/delete/{id}")
    public String deleteProfile(@PathVariable("id") Long id) {

        if(id != 1)  // delete user if it's nut admin
            userService.deleteById(id);

        return "redirect:/admin/profiles";
    }
}