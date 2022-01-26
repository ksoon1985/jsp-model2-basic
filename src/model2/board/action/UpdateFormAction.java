package model2.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardDAO;
import board.BoardDTO;
import board.PageDTO;

public class UpdateFormAction implements CommandAction {

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
		   int num = 
				Integer.parseInt(req.getParameter("num"));
			//DAO를 통해서 해당 글 갖고오기
		    BoardDAO dao = BoardDAO.getInstance();
		    //해당 인스턴스에서 해당되는 메소드 실행
		    BoardDTO dto = dao.getArticle(num);
		    //setAttribute를 해줘야 됨
		    req.setAttribute("dto", dto);
		
		
		return "/board2/updateForm.jsp";
	}

}
