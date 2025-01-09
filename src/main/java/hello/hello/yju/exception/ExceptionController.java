package hello.hello.yju.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(StudySellException.class)
    public ModelAndView entityNotFoundException(StudySellException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("message", ex.getMessage());
        mav.addObject("statusCode", ex.getStatusCode());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView allException(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error/500");
        model.addAttribute("message", ex.getMessage());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}
