package com.example.spring02.controller.member;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring02.model.member.dto.MemberDTO;
import com.example.spring02.service.member.MemberService;

@Controller
@RequestMapping("/member/*")//공통적인 url mapping
public class MemberController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService memberService;
	
	@RequestMapping("login.do")
	public String login() {
		return "member/login";
	}
	
	@RequestMapping("login_check.do")
	public ModelAndView login_check(@ModelAttribute MemberDTO dto, HttpSession session) {
		String name=memberService.loginCheck(dto, session);
		ModelAndView mav = new ModelAndView();
		if(name!=null)
			mav.setViewName("home");
		else {
			mav.setViewName("member/login");
			mav.addObject("message","error");
		}
		return mav;
	}
	
	@RequestMapping("logout.do")
	public ModelAndView logout(HttpSession session, ModelAndView mav) {
		memberService.logout(session);	//세션 초기화
		mav.setViewName("member/login");//이동할 페이지의 이름
		mav.addObject("message","logout");//변수 저장
		return mav;
	}
}
