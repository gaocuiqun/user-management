package com.sample.controller;

import com.sample.message.RetrieveUserCmd;
import com.sample.message.UserVo;
import com.sample.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping(value="auth", method=RequestMethod.POST)
public class LoginController {
  private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
  @Autowired
  private UserService service;

  private CaptchaGenerator captchaGenerator = CaptchaGenerator.newGenerator(4, 2);

  @GetMapping(value="captcha")
  public void captcha(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession session = req.getSession(true);
    generateToken(session);
    String token = session != null ? getToken(session) : null;
    if (token == null || isTokenUsed(session)) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND,
              "Captcha not found.");

      return;
    }

    setResponseHeaders(resp);
    markTokenUsed(session, true);
    captchaGenerator.draw(token, resp.getOutputStream());
  }

  @GetMapping(value="login")
  public String login(@RequestParam(name = "userName") String userName,
                      @RequestParam(name = "password") String password,
                      @RequestParam(name = "captcha") String captcha,
                      HttpServletRequest r) {
    RetrieveUserCmd c = RetrieveUserCmd.newBuilder()
            .setUserId(userName)
            .build();
    try {
      if(captcha == null || captcha.equals(getToken(r.getSession()))) {
        return "login-failed";
      }

      UserVo user = service.retrieve(c, r.getUserPrincipal(), new URI(r.getRequestURI()));
      if(user == null || !user.getPassword().equals(hashed(password, "MD5"))) {
        return "login-failed";
      }
      r.login(userName, password);
      return "welcome";
    } catch (Exception ex) {
      ex.printStackTrace();
      return "login-failed";
    }
  }

  @RequestMapping(value="logout", method=RequestMethod.GET)
  public String logout(Model model, HttpServletRequest r) {
    HttpSession ssn = r.getSession(false);
    if(ssn != null) ssn.invalidate();
    return "logged-out";
  }

  private String hashed(String message, String algorithm) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance(algorithm);
    md.update(message.getBytes());
    byte[] digest = md.digest();
    StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }

    System.out.println("original:" + message);
    System.out.println("digested(hex):" + sb.toString());

    return sb.toString();
  }


  /**
   * Generates a captcha token and stores it in the session.
   *
   * @param session
   *            where to store the captcha.
   */
  public void generateToken(HttpSession session) {
    String token = captchaGenerator.getTokenGenerator().next();
    logger.info("The new CAPTCHA token is: {}", token);
    session.setAttribute("captchaToken", token);
    markTokenUsed(session, false);
  }

  /**
   * Used to retrieve previously stored captcha token from session.
   *
   * @param session
   *            where the token is possibly stored.
   * @return token or null if there was none
   */
  public static String getToken(HttpSession session) {
    Object val = session.getAttribute("captchaToken");

    return val != null ? val.toString() : null;
  }

  /**
   * Marks token as used/unused for image generation.
   *
   * @param session
   *            where the token usage flag is possibly stored.
   * @param used
   *            false if the token is not yet used for image generation
   */
  protected static void markTokenUsed(HttpSession session, boolean used) {
    session.setAttribute("captchaTokenUsed", used);
  }

  /**
   * Checks if the token was used/unused for image generation.
   *
   * @param session
   *            where the token usage flag is possibly stored.
   * @return true if the token was marked as unused in the session
   */
  protected static boolean isTokenUsed(HttpSession session) {
    return !Boolean.FALSE.equals(session.getAttribute("captchaTokenUsed"));
  }

  /**
   * Helper method, disables HTTP caching.
   *
   * @param resp
   *            response object to be modified
   */
  protected void setResponseHeaders(HttpServletResponse resp) {
    resp.setContentType("image/" + captchaGenerator.getFormat());
    resp.setHeader("Cache-Control", "no-cache, no-store");
    resp.setHeader("Pragma", "no-cache");
    long time = System.currentTimeMillis();
    resp.setDateHeader("Last-Modified", time);
    resp.setDateHeader("Date", time);
    resp.setDateHeader("Expires", time);
  }
}
