package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@RestController
public class DummyControllerTest {
	
	private static final Logger log = LoggerFactory.getLogger(DummyControllerTest.class);
	
	@Autowired // 의존성 주입
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {  // DB에 존재하지 않는 데이터에 접근할 때 발생하는 예외 
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		return "삭제되었습니다. id : " + id;
	}
	
	// save 함수는 id를 전달하면 값이 있을 땐 update, 값이 없을 땐 insert
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		 log.info("id : " + id);
		 log.info("password : " +requestUser.getPassword());
		 log.info("email : " +requestUser.getEmail());
		 
		 User user = userRepository.findById(id).orElseThrow(() -> {
			 return new IllegalArgumentException("수정에 실패하였습니다.");
		 });
		 
		 user.setPassword(requestUser.getPassword());
		 user.setEmail(requestUser.getEmail());
		 
		 // 더티 체킹
		 return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	// 한 페이지당 2건의 데이터를 리턴
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 1, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	// {id} 주소로 파라미터를 전달 받을 수 있음
	// http://localhost:8000/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// findById => return 방식 Optional.. null인지 아닌지 판단해야됨
		
		// 람다식 : () -> {new illegalArgumentEx....};
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		// 번역을 해야한다.
		return user;
	}
	
	// http://localhost:8000/blog/dummy/join (요청)
	// http의 body에 username, password, email 데이터를 가지고 (요청)
	@PostMapping("/dummy/join")
	public String join(User user) {
		log.info("username : " + user.getUsername());
		log.info("password : " + user.getPassword());
		log.info("email : " + user.getEmail());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
