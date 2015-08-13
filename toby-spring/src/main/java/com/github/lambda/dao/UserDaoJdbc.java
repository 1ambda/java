package com.github.lambda.dao;

import com.github.lambda.domain.Level;
import com.github.lambda.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class UserDaoJdbc implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSource ds;

	public void deleteAll() {
		jdbcTemplate.update("delete from users");
	}

	public int getCount() {
		return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

	public void add(User user) throws DuplicationUserIdException {

		try {
			jdbcTemplate.update("INSERT INTO users(id, name, password, level, login, recommend, email)" +
                            " VALUES (?, ?, ?, ?, ?, ?, ?);",
                    user.getId(),
                    user.getName(),
                    user.getPassword(),
                    user.getLevel().intValue(),
                    user.getLogin(),
                    user.getRecommend(),
                    user.getEmail());
		} catch (DuplicateKeyException e) {
			throw new DuplicationUserIdException(e);
		}
	}

	public User get(String id) {
		return jdbcTemplate.queryForObject( "SELECT * from users WHERE id = ?",
		                          new Object[] {id},
		                          (rs, rowNum) -> {
		                          	User user = new User();
		                          	user.setId(rs.getString("id"));
		                          	user.setName(rs.getString("name"));
		                          	user.setPassword(rs.getString("password"));
		                          	user.setLevel(Level.valueOf(rs.getInt("level")));
		                          	user.setLogin(rs.getInt("login"));
		                          	user.setRecommend(rs.getInt("recommend"));
		                          	return user;
		                          });
	}

	public List<User> getAll() {

		return jdbcTemplate.query("SELECT * FROM users",
		                (rs, rowNum) -> { return new User(rs.getString(1), 
		                	                rs.getString(2),
		                	                rs.getString(3),
		                	                Level.valueOf(rs.getInt(4)),
		                	                rs.getInt(5),
		                	                rs.getInt(6),
		                	                rs.getString(7)); });
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update("UPDATE users SET name = ?, password = ?, "
                        + "level = ?, login = ?, recommend = ? where id = ?",
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId());
	}

}
