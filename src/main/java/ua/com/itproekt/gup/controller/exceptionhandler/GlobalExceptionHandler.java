package ua.com.itproekt.gup.controller.exceptionhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.com.itproekt.gup.service.profile.ProfilesService;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    ProfilesService profilesService;

    private static final Logger logger = Logger.getLogger("globalExceptionHandler");

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<String, Object> handleValidationException(HttpServletRequest request,
                                                                       Principal principal,
                                                                       MethodArgumentNotValidException ex) {

        String userEmail = (principal == null ? "NULL" : principal.getName());
        StringWriter stack = new StringWriter();

        ex.printStackTrace(new PrintWriter(stack)); // **********

        logger.error("UncaughtException Occured:: URL=" + request.getRequestURL() + ";" +
                "   User email: " + userEmail + ";" +
                "   Exception: " + stack.toString());

        Map<String, Object>  map = new HashMap<>();
        map.put("error", "Validation Failure");
        map.put("violations", convertConstraintViolation(ex));
        return map;
    }

    private Map<String, Map<String, Object> > convertConstraintViolation(MethodArgumentNotValidException ex) {
        Map<String, Map<String, Object> > result = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            Map<String, Object>  violationMap = new HashMap<>();
            violationMap.put("target", ex.getBindingResult().getTarget());
            violationMap.put("type", ex.getBindingResult().getTarget().getClass());
            violationMap.put("message", error.getDefaultMessage());
            result.put(error.getObjectName(), violationMap);
        }
        return result;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String  handleAccessDeniedException(HttpServletRequest request,
                                                                     Principal principal,
                                                                     Exception ex) {
        String userEmail = (principal == null ? "NULL" : principal.getName());
        StringWriter stack = new StringWriter();

        ex.printStackTrace(new PrintWriter(stack)); // **********

        logger.error("AccessDeniedException Occured:: URL=" + request.getRequestURL() + ";" +
                "   User email: " + userEmail + ";" +
                "   Exception: " + stack.toString());



        return "error/403";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public String handleAuthCredentialsNotFoundEx(HttpServletRequest request, Principal principal, Exception ex) {
        String userEmail = (principal == null ? "NULL" : principal.getName());
        StringWriter stack = new StringWriter();
        ex.printStackTrace(new PrintWriter(stack)); // **********

        logger.error("AuthenticationCredentialsNotFoundException Occured:: URL=" + request.getRequestURL() + ";" +
                "   User email: " + userEmail + ";" +
                "   Exception: " + stack.toString());

        return "error/401";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleUncaughtException(HttpServletRequest request, Principal principal, Exception ex) {
        String userEmail = (principal == null ? "NULL" : principal.getName());
        StringWriter stack = new StringWriter();
        ex.printStackTrace(new PrintWriter(stack)); // **********

        logger.error("UncaughtException Occured:: URL=" + request.getRequestURL() + ";" +
                "   User email: " + userEmail + ";" +
                "   Exception: " + stack.toString());

        ////////////////////////////////////////////////////////////////
        System.err.println("error : Unknown Error");
        System.err.println("class : " + ex.getClass());
        System.err.println(" : " + ex.getClass());
        if (ex.getCause() != null) {
            System.err.println("cause : " + ex.getCause().getMessage());
        } else {
            System.err.println("cause :" + ex.getMessage());
        }

        return "error/error";
    }

}
