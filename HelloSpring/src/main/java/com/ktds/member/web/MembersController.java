package com.ktds.member.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.common.session.Session;
import com.ktds.member.service.MembersService;
import com.ktds.member.validator.MembersValidator;
import com.ktds.member.vo.MembersVO;

@Controller
public class MembersController {

	@Autowired
	private MembersService membersService;
	
	@GetMapping("/member/logout")
	public String doMemberLogoutAction( HttpSession session ) {
		// Logout
		session.invalidate();
		return "redirect:/member/login";
	}
	
	@GetMapping("/member/regist")
	public String viewRegistPage() {
		return "member/regist";
	}
	
	// /member/check/duplicate?email=234@234.com
	@PostMapping("/member/check/duplicate")
	@ResponseBody
	public Map<String, Object> doCheckDuplicateEmail(@RequestParam String email) {
		
		Random random = new Random();
		
		Map<String, Object> result = new HashMap<>();
		result.put("status", "OK");
		result.put("duplicated", random.nextBoolean());
		
		return result;
	}
	
	@PostMapping("/member/regist")
	public ModelAndView doMemberRegistAction(
			@Validated(value= {MembersValidator.Regist.class}) @ModelAttribute MembersVO membersVO
			, Errors errors) {
		
		ModelAndView view = new ModelAndView("redirect:/member/login");
		if ( errors.hasErrors() ) {
			view.setViewName("member/regist");
			view.addObject("membersVO", membersVO);
			return view;
		}
		
		boolean isSuccess = this.membersService.regiestOneMember(membersVO);
		return view;
		/*return this.membersService.regiestOneMember(membersVO) ? 
				"redirect:/member/login" : "redirect:/member/regist";*/
	}
	
	@GetMapping("/member/login")
	public String viewLoginPage() {
		return "member/login";
	}
	
	@PostMapping("/member/login")
	public ModelAndView doMemberLoginAction(
			@Validated(value= {MembersValidator.Login.class}) @ModelAttribute MembersVO membersVO
			, Errors errors
			, HttpSession session) {
		
		ModelAndView view = new ModelAndView("redirect:/board/list");
		
		if ( errors.hasErrors() ) {
			view.setViewName("member/login");
			view.addObject("membersVO", membersVO);
			return view;
		}
		
		MembersVO loginMember = this.membersService.readOneMember(membersVO);
		
		// 로그인
		session.setAttribute(Session.USER, loginMember);
		return view;
	}
}
