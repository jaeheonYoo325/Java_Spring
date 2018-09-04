package com.ktds.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ktds.common.dao.support.BindData;
import com.ktds.member.vo.MembersVO;

@Repository
public class MembersDaoImpl implements MembersDao{

	private interface Query {
		int INSERT_MEMBER_QUERY = 0;
		int SELECT_MEMBER_QUERY = 1;
		int POINT_UPDATE_QUERY = 2;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="memberQueries")
	private List<String> memberQueries;

	@Override
	public int insertOneMember(MembersVO membersVO) {
		return this.jdbcTemplate.update
				(this.memberQueries.get(Query.INSERT_MEMBER_QUERY)
						, membersVO.getEmail()
						, membersVO.getName()
						, membersVO.getPassword()
				);
	}

	@Override
	public MembersVO selectOneMember(MembersVO membersVO) {
		return jdbcTemplate.queryForObject(
				this.memberQueries.get(Query.SELECT_MEMBER_QUERY)
				, new Object[] {membersVO.getEmail(), membersVO.getPassword()}
				, new RowMapper<MembersVO>() {

					@Override
					public MembersVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						return BindData.bindData(rs, new MembersVO());
					}
				}
			);
	}

	@Override
	public int updatePoint(Map<String, Object> param) {
		return this.jdbcTemplate.update(
				this.memberQueries.get(Query.POINT_UPDATE_QUERY)
				, param.get("point")
				, param.get("email")
			);
	}
}
