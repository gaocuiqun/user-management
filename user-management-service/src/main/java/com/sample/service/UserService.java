package com.sample.service;

import com.github.apuex.eventsource.*;
import com.github.apuex.springbootsolution.runtime.*;
import com.sample.message.*;
import com.sample.dao.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.net.*;
import java.util.*;
import java.security.*;

@Component
public class UserService {
  private final static Logger logger = LoggerFactory.getLogger(UserService.class);
  @Autowired
  private EventSourceAdapter eventSourceAdapter;
  @Autowired
  private UserDAO userDAO;

  @Transactional
  public void create(CreateUserCmd c, Principal p, URI u) {
    userDAO.create(c);
    eventSourceAdapter.publish(c, p, u);
  }

  @Transactional
  public UserVo retrieveByRowid(RetrieveByRowidCmd c, Principal p, URI u) {
    eventSourceAdapter.publish(c, p, u);
    return userDAO.retrieveByRowid(c);
  }

  @Transactional
  public UserVo retrieve(RetrieveUserCmd c, Principal p, URI u) {
    eventSourceAdapter.publish(c, p, u);
    return userDAO.retrieve(c);
  }

  @Transactional
  public void update(UpdateUserCmd c, Principal p, URI u) {
    userDAO.update(c);
    eventSourceAdapter.publish(c, p, u);
  }

  @Transactional
  public void delete(DeleteUserCmd c, Principal p, URI u) {
    userDAO.delete(c);
    eventSourceAdapter.publish(c, p, u);
  }

  @Transactional
  public UserListVo query(QueryCommand q, Principal p, URI u) {
    eventSourceAdapter.publish(q, p, u);
    return userDAO.query(q);
  }

}
