package com.ktds.member.biz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ktds.member.dao.MembersDao;

@Component
public class MembersBizImpl implements MembersBiz{

	@Autowired
	@Qualifier("membersDaoImplMyBatis")
	private MembersDao membersDao;
	
	@Override
	public int updatePoint(String email, int point) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("email", email);
		param.put("point", point);
		
		return membersDao.updatePoint(param);
	}

}
