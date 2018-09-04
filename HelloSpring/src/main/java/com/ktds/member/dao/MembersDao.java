package com.ktds.member.dao;

import java.util.Map;

import com.ktds.member.vo.MembersVO;

public interface MembersDao {

	public int insertOneMember(MembersVO membersVO);
	
	public MembersVO selectOneMember(MembersVO membersVO);
	
	public int updatePoint(Map<String, Object> param);
}
