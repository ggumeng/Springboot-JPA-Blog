package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ORM -> Java(다른언어) Object -> 테이블로 매핑해주는 기술
@Entity // User 클래스가 MySQL 에 테이블이 생성됨
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	
	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // 시퀀스, MySQL: auto_increment
	
	@Column(nullable = false, length = 30)
	private String username; // 아이디
	
	@Column(nullable = false, length = 100) // 해쉬를 이용해 비밀번호 암호화를 하려면 길이를 길게 설정해야 한다.
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	  
	@ColumnDefault("'user'")
	private String role; // Enum 쓰는 것이 좋다. (권한 => admin, user, manager)
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}
