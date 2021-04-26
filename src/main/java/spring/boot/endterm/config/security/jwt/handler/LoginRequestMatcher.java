package spring.boot.endterm.config.security.jwt.handler;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class LoginRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        if(httpServletRequest.getRequestURI().contains("/error"))
            return false;
        if(httpServletRequest.getRequestURI().contains("/account"))
            return false;
        return true;

    }
}
