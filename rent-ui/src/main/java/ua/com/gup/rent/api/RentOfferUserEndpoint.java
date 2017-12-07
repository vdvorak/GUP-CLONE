package ua.com.gup.rent.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(path = "/api/users")
public class RentOfferUserEndpoint {

    @Autowired
    private Environment e;


    @GetMapping(path = "/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        String referrer = request.getHeader("referer");
        return !StringUtils.isEmpty(referrer) ? "redirect:" + referrer : "redirect:" + e.getRequiredProperty("application.url");
    }

    @GetMapping(path = "/current")
    public ResponseEntity getPrincipal() throws IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        if ("anonymousUser".equals(principal)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(principal, HttpStatus.OK);
    }
}
