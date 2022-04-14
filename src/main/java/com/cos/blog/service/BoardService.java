package com.cos.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다
@Service
public class BoardService {

	private static final Logger log = LoggerFactory.getLogger(BoardService.class);

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void writing(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional (readOnly = true)
	public Page<Board> getBoardList(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional (readOnly = true)
	public Board viewBoardDetail(int id) {
		return boardRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
				});
	}

	@Transactional
	public void deleteById(int id) {
		boardRepository.deleteById(id);
	}

	@Transactional
	public void update(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
				}); // 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료 시에(Service가 종료될 때) 트랜잭션이 종료된다. 이 때 더티체킹
	}

	@Transactional
	public void replyWriting(ReplySaveRequestDto replySaveRequestDto) {
		
//		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
//			return new IllegalArgumentException("댓글 쓰기 실패 : 유저 id를 찾을 수 없습니다.");
//		});
//		
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() -> {
//			return new IllegalArgumentException("댓글 쓰기 실패 : 게시글을 찾을 수 없습니다.");
//		});
//		
//		Reply reply = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
		
		int result = replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
	}
	
	
}
