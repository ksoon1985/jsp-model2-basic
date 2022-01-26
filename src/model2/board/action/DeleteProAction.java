package model2.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardDAO;
import board.PageDTO;

public class DeleteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse res) throws Throwable {
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
		   int num 
		     =Integer.parseInt(req.getParameter("num"));
		    //DAO 가져오기
		    BoardDAO dao = BoardDAO.getInstance();
		    // DAO메소드실행
		    int r =dao.deleteArticle(num);
		    req.setAttribute("r", r);
		return "/board2/deletePro.jsp";
	}

}
