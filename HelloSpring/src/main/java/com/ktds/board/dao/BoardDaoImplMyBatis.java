package com.ktds.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;

@Repository
public class BoardDaoImplMyBatis extends SqlSessionDaoSupport implements BoardDao{

	private Logger logger = LoggerFactory.getLogger(BoardDaoImplMyBatis.class);
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public int insertBoard(BoardVO boardVO) {
		return getSqlSession().insert("BoardDao.insertBoard", boardVO);
	}

	@Override
	public int updateBoard(BoardVO boardVO) {
		return getSqlSession().update("BoardDao.updateBoard", boardVO);
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		return getSqlSession().selectOne("BoardDao.selectOneBoard", id);
	}

	@Override
	public List<BoardVO> selectAllBoard() {
		return getSqlSession().selectList("BoardDao.selectAllBoard");
	}

	@Override
	public int deleteOneBoard(int id) {
		return getSqlSession().delete("BoardDao.deleteOneBoard", id);
	}

	@Override
	public int selectAllBoardsCount(BoardSearchVO boardSearchVO) {
		return getSqlSession().selectOne("BoardDao.selectAllBoardsCount", boardSearchVO);
	}

	@Override
	public List<BoardVO> selectAllBoards(BoardSearchVO boardSearchVO) {
		return getSqlSession().selectList("BoardDao.selectAllBoards", boardSearchVO);
	}
	
}
