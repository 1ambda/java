package com.github.lambda.service;

import com.github.lambda.domain.User;
import org.springframework.stereotype.Component;

@Component("testUserService")
public class TestUserServiceImpl extends UserServiceImpl {
  
  private String id = "silver2";

  protected void upgradeLevel(User user) {
    if (user.getId().equals(this.id)) throw new TestUserServiceException();
    super.upgradeLevel(user);
  }
}
