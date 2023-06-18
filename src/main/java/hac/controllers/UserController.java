package hac.controllers;

import hac.repo.User;
import hac.repo.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/")
    public String index(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "index";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user, BindingResult result, Model model) {

        System.out.println(user.getPassword());

//        // validate the object and get the errors
        if (result.hasErrors()) {
            // errors will be displayed in the view
            System.out.println("validation errors: " + result.getAllErrors());
            model.addAttribute("user", user);
            return "index";
        }

       repository.save(user);
//
//       //  pass the list of users to the view
//        model.addAttribute("user", user);
        System.out.print("in addUser");
        return "index";
    }
}
