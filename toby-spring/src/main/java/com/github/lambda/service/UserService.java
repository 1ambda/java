package com.github.lambda.service;

import com.github.lambda.domain.User;

public interface UserService {

	public abstract void add(User u);

	public abstract void upgradeLevels();

}