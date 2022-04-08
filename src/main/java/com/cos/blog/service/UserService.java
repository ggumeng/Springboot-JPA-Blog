package com.cos.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다
@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void joinMember(User user) {
		userRepository.save(user);
	}

	@Transactional(readOnly = true) // Select 할 때 트랜잭션 시작, 서비스 종료 시에 트랜잭션 종료 (정합성)
	public User loginMember(User user) {
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
	}
}
