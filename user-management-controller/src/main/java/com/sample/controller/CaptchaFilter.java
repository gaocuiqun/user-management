package com.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CaptchaFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(CaptchaFilter.class);

    private AuthenticationFailureHandler failureHandler;

    public CaptchaFilter(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if("/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {

            String captcha = request.getParameter("captcha");
            String ssnCaptcha = LoginController.getToken(request.getSession());
            if (captcha != null && !captcha.trim().equals("")
                    && ssnCaptcha != null && !ssnCaptcha.trim().equals("")) {
                if (captcha.equals(ssnCaptcha)) {
                    logger.info("proceed with login, captcha matches(ssn against posted): {} != {}.", ssnCaptcha, captcha);
                    chain.doFilter(req, resp);
                    return;
                } else {
                    logger.info("login-failed, captcha doesn't match(ssn against posted): {} != {}.", ssnCaptcha, captcha);
                    request.setAttribute("error", String.format("captcha doesn't match(ssn against posted): '%s' != '%s'.", ssnCaptcha, captcha));
                }
            } else {
                logger.info("login-failed, no captcha.");
                request.setAttribute("error", "no captcha.");
            }
            request.setAttribute("error", "Captcha phase doesn't match.");
            AuthenticationServiceException exception = new AuthenticationServiceException(
                    String.format("Captcha phase doesn't match:'%s' != '%s'.", ssnCaptcha, captcha));
            failureHandler.onAuthenticationFailure(request, response, exception);
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
