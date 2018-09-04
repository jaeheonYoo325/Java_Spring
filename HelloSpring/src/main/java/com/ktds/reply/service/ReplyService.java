package com.ktds.reply.service;

import org.springframework.stereotype.Service;

import com.ktds.reply.vo.ReplyVO;

public interface ReplyService {

	public boolean createOneReply(ReplyVO replyVO);
	
}
