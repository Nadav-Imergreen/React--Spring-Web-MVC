package hac.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MainController {
    @RequestMapping("/")
    public String Welcome() {
        return "welcome";
    }

    /** Error page that displays all exceptions. */

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping("/error-page")
    public String error(Exception ex, Model model) {

        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    /** simple Error page. */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }
}


