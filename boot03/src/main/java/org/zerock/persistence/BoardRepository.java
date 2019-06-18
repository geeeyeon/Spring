package org.zerock.persistence;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.zerock.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {

	//제목으로 게시글 찾기
	public List<Board> findBoardByTitle(String title);
	
	//작성자로 검색
	public Collection<Board> findByWriter(String writer);
	
	//작성자에 대한 like % 키워드 %
	public Collection<Board> findByWriterContaining(String wirter);
	
	//or조건 처리
	public Collection<Board> findByTitleContainingOrContentContaining(String title, String content);
	
	//부등호 처리
	//title LIKE % ? % AND BNO > ?
	public Collection<Board> findByTitleContainingAndBnoGreaterThan(String keyword, Long num);
	
	//게시물의 bno가 특정 번호보다 큰 게시물을 bno값의 역순으로 처리
	public Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);
	//페이징처리
	public List<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno, Pageable paging);
	//페이징,정렬처리
	//public List<Board> findByBnoGreaterThan(Long bno, Pageable paging);
	//인터페이스에 페이징, 정렬 처리
	public Page<Board> findByBnoGreaterThan(Long bno, Pageable paging);
	
	
	
	@Query("SELECT b FROM Board b WHERE b.title LIKE %?1% AND b.bno>0 ORDER BY b.bno DESC")
	public List<Board> findByTitle(String title);
	
	@Query("SELECT b FROM Board b WHERE b.content LIKE %:content% AND b.bno>0 ORDER BY b.bno DESC")
	public List<Board> findByContent(@Param("content") String content);
	
	//content 칼럼의 내용을 제외한 칼럼 가져오기
	//리턴타입이 Object[]의 리스트 형태->결과 역시 배열로 출력
	@Query("SELECT b.bno, b.title, b.writer, b.regdate FROM Board b WHERE b.title LIKE %?1% AND b.bno>0 ORDER BY b.bno DESC")
	public List<Object[]> findByTitle2(String title);
}
