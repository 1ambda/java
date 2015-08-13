package com.github.lambda.service;

import com.github.lambda.domain.Level;
import com.github.lambda.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("testUserService")
@Transactional
public class TestUserServiceImpl extends UserServiceImpl {
  
  private String id = "silver2";

  @Override
  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
  public List<User> getAll() {
    // TODO
    return null;
  }

  protected void upgradeLevel(User user) {
    if (user.getId().equals(this.id)) throw new TestUserServiceException();
    super.upgradeLevel(user);
  }
}
