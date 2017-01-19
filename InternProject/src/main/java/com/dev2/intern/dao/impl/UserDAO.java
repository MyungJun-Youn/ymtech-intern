package com.dev2.intern.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dev2.intern.dao.IUserDAO;
import com.dev2.intern.exception.ExistEmailException;
import com.dev2.intern.vo.CreateUserVO;
import com.dev2.intern.vo.UserVO;

@Repository(UserDAO.BEAN_QUALIFIER)
public class UserDAO extends GenericDAO implements IUserDAO {

	public static final String BEAN_QUALIFIER = "com.dev2.intern.dao.impl.UserDAO";
	
	@Override
	public int createUser(CreateUserVO createUserVO) throws ExistEmailException {
		String query = getQuery("user.createUser");
		checkExistUser(createUserVO.getEmail());
		
		return jdbcTemplate.update(query, createUserVO.getEmail(), createUserVO.getName(), createUserVO.getPassword());
	}
	
	@Override
	public void checkExistUser(String email) throws ExistEmailException {
		String query = getQuery("user.checkExistUser");
		int existionCheck = jdbcTemplate.queryForObject(query, Integer.class, email);

		if (existionCheck == 1) {
			throw new ExistEmailException();
		}
	}
	
	@Override
	public int checkValidLogin(String email, String password) {
		String query = getQuery("user.checkValidLogin");
		
		return jdbcTemplate.queryForObject(query, Integer.class, email, password);
	}
	
	@Override
	public int extractLevel(String email) {
		String query = getQuery("user.extractLevel");
		
		return jdbcTemplate.queryForObject(query, Integer.class, email);
	}

	@Override
	public UserVO getUserByEmail(String email) {
		String query = getQuery("user.getUserByEmail");
		UserVO userVO = (UserVO)jdbcTemplate.queryForObject(query, new RowMapper<UserVO>() {
			public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserVO userVO = new UserVO().setId(rs.getInt("id"))
											.setEmail(rs.getString("email"))
											.setName(rs.getString("name"))
											.setLevel(rs.getInt("level"));
				return userVO;
			}
		}, email);
		
		return userVO;
	}
	
	@Override
	public int modifyUser(String email, String password, String name) {
		String query = getQuery("user.modifyUser");
		int userId = getUserByEmail(email).getId();
		
		return jdbcTemplate.update(query, password, name, userId);
	}

	@Override
	public int deleteUser(String email) {
		String query = getQuery("user.deleteUser");
		gotoTrash(email);
		
		return jdbcTemplate.update(query, email);
	}
	
	@Override
	public int gotoTrash(String email) {
		String query = getQuery("user.gotoTrash");
		
		return jdbcTemplate.update(query, email);
	}
}
