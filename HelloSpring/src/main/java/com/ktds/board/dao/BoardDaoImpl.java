package com.ktds.board.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.dao.support.BindData;
import com.ktds.member.vo.MembersVO;

@Repository
public class BoardDaoImpl implements BoardDao{

	/*
	 가장 일반적으로 상수를 사용하는 방법이다.
	private final int INSERT_QUERY = 1;
	private final int SELECT_ONE_QUERY = 2;
	private final int DELETE_ONE_QUERY =3;
	*/
	
	// Inner Class (Interface)
	// 정석적인 상수 처리 방법
	private interface Query {
		int INSERT_QUERY = 0;
		int SELECT_ONE_QUERY = 1;
		int DELETE_ONE_QUERY = 2;
		int SELECT_ALL_QUERY = 3;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="boardQueries")
	private List<String> boardQueries;
	
	@Override
	public int insertBoard(BoardVO boardVO) {
		return this.jdbcTemplate.update(
					this.boardQueries.get(Query.INSERT_QUERY)
					, boardVO.getSubject()
					, boardVO.getContent()
					, boardVO.getEmail()
					, boardVO.getFileName()
					, boardVO.getOriginFileName()
				);
	}

	@Override
	public int updateBoard(BoardVO boardVO) {
		System.out.println("Call UpdateBoard();");
		return 0;
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		// new Object[] {} 는 배열을 만들어서 초기화 해주는 방법
		return this.jdbcTemplate.queryForObject(
				this.boardQueries.get(Query.SELECT_ONE_QUERY)
				, new Object[] {id}
				, new RowMapper<BoardVO>() {

					@Override
					public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						/*BoardVO boardVO = new BoardVO();
						boardVO.setId( rs.getInt("ID"));
						boardVO.setSubject( rs.getString("SUBJECT") );
						boardVO.setContent( rs.getString("CONTENT") );
						boardVO.setEmail( rs.getString("EMAIL") );
						boardVO.setCrtDt( rs.getString("CRT_DT") );
						boardVO.setMdfyDt( rs.getString("MDFY_DT"));
						boardVO.setFileName( rs.getString("FILE_NAME") );
						boardVO.setOriginFileName( rs.getString("ORIGIN_FILE_NAME") );
						
						return BindData.bindData(rs, new BoardVO());*/
						
						MembersVO membersVO = BindData.bindData(rs, new MembersVO());
						BoardVO	boardVO = BindData.bindData(rs, new BoardVO());
						boardVO.setMembersVO(membersVO);
						
						return boardVO;
					}
					
				}
			);
	}

	@Override
	public int deleteOneBoard(int id) {
		return this.jdbcTemplate.update(
				this.boardQueries.get(Query.DELETE_ONE_QUERY)
				, id);
	}

	@Override
	public List<BoardVO> selectAllBoard() {
		//return null;
		return this.jdbcTemplate.query(
				this.boardQueries.get(Query.SELECT_ALL_QUERY)
				, new RowMapper<BoardVO>() {

					@Override
					public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						//return BindData.bindData(rs, new BoardVO());
						
						MembersVO membersVO = BindData.bindData(rs, new MembersVO());
						BoardVO	boardVO = BindData.bindData(rs, new BoardVO());
						boardVO.setMembersVO(membersVO);
						
						return boardVO;
					}
				}
			);
	}

	@Override
	public int selectAllBoardsCount(BoardSearchVO boardSearchVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BoardVO> selectAllBoards(BoardSearchVO boardSearchVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
