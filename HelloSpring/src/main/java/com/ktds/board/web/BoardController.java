package com.ktds.board.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.board.service.BoardService;
import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.exceptions.PolicyViolationException;
import com.ktds.common.session.Session;
import com.ktds.common.web.DownloadUtil;
import com.ktds.member.vo.MembersVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

@Controller
public class BoardController {

	//private Logger logger = LoggerFactory.getLogger(BoardController.class);
	private Logger statisticsLogger = LoggerFactory.getLogger("list.Statistics");
	// com.ktds.board.web.BoardController.class
	private Logger paramLogger = LoggerFactory.getLogger(BoardController.class);
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	@Qualifier("boardServiceImpl")
	private BoardService boardService;
	
	@RequestMapping("/board/list/init")
	public String viewBoardListPageAForInitiate( HttpSession session ) {
		session.removeAttribute(Session.SEARCH);
		return "redirect:/board/list";
	}
	
	@RequestMapping("/board/list")
	public ModelAndView viewBoardListPage(
			@ModelAttribute BoardSearchVO boardSearchVO
			, HttpServletRequest request
			, HttpSession session) { // IP를 얻어올 때 사용

		// 전체 검색 or 상세-> 목록 or 글쓰기
		if ( boardSearchVO.getSearchKeyword() == null ) {
			boardSearchVO = (BoardSearchVO) session.getAttribute(Session.SEARCH);
			
			if ( boardSearchVO == null ) {
				boardSearchVO = new BoardSearchVO();
				boardSearchVO.setPageNo(0);
			}
		}
		
		PageExplorer pageExplorer = this.boardService.readAllBoard(boardSearchVO);
		
		// 로그 
		statisticsLogger.info(
				"URL : /board/list, IP : " 
		+ request.getRemoteAddr() 
		+ ", List Size : " 
		+ pageExplorer.getList().size());
		
		session.setAttribute(Session.SEARCH, boardSearchVO);
		
		ModelAndView view = new ModelAndView("board/list");
		view.addObject("boardVOList", pageExplorer.getList());
		view.addObject("pagenation", pageExplorer.make());
		view.addObject("size", pageExplorer.getTotalCount());
		view.addObject("boardSearchVO", boardSearchVO);
		// /WEB-INF/view/board/list.jsp로 포워드 시켜준다.
		return view;
	}
	
