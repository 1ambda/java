package com.github.lambda.service;

import com.github.lambda.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

	void add(User u);
    void deleteAll();
    void update(User user);

    User get(String id);
    List<User> getAll();

	void upgradeLevels();

}