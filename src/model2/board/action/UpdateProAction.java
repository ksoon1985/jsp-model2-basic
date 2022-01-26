package model2.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardDAO;
import board.BoardDTO;
import board.PageDTO;

public class UpdateProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		//한글 확인업을 위해 utf-8로 전환
		/* req.setCharacterEncoding("utf-8"); */
		 int currentPage =0;
		 if(req.getParameter("currentPage")==null) {
			 currentPage = 1; 
		   }else {
			 currentPage=Integer.parseInt(req.getParameter("currentPage"));
		   }
		   int currPageBlock=0;
		   if(req.getParameter("currPageBlock")==null) {
			   currPageBlock=1;
		   }else {
			 currPageBlock=Integer.parseInt(
					         req.getParameter("currPageBlock"));
		   }
			PageDTO pdto = new PageDTO();
			pdto.setCurrentPage(currentPage);
			pdto.setCurrPageBlock(currPageBlock);	
			req.setAttribute("pdto", pdto);
				//  updateForm에서 보내준 데이터 모두 받기
				BoardDTO dto = new BoardDTO();
				dto.setNum(Integer.parseInt(req.getParameter("num")));
				dto.setSubject(req.getParameter("subject"));
				dto.setContent(req.getParameter("content"));
				dto.setEmail(req.getParameter("email"));
				dto.setPasswd(req.getParameter("passwd"));
				dto.setAttachNm(req.getParameter("attachNm"));
		
		    //DAO에 대한 인스턴스 받아오기
		    BoardDAO dao = BoardDAO.getInstance();
		    //DAO에 해당 데이터 저장하는 로직을 만들고
		    // 그 로직을 사용한 후
		    dao.boardUpdate(dto);
		    //다음 페이지로 이동 시킴
		
		return "/board2/updatePro.jsp";
	}

}
