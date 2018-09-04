package com.ktds.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ktds.board.dao.BoardDao;
import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.exceptions.PolicyViolationException;
import com.ktds.member.biz.MembersBiz;
import com.ktds.member.dao.MembersDao;
import com.ktds.member.vo.MembersVO;
import com.ktds.reply.dao.ReplyDao;
import com.ktds.reply.vo.ReplyVO;

import io.github.seccoding.web.pager.Pager;
import io.github.seccoding.web.pager.PagerFactory;
import io.github.seccoding.web.pager.explorer.ClassicPageExplorer;
import io.github.seccoding.web.pager.explorer.ListPageExplorer;
import io.github.seccoding.web.pager.explorer.PageExplorer;

@Service
public class BoardServiceImpl implements BoardService{

	//private BoardDaoImpl; 최악의 코드
	@Autowired
	@Qualifier("boardDaoImplMyBatis")
	private BoardDao boardDao;
	
	@Autowired
	private MembersBiz membersBiz;
	
	@Autowired
	private ReplyDao replyDao;
	
	// DI에서 제일 많이 사용하는 것이 Setter이다.
	
	/*public void setBoardDao(BoardDao boardDao) {
		System.out.println("Spring에서 호출함.");
		System.out.println(boardDao);
		this.boardDao = boardDao;
	}*/
	
	@Override
	public boolean createBoard(BoardVO boardVO, MembersVO membersVO) {
		
		boolean isUploadFile = boardVO.getOriginFileName() != null;
		
		int point = 10;
		if ( isUploadFile ) {
			point += 10;
		} 
		
		this.membersBiz.updatePoint(membersVO.getEmail(), point);
		
		int memberPoint = membersVO.getPoint();
		memberPoint += 10;
		membersVO.setPoint(memberPoint);
		
		boolean isSuccess = this.boardDao.insertBoard(boardVO) > 0;
		//Integer.parseInt("ABC"); // NumberFormatException
		return isSuccess;
	}

	@Override
	public boolean updateBoard(BoardVO boardVO) {
		System.out.println("Call BoardService.updateBoard();");
		return this.boardDao.updateBoard(boardVO) > 0;
	}

	@Override
	public BoardVO readOneBoard(int id) {
		return this.boardDao.selectOneBoard(id);
	}
	
	@Override
	public BoardVO readOneBoard(int id, MembersVO membersVO) {
		
		
		BoardVO boardVO = this.readOneBoard(id);
		
		List<ReplyVO> replyList = this.replyDao.selectAllReplies(id);
		boardVO.setReplyList(replyList);
		
		if (!boardVO.getEmail().equals( membersVO.getEmail())) {
			
			
			if ( membersVO.getPoint() < 2 ) {
				// /board/list는 예외를 던져줄 경로
				throw new PolicyViolationException("포인트가 부족합니다.", "/board/list");
			}
			
			// db
			this.membersBiz.updatePoint(membersVO.getEmail(), -2);
			// 조회 결과
			int point = membersVO.getPoint();
			point -= 2;
			membersVO.setPoint(point);
		}
		return boardVO;
	}

	@Override
	public boolean deleteOneBoard(int id) {
		return this.boardDao.deleteOneBoard(id) > 0;
	}

	@Override
	public PageExplorer readAllBoard(BoardSearchVO boardSearchVO) {
		
		int totalCount = this.boardDao.selectAllBoardsCount(boardSearchVO);
		
		Pager pager = PagerFactory.getPager(Pager.ORACLE, boardSearchVO.getPageNo() + "");
		
		pager.setTotalArticleCount(totalCount);
		
		PageExplorer pageExplorer = pager.makePageExplorer(ListPageExplorer.class, boardSearchVO);
		
		pageExplorer.setList( this.boardDao.selectAllBoards(boardSearchVO) );
		
		return pageExplorer;
	}
}
