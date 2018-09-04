package com.ktds.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ktds.member.biz.MembersBiz;
import com.ktds.member.dao.MembersDao;
import com.ktds.member.vo.MembersVO;

@Service
public class MembersServiceImpl implements MembersService{

	@Autowired
	@Qualifier("membersDaoImplMyBatis")
	private MembersDao membersDao;
	
	@Autowired
	private MembersBiz membersBiz;
	
	@Override
	public boolean regiestOneMember(MembersVO membersVO) {
		return this.membersDao.insertOneMember(membersVO) > 0;
	}

	@Override
	public MembersVO readOneMember(MembersVO membersVO) {
		
		MembersVO loginMembersVO = this.membersDao.selectOneMember(membersVO);
		
		if ( loginMembersVO != null ) {
			
			this.membersBiz.updatePoint(membersVO.getEmail(), +2);
			
			int point = loginMembersVO.getPoint();
			point += 2;
			loginMembersVO.setPoint(point);
		}
		
		return loginMembersVO;
	}

}
