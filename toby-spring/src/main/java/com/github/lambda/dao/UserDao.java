package com.github.lambda.dao;

import com.github.lambda.domain.User;

import java.util.List;

public interface UserDao {
	public void deleteAll();

	public int getCount();
	
	public void update(User user);

	public void add(User user) throws DuplicationUserIdException;

	public User get(String id);

	public List<User> getAll();
}
