package org.zerock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.FreeBoard;
import org.zerock.domain.FreeBoardReply;
import org.zerock.persistence.FreeBoardReplyRepository;
import org.zerock.persistence.FreeBoardRepository;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class FreeBoardTests {

	@Autowired
	FreeBoardRepository boardRepo;
	
	@Autowired
	FreeBoardReplyRepository replyRepo;
	
	@Test
	public void insertDummy() {
		IntStream.range(1,200).forEach(i->{
			FreeBoard board = new FreeBoard();
			board.setTitle("Free Board ..." +i);
			board.setContent("Free Content...."+i);
			board.setWriter("user"+i%10);
			
			boardRepo.save(board);
		});
	}
	
	//양방향 방식의 댓글 추가
	//FreeBoard객체를 얻어온 후 FreeBoardReply를 댓글 리스트에 추가한 후에 FreeBoard 자체를 저장
	@Transactional
	@Test
	public void insertReply2Way() {
		Optional<FreeBoard> result = boardRepo.findById(199L);
		result.ifPresent(board->{
			List<FreeBoardReply> replies=board.getReplies();
			FreeBoardReply reply = new FreeBoardReply();
			reply.setReply("REPLY..........");
			reply.setReplyer("replyer00");
			reply.setBoard(board);
			
			replies.add(reply);
			board.setReplies(replies);
			boardRepo.save(board);
		});
	}
	
	//단방향 방식의 댓글 추가
	@Test
	public void insertReply1Way() {
		FreeBoard board = new FreeBoard();
		board.setBno(199L);
		
		FreeBoardReply reply = new FreeBoardReply();
		reply.setReply("REPLY..........");
		reply.setReplyer("replyer00");
		reply.setBoard(board);
		
		replyRepo.save(reply);
	}
	
	//게시물의 페이징 처리
	//쿼리메소드 이용
	@Test
	public void testList1() {
		Pageable page = PageRequest.of(0, 10,Sort.Direction.DESC,"bno");
		boardRepo.findByBnoGreaterThan(0L, page).forEach(board->{
			log.info(board.getBno()+": "+board.getTitle());
		});
	}
	
	//지연로딩<->즉시로딩
	//즉시 로딩은 @OneToMany에 'fetch'라는 속성값으로 FetchType.EAGER를 지정
	//제목과 댓글의 수가 같이 출력되도록 하기
	@Test
	public void testList2() {
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "bno");
		boardRepo.findByBnoGreaterThan(0L, page).forEach(board->{
			log.info(board.getBno()+": "+board.getTitle()+": "+board.getReplies().size());
		});
	}
	
	//지연로딩의 문제를 해결하는 방법 -> @Query를 이용해서 조인 처리하기
	@Test
	public void testList3() {
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC,"bno");
		boardRepo.getPage(page).forEach(arr-> log.info(Arrays.toString(arr)));
	}
	
}
