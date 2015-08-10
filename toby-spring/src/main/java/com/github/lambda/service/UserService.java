package com.github.lambda.service;

import com.github.lambda.domain.User;

import java.util.List;

public interface UserService {

	void add(User u);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    void update(User user);

	void upgradeLevels();

}