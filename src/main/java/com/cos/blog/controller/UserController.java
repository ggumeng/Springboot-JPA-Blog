package com.cos.blog.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js/**, /css/**, /image/** 허용
@Controller
public class UserController {
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}

	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) {

		// POST 방식으로 key=value 데이터를 요청
		// Retrofit2
		// OkHttp
		// RestTemplate

		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "087cf25db6d8c1a15882fd271742f35c");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		// Http 요청하기 - post 방식, response 받음
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);

		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;

		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		// Http 요청하기 - post 방식, response 받음
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;

		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail().split("@")[0] + "_" + kakaoProfile.getId())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao").build();
		
		// 가입자 혹은 비가입자 체크해서 처리
		User originUser = userService.findMember(kakaoUser.getUsername());
		
		if (originUser.getUsername() == null) {
			System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다.");
			userService.joinMember(kakaoUser);
		}
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}
	
	@GetMapping("/tax")
	public String taxPage(Model model) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\ISHIFT\\Documents\\카카오톡 받은 파일\\1-99.txt"));
		
		List<String> noList = new ArrayList<>();
		String str;
		
		while ((str=br.readLine()) != null) {
			noList.add(str);
		}
		
		System.out.println(noList);
		
//		RestTemplate restTemplate = new RestTemplate();
//
//		// HttpHeader 오브젝트 생성
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-type", "application/json");

		// HttpBody 오브젝트 생성
		/*
		 * ObjectMapper objectMapper = new ObjectMapper(); objectMapper.
		 * params.add("serviceKey",
		 * "sBC5Y6wUFBw5ipm9767%2F1Cem0Z22wOJwyRNuDzCcaErWb9XjI%2BRL2mSHLZgbaUOnjM9vx%2B%2F4Mua9ZJWPyxxZAw%3D%3D"
		 * ); params.add("b_no", noList);
		 */

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
//		HttpEntity<MultiValueMap<String, Object>> taxRequest = new HttpEntity<>(headers);
//		
//		// Http 요청하기 - post 방식, response 받음
//		ResponseEntity<String> response = 
//				restTemplate.exchange("https://api.odcloud.kr/api/nts-businessman/v1/status", 
//						HttpMethod.POST,
//						taxRequest, 
//						String.class);
//		
//		System.out.println(response.getBody());
		
		model.addAttribute("nos", noList);
		return "hometax/tax";
	}
}
