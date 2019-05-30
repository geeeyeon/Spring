package com.example.spring01.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring01.model.dto.MemberDTO;
import com.example.spring01.service.MemberService;

@Controller //현재 클래스를 Controller Bean으로 등록함
public class MemberController {
	private static final Logger logger=
			LoggerFactory.getLogger(MemberController.class);
	
	@Inject // MemberService 객체가 주입됨
	MemberService memberService;
	
	@RequestMapping("member/list.do") //사용자가 요청하는 주소
	public String memberList(Model model) {
		List<MemberDTO> list=memberService.memberList();
		logger.info("회원 목록:"+list);
		model.addAttribute("list",list); //모델에 저장 
		return "member/member_list"; //출력 페이지로 포워딩 
	} 
	
	@RequestMapping("member/write.do")
	public String write() {
		
		return "member/write";
	}
//폼에 입력한 데이터가 MemberDTO dto 변수에 저장됨 
//	request.getParameter 생략	
	@RequestMapping("member/insert.do")
	public String insert(@ModelAttribute MemberDTO dto) {
		memberService.insertMember(dto);
		return "redirect:/member/list.do"; //목록 갱신 
	}
// view.do?userid=kim 이라면 
//	@RequestParam String userid 변수에 kim이 저장됨  	
	@RequestMapping("member/view.do")
	public String view(@RequestParam String userid, Model model) {
		model.addAttribute("dto", memberService.viewMember(userid));
		return "member/view"; // view.jsp로 포워딩 
	}
	
	@RequestMapping("member/update.do")
	public String update(@ModelAttribute MemberDTO dto,Model model){
		boolean result=memberService.checkPw(
				dto.getUserid(), dto.getPasswd());
		logger.info("비밀번호 확인:"+result);
		
		if(result) { //비밀번호가 맞으면
			memberService.updateMember(dto); //레코드 수정
			return "redirect:/member/list.do"; //목록으로 이동
		}else { //비밀번호가 틀리면
			MemberDTO dto2=memberService.viewMember(dto.getUserid());
			dto.setJoin_date(dto2.getJoin_date()); //날짜가 지워지지 않도록
			model.addAttribute("dto",dto);
			model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
			return "member/view"; //수정 페이지로 되돌아감 
		}
	}
	
	@RequestMapping("member/delete.do")
	public String delete(@RequestParam String userid, 
			@RequestParam String passwd, Model model) {
		boolean result=memberService.checkPw(userid, passwd);
		if(result) {
			memberService.deleteMember(userid);
			return "redirect:/member/list.do";
		}else {
			model.addAttribute("message","비밀번호가 일치하지 않습니다.");
			model.addAttribute("dto", memberService.viewMember(userid));
			return "member/view";
		}
	}
}





















