package com.ktds.board.service;

import java.util.List;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.member.vo.MembersVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

public interface BoardService {
	
	public boolean createBoard(BoardVO boardVO, MembersVO membersVO);
	
	public boolean updateBoard(BoardVO boardVO);
	
	public BoardVO readOneBoard(int id);
	public BoardVO readOneBoard(int id, MembersVO membersVO);
	
	public boolean deleteOneBoard(int id);
	
	public PageExplorer readAllBoard(BoardSearchVO boardSearchVO);
	
}
