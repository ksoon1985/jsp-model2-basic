# jsp-model2-basic

soldesk academy에서 배운 model 2 방식 게시판 프로젝트

## java
* board
  * BoardDAO - DB 로직 처리
  * BoardDTO - 게시판 DTO
  * PageDTO - 페이징 DTO
  
* common
  * ServletUpload 
  * dbutil 
    * CommonEncodingFilter 
    * DBConnection

* model2.board
  * ControllerAction - init : Command.properties 초기화, do get,post 서블릿 역활(*.do)
  * Command.properties - url mapping
  * action
    * CommandAction(interface) 
    * ContentAction - 목록
    * DeleteProAction - 삭제
    * ListAction - 리스트
    * UpdateFormAction - 수정폼
    * UpdateProAction - 수정
    * WriteFormAction - 작성 폼
    * WriteProAction - 작성 


## jsp
* view

## js
* boardScript - validation chk
* jquery 

## web-inf
* web.xml
  * 서블릿 설정 model2.board.ControllerAction - boardServlet (configProperty -> C:\~~\board\Command.properties)
  * 인코딩 필터 common.dbutil.CommonEncodingFilter - Encoding Filter

## lib
* commons-fileupload-1.4
* commons-io-2.7
* cos
* jai_codec
* jai_core
* jstl-1.2
* ojdbc8

## db
* oracle - 21 

## 기본코드
기본적인 동작방법은 이러하다  
### ControllerAction

```java

public class ControllerAction extends HttpServlet {
	
	 private Map<String,Object> commandMap = new HashMap<String,Object>();
	
 /*
  서블릿 최초 요청 시 서블릿 컨테이너에 의해 서블릿 객체 생성
  -> 이후 같은 요청이 오면 앞서 만든 서블릿 객체 사용
  
  최초 생성시 init() 함수 호출
  
  web.xml에 정의해둔 
  <servlet-mapping>
      <servlet-name>boardServlet</servlet-name>
      <url-pattern>*.do</url-pattern>
  </servlet-mapping>
  <servlet>
     <servlet-name>boardServlet</servlet-name>
     <servlet-class>model2.board.ControllerAction</servlet-class>
     <init-param>
	     <description>Model2 Task Properties</description>
	     <param-name>configProperty</param-name>
	     <param-value>C:...\src\model2\board\Command.properties</param-value>
     </init-param>
  </servlet>
  
  configProperty 파라미터를 통해 Command.properties 파일을 로딩 -> commandMap에 저장
 */
	 public void init(ServletConfig config) {
	 String props = config.getInitParameter("configProperty");
		Properties pr = new Properties();
		FileInputStream f=null;
		try {
		   f=new FileInputStream(props)	; //props : Command.properties자료 위치 
		   pr.load(f);
		} ...
  
		Iterator<Object> key = pr.keySet().iterator();
		while(key.hasNext()) {
		String command = (String) key.next();
		String className= pr.getProperty(command);
		try {
				Class commansClass = Class.forName(className);
				Object commandInstance = commansClass.newInstance();
				commandMap.put(command,commandInstance);
		} ...
	}//init end 
		
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    requestPro(request, response);}
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   doGet(request, response);}
  
  private void requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

	  CommandAction com=null;
	  try {
	    String command = request.getRequestURI();
	    if(command.indexOf(request.getContextPath())==0) {
	    	command = command.substring(request.getContextPath().length());
	    } // command : /board2/list.do (ex)
	    com = (CommandAction)commandMap.get(command);

			view = com.requestPro(request, response);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	    //forward view로 
	    RequestDispatcher dispatcher = request.getRequestDispatcher(view);	
	    dispatcher.forward(request, response);
	}
	}
}
```

### Command.properties
```
/board2/list.do=model2.board.action.ListAction
/board2/writeForm.do=model2.board.action.WriteFormAction
/board2/writePro.do=model2.board.action.WriteProAction
/board2/content.do=model2.board.action.ContentAction
/board2/updateForm.do=model2.board.action.UpdateFormAction
/board2/updatePro.do=model2.board.action.UpdateProAction
/board2/deletePro.do=model2.board.action.DeleteProAction
```

### CommandAction
```java
// 다형성을 위해 
public interface CommandAction {
	public String requestPro(HttpServletRequest req, HttpServletResponse res) throws Throwable;
}

// 구현체 : ListAction, ContentAction, 등...

```

## 페이징 코드
다시보니 이게 뭐어지... -> 참고용으로 올려둠 
### ListAction

```java
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
     
     /*
     if currPageBlock == 2 -> startPage = 6 , endPage = 10
     */
		   
		   int startPage = 1;
		   int endPage=1;

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
```

### boardList.jsp
```jsp
	 <h5 style="text-align: center">
	    <c:if test="${pdto.startPage>pdto.pageBlock}">
	        <a href="list.do?currentPage=${pdto.startPage-pdto.pageBlock}&currPageBlock=${pdto.currPageBlock-1}"
	          style=" text-decoration: none;"> [이전]</a>
	    </c:if>
	    <c:forEach var="i" begin="${pdto.startPage}" end="${pdto.endPage}">
	      <a href="list.do?currentPage=${i}&currPageBlock=${pdto.currPageBlock}" style=" text-decoration: none;">
	             <c:out value="[${i}]"/>&nbsp;
	      </a>
	    </c:forEach>
	    <c:if test="${pdto.endPage<pdto.allPage}">
	        <a href="list.do?currentPage=${pdto.endPage+1}&currPageBlock=${pdto.currPageBlock+1}"
	          style=" text-decoration: none;"> [다음]</a>
	    </c:if>
	  </h5>
```
