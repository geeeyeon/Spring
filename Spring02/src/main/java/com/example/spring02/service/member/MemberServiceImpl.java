package com.example.spring02.service.member;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.spring02.model.member.dao.MemberDAO;
import com.example.spring02.model.member.dto.MemberDTO;

@Service
public class MemberServiceImpl implements MemberService {

	@Inject
	MemberDAO memberDao;
	
	@Override
	public String loginCheck(MemberDTO dto, HttpSession session) {
		//맞으면 이름이 넘어오고 틀리면 null이 넘어옴
		String name=memberDao.loginCheck(dto);
		
		if(name!=null) {//맞으면
			session.setAttribute("userid", dto.getUserid());
			session.setAttribute("name", name);
		}
		return name;
	}

	@Override
	public void logout(HttpSession session) {
		//세션 초기화
		session.invalidate();
		//invalidate : 세션을 지워버리는 함수
	}

}
