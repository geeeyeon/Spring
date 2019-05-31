package com.example.spring02.controller.sop;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring02.service.shop.ProductService;

@Controller//현재 클래스를 스프링에서 관리하는 컨트롤러 빈으로 설정
@RequestMapping("/shop/product/*") //공통적인 url mapping
public class ProductController {
	@Inject
	ProductService productService;//스프링에서 만든 서비스 객체를 연결시킴
	
	@RequestMapping("list.do")
	public ModelAndView list(ModelAndView mav) {
		mav.setViewName("/shop/product_list");//이동할 페이지
		mav.addObject("list",productService.listProduct());
		return mav;
	}
	
	@RequestMapping("/detail/{product_id}")
	public ModelAndView detail(
			@PathVariable("product_id") int product_id, ModelAndView mav) {
		mav.setViewName("/shop/product_detail");
		mav.addObject("dto",productService.detailProduct(product_id));
		return mav;
	}
}
