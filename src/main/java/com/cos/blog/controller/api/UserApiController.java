package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController {

	private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;

	@PostMapping("/api/user")
	public ResponseDto<Integer> save(@RequestBody User user) {
		log.info("save 호출됨");

		// 실제로 DB에 INSERT를 하고 return
		user.setRole(RoleType.USER);
		userService.joinMember(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// 스프링 시큐리티를 이용해서 로그인
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user){
		log.info("login 호출됨");
		User principal = userService.loginMember(user); // principal (접근주체)
		
		if (principal != null) {
			session.setAttribute("principal", principal);
		}
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
   