package model2.board.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.BoardDAO;
import board.BoardDTO;
import board.PageDTO;

public class ListAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		 BoardDAO dao = BoardDAO.getInstance();
		 PageDTO pdto = new PageDTO();
		   //전체 레코드 수
		   int cnt = dao.getAllCount();
		   pdto.setAllCount(cnt);
		   //전체 페이지 수 계산
		   int pageCnt = cnt%pdto.getLinePerPage();
		   if(pageCnt==0) {
			   //몫이 전체페이지수
			pdto.setAllPage(cnt/pdto.getLinePerPage());   
		   }else {
			   //몫+1이 전체 페이지 수
			pdto.setAllPage(cnt/pdto.getLinePerPage()+1);
		   }
		   int currentPage =0;
		   if(req.getParameter("currentPage")==null || req.getParameter("currentPage").equals("0")) {
			   currentPage = 1; 
		   }else {
			   currentPage=Integer.parseInt(req.getParameter("currentPage"));
		   }
		   //현재 블럭 받아오기
		   int currPageBlock=0;
		   if(req.getParameter("currPageBlock")==null || req.getParameter("currPageBlock").equals("0")) {
			   currPageBlock=1;
		   }else {
			   currPageBlock=Integer.parseInt(req.getParameter("currPageBlock"));
		   }
		   pdto.setCurrentPage(currentPage);
		   pdto.setCurrPageBlock(currPageBlock);
		   
		   int startPage = 1;
		   int endPage=1;
		   //전체 페이지 수가 블럭보다 작다
		  /* if(pdto.getAllPage()<pdto.getPageBlock()) {
		     startPage = 1;
		     endPage=pdto.getAllPage();
		   }else {*/
			   //전체페이지 수가 블럭보다 크다
			   //현재 페이지 블럭에 따라서 바뀜
			 startPage = (currPageBlock-1)*pdto.getPageBlock()+1;
			 endPage = currPageBlock*pdto.getPageBlock()>pdto.getAllPage()?pdto.getAllPage():currPageBlock*pdto.getPageBlock(); 
				/* } */
		   pdto.setStartPage(startPage);
		   pdto.setEndPage(endPage);
		  
		   //시작 값
		   int sRow=(currentPage-1)*pdto.getLinePerPage()+1;
		   List<BoardDTO> list = dao.getArticles(sRow, currentPage*pdto.getLinePerPage());
		    //view에서 사용할 결과 값 저장
		   req.setAttribute("list", list);
		   //페이지 정보도 저장
		   req.setAttribute("pdto",pdto);
		return "/board2/boardList.jsp";
	}

	
	
	
}
