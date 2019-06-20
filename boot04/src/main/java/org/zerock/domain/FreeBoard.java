package org.zerock.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="replies")
@Entity
@Table(name="tbl_freeboards")
@EqualsAndHashCode(of="bno")
public class FreeBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;
	private String title;
	private String writer;
	private String content;
	
	@CreationTimestamp
	private Timestamp regdate;
	@UpdateTimestamp
	private Timestamp updatedate;
	
	//@OneToMany(mappedBy="board", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	//게시물이 저장될 때 댓글이 같이 저장되도록 cascading추가
	//즉시로딩을 하기 위해 fetch의 속성값 추가
	//지연로딩을 이용하려면 fetch의 속성값을 .LAZY로 하고, test에서 @Transactional을 이용
	@OneToMany(mappedBy="board" , cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<FreeBoardReply> replies;


}
