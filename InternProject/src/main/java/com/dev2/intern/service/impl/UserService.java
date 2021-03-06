package com.dev2.intern.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dev2.intern.dao.impl.UserDAO;
import com.dev2.intern.exception.ExistEmailException;
import com.dev2.intern.service.IUserService;
import com.dev2.intern.vo.CreateUserVO;
import com.dev2.intern.vo.ModifyUserVO;
import com.dev2.intern.vo.UserVO;

@Service(UserService.BEAN_QUALIFIER)
public class UserService implements IUserService {

	public static final String BEAN_QUALIFIER = "com.dev2.intern.service.impl.UserService";
	
	@Autowired
	@Qualifier(UserDAO.BEAN_QUALIFIER)
	private UserDAO userDAO;
	
	@Override
	public int createUser(CreateUserVO createUserVO) throws ExistEmailException {
		int existionCheck = userDAO.checkExistUser(createUserVO.getEmail());
		
		if (existionCheck == 1) {
			throw new ExistEmailException();
		}
		
		return userDAO.createUser(createUserVO);
	}

	@Override
	public UserVO getUserByEmail(String email) {
		return userDAO.getUserByEmail(email);
	}
	
	@Override
	public int modifyUser(String email, ModifyUserVO modifyUserVO) {
		int userId = userDAO.getUserByEmail(email).getId();
		return userDAO.modifyUser(modifyUserVO.getPassword(), modifyUserVO.getName(), userId);
	}

	@Override
	public int deleteUser(String email) {
		userDAO.gotoTrash(email);
		return userDAO.deleteUser(email);
	}
}
