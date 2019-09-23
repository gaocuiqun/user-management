package com.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class CaptchaFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String captcha = req.getParameter("captcha");
        if("/login".equals(req.getRequestURI()) && req.getMethod().equalsIgnoreCase("POST")) {
            if (captcha != null) {
                String ssnCaptcha = LoginController.getToken(req.getSession());
                if (captcha.equals(ssnCaptcha)) {
                    logger.info("proceed with login, captcha matches(ssn against posted): {} != {}.", ssnCaptcha, captcha);
                    chain.doFilter(request, response);
                    return;
                } else {
                    logger.info("login-failed, captcha doesn't match(ssn against posted): {} != {}.", ssnCaptcha, captcha);
                }
            } else {
                logger.info("login-failed, no captcha ");
            }
            res.sendError(401, "captcha doesn't match.");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
