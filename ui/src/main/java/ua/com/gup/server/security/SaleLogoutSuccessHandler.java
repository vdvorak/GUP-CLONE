package ua.com.gup.server.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SaleLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {
    public SaleLogoutSuccessHandler(HttpStatus status) {
        super(status);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        super.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);
    }

}