	// Spring 4.2 이하에서 사용
	// @RequestMapping(value="/write", method=RequestMethod.GET)
	// Spring 4.3 이상에서 사용
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
		return "board/write";
	}
	
	@PostMapping("/board/write")
	public ModelAndView doBoardWriteAction(
			@Valid @ModelAttribute BoardVO boardVO
			, Errors errors
			, HttpSession session
			, HttpServletRequest request) {
		
		ModelAndView view = new ModelAndView("redirect:/board/list/init");
		// Validation Annotation이 실패했는지 체크
		if ( errors.hasErrors() ) {
			view.setViewName("board/write");
			view.addObject("boardVO", boardVO);
			return view;
		}
		
		MultipartFile uploadFile = boardVO.getFile();
		
		if ( !uploadFile.isEmpty() ) {
			
			// 실제 파일 이름
			String originFileName = uploadFile.getOriginalFilename();
			// 파일 시스템에 저장될 파일의 이름(난수)
			String fileName = UUID.randomUUID().toString();
			
			File uploadDir = new File(this.uploadPath);
			// 폴더가 존재하지 않는다면 생성
			if ( !uploadDir.exists() ) {
				uploadDir.mkdirs();
			}
			
			// 파일이 업로드될 경로 지정
			File destFile = new File(this.uploadPath, fileName);
			
			try {
				// 업로드
				uploadFile.transferTo(destFile);
				// DB에 File 정보 저장하기 위한 정보 셋팅
				boardVO.setOriginFileName(originFileName);
				boardVO.setFileName(fileName);
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			
		}
		
		// 4.3 미만에서 사용
		// session.getAttribute은 Object타입이라 Object의 자식인 MemberVO에 넣을 수 없다.
		// 넣을려면 Casting 해야한다.
		
		// 세션을 체크하는 부분이다.
		MembersVO loginMemberVO = (MembersVO) session.getAttribute(Session.USER);
		String email = loginMemberVO.getEmail();
		boardVO.setMembersVO(loginMemberVO);
		boardVO.setEmail(email);
		
		/*String view = this.boardService.createBoard(boardVO, loginMemberVO) ? 
				"redirect:/board/list" : "redirect:/board/write";*/
		
		boolean isSuccess = this.boardService.createBoard(boardVO, loginMemberVO); 
				
		
		String paramFormat = "IP:%s, Param:%s, Result:%s";
		paramLogger.debug(String.format(paramFormat
				, request.getRemoteAddr()
				, boardVO.getSubject() + ", " 
						+ boardVO.getContent() + ", "
						+ boardVO.getEmail()
						+ boardVO.getFileName() + ", "
						+ boardVO.getOriginFileName()
				, view.getViewName() // "redirect:/board/list"
				));
		return view;
		
		
		/*String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		String email = request.getParameter("email");
		
		BoardVO boardVO = new BoardVO();
		boardVO.setSubject(subject);
		boardVO.setContent(content);
		boardVO.setEmail(email);*/
		
		/*boolean isSuccess = this.boardService.createBoard(boardVO);
		return "redirect:/list";*/
		
		//(condition) ? true : false; <- Elvis Operator
		// 삼항 연산자는 느리다. 자바는 삼항 연산자를 if문으로 다시 변환해주기 때문이다.
		// 간단한 한줄 정도면 괜찮지만 반복문 안에 적으면 비효율적이다.
	}
	
	// http://localhost:8080/HelloSpring/board/detail/1
	@RequestMapping("/board/detail/{id}")
	public ModelAndView viewBoardDetailPage(
			@PathVariable int id
			, @SessionAttribute(Session.USER) MembersVO membersVO
			, HttpServletRequest request) {
				
		
		/*if ( membersVO.getPoint() < 2 ) {
			// 데이터는 안보내고 뷰로만 보낼 수 있다.
			return new ModelAndView("redirect:/board/list");
		}*/
		
		BoardVO boardVO = this.boardService.readOneBoard(id, membersVO);
 		ModelAndView view = new ModelAndView("board/detail");
		view.addObject("boardVO", boardVO);
		
		String paramFormat = "IP:%s, Param:%s, Result:%s";
		paramLogger.debug(String.format(paramFormat
				, request.getRemoteAddr()
				, boardVO.getId() + ", "
						+boardVO.getSubject() + ", " 
						+ boardVO.getContent() + ", "
						+ boardVO.getEmail()
						+ boardVO.getFileName() + ", "
						+ boardVO.getOriginFileName()
				, view
				) );
		
		return view; 
	}
	
	@RequestMapping("/board/delete/{id}")
	public String doBoardDeleteAction(
			@PathVariable int id
			, HttpServletRequest request
			, @SessionAttribute(Session.USER) MembersVO membersVO) {
		boolean isSuccess = this.boardService.deleteOneBoard(id);
		
		String paramFormat = "IP:%s, Param:%s, Result:%s";
		paramLogger.debug(String.format(paramFormat
				, request.getRemoteAddr()
				, membersVO.getEmail()
				+ id
				, isSuccess 
				) );
		
		return "redirect:/board/list";
	}
	
	@RequestMapping("/board/download/{id}")
	public void fileDownload(
			@PathVariable int id
			, HttpServletRequest request
			, HttpServletResponse response
			, @SessionAttribute(Session.USER) MembersVO membersVO) { 
		
		if ( membersVO.getPoint() < 5 ) {
			throw new PolicyViolationException(
					"다운로드를 위한 포인트가 부족합니다."
					, "/board/detail/" + id);
		}
		
		 BoardVO boardVO = this.boardService.readOneBoard(id);
		
		 String originFileName = boardVO.getOriginFileName();
	     String fileName = boardVO.getFileName();
	     
	     // Windows => \
	     // Unix/Linux/macOS => /
	     
	     try {
			new DownloadUtil(this.uploadPath + File.separator + fileName)
			 .download(request, response, originFileName);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
