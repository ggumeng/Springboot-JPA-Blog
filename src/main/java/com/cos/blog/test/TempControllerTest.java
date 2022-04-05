package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {

	// http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		// 파일 리턴 기본경로: src/main/resources/static
		// 리턴명: /home.html
		// 풀경로: src/main/resource/static/home.html
		return "/home.html";
	}

	@GetMapping("/temp/img")
	public String tempImg() {
		return "/a.PNG";
	}

	// 동적 파일은 /static 에서 찾을 수 없음 -> 브라우저가 인식하지 못함
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		return "test";
	}
}
