package com.github.lambda.service;

import com.github.lambda.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("testUserService")
public class TestUserServiceImpl extends UserServiceImpl {
  
  private String id = "silver2";

  @Override
  public List<User> getAll() {
    for(User user : super.getAll()) {
      super.update(user);
    }

    return null;
  }

  protected void upgradeLevel(User user) {
    if (user.getId().equals(this.id)) throw new TestUserServiceException();
    super.upgradeLevel(user);
  }
}
