package com.ktds.member.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktds.member.vo.MembersVO;

@Repository
public class MembersDaoImplMyBatis 
		extends SqlSessionDaoSupport
		implements MembersDao {
	
	private Logger logger = LoggerFactory.getLogger(MembersDaoImplMyBatis.class);
	
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		logger.debug("Initiate MyBatis");
	    super.setSqlSessionTemplate(sqlSessionTemplate);
	  }

	@Override
	public int insertOneMember(MembersVO membersVO) {
		return getSqlSession().insert("MembersDao.insertOneMember", membersVO);
	}

	@Override
	public MembersVO selectOneMember(MembersVO membersVO) {
		return getSqlSession().selectOne("MembersDao.selectOneMember", membersVO);
	}

	@Override
	public int updatePoint(Map<String, Object> param) {
		/*Map<String, Object> param = new HashMap<>();
		param.put("email", email);
		param.put("point", point);*/
		return getSqlSession().update("MembersDao.updatePoint", param);
	}
}
