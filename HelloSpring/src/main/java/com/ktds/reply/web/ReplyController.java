package com.ktds.reply.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktds.common.session.Session;
import com.ktds.member.vo.MembersVO;
import com.ktds.reply.service.ReplyService;
import com.ktds.reply.vo.ReplyVO;

@Controller
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@PostMapping("/reply/write")
	public String doReplyWriteAction(
			@ModelAttribute ReplyVO replyVO
			, @SessionAttribute(Session.USER) MembersVO membersVO) {
		
		replyVO.setEmail(membersVO.getEmail());
		boolean isSuccess = this.replyService.createOneReply(replyVO);
		
		return "redirect:/board/detail/" + replyVO.getBoardId();
		
	}
}
