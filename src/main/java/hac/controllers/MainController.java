package hac.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller class for handling main routes and error pages.
 */
@Controller
public class MainController {

    /**
     * Handles the root route ("/") and returns the welcome view.
     *
     * @return the welcome view
     */
    @RequestMapping("/")
    public String Welcome() {
        return "welcome";
    }

    /**
     * Error page that displays all exceptions.
     * Handles any Exception and returns the error view with the error message.
     *
     * @param ex    the Exception instance
     * @param model the model for the view
     * @return the error view with the error message
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping("/error-page")
    public String error(Exception ex, Model model) {
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    /**
     * Handles the forbidden route ("/403") and returns the forbidden view.
     *
     * @return the forbidden view
     */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }
}
