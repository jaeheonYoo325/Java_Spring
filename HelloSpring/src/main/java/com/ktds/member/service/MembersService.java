package com.ktds.member.service;

import com.ktds.member.vo.MembersVO;

public interface MembersService {

	public boolean regiestOneMember(MembersVO membersVO);
	
	public MembersVO readOneMember(MembersVO membersVO);
}
