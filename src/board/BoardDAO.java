package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import common.dbutil.DBConnection;

public class BoardDAO {
 //DAO : Data Access Object
 //DB접근해서 처리하는 작업을 모두 처리하는 객체
 /*
  * DB 처리해야 할 일이 있으면 DAO
  * 인스턴슬 받아서
  * 각각 insert, delete 등 모든 작업을
  * 모듈화 해 놓으면 다른 객체에서 호출해서 사용
  */
	
  private static BoardDAO instance
     = new BoardDAO();
  
  public static BoardDAO getInstance() {
	  return instance;
  }
  
  public int boardWrite(BoardDTO dto) throws NamingException, SQLException {
	  Connection conn= DBConnection.getConnection();
	  int cnt =0;
	  PreparedStatement pstmt =null;
	  ResultSet rs = null;
	  //글번호를 위한 변수
	  int num=dto.getNum();
	  int number = 0;
	  int ref=dto.getRef();
	  int re_step=dto.getRe_step();
	  int re_level=dto.getRe_level();
	  
	  if(conn!=null) {
		  String sql="";
			  //새로운 글번호 만들기
		  //sql = "select boardnum_seq.nextval num from dual";
			  sql = "select max(num)+1 num from board";
			  pstmt = conn.prepareStatement(sql);
			  rs = pstmt.executeQuery();
			  if(rs.next()) {
				 number = rs.getInt("num") ;
			  }
		 if(num==0) {	
			  num=number;
			  ref=num;
			  re_step = 1;
			  re_level=1;
		  }else if(num!=0) {
			  num =number;
			  re_step +=1;
			  re_level+=1;
		  }
		  
		//시퀀스 받은 값을 사용한다.
		  sql="insert into board(NUM,WRITER,";
		  sql+="SUBJECT,EMAIL,CONTENT, PASSWD, ";
		  sql+="REG_DATE,READCNT, REF, ";
		  sql+="RE_STEP, RE_LEVEL, ATTACHNM)";
		  sql+="values(?,?,";
		  sql+="?,?,?,?,sysdate,0,";
		  sql+="?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, num); //dto2.get("num");
		pstmt.setString(2, dto.getWriter());
		pstmt.setString(3, dto.getSubject());
		pstmt.setString(4, dto.getEmail());
		pstmt.setString(5, dto.getContent());
		pstmt.setString(6, dto.getPasswd());
		pstmt.setInt(7, ref); //그룹
		pstmt.setInt(8, re_step);//답글의 단계
		pstmt.setInt(9, re_level);//답글의 깊이
		pstmt.setString(10, dto.getAttachNm());//답글의 깊이
		
		cnt = pstmt.executeUpdate();
	  }
	  
	  if (rs!=null) rs.close();
      if(pstmt!=null)pstmt.close();
      if(conn!=null) conn.close();
		  
	 return cnt;
  }
  
  public List<BoardDTO> getArticles(int sRow,
		                int pageSize) throws NamingException, SQLException{
	  String sql= "SELECT A.*"; 
			sql+=" FROM (SELECT ROWNUM RR, RBOARD.*                                ";
			sql+="         FROM  (SELECT NUM, WRITER, SUBJECT, EMAIL, CONTENT,    ";
			sql+="                       PASSWD, REG_DATE, READCNT, REF, RE_STEP, ";
			sql+="                       RE_LEVEL, ATTACHNM                       ";
			sql+="                 FROM BOARD                                     ";
			sql+="                 ORDER BY REF DESC, RE_STEP ASC) RBOARD) A      ";
			sql+="  WHERE A.RR BETWEEN ? AND ?                                     ";
            
	 Connection conn= DBConnection.getConnection();  
	 List<BoardDTO> articles 
	      = new ArrayList<BoardDTO>(20);
	 PreparedStatement pstmt 
	       = conn.prepareStatement(sql);
	 pstmt.setInt(1, sRow);
	 pstmt.setInt(2, pageSize);
	 ResultSet rs = pstmt.executeQuery();
	 while(rs.next()) {
		 //하나의 정보를 저장하는 작업
		 BoardDTO dto =new BoardDTO();
		 dto.setRr(rs.getInt("rr"));//
		 dto.setNum(rs.getInt("num"));
		 dto.setRef(rs.getInt("ref"));
		 dto.setRe_step(rs.getInt("re_step"));
		 dto.setRe_level(rs.getInt("re_level"));
		 dto.setReadcnt(rs.getInt("readcnt"));
		 dto.setWriter(rs.getString("writer"));
		 dto.setSubject(rs.getString("subject"));
		 dto.setContent(rs.getString("content"));
		 dto.setEmail(rs.getString("email"));
		 dto.setReg_date(rs.getString("reg_date"));
		 dto.setPasswd(rs.getString("passwd"));
		 dto.setAttachNm(rs.getString("attachnm"));
		 articles.add(dto);
	 }
	 if (rs!=null) rs.close();
     if(pstmt!=null)pstmt.close();
	 if(conn!=null) conn.close();
	  return articles;
  }
 
  public BoardDTO getArticle(int num) 
			throws NamingException/* , SQLException */ {
     //조회수 수정하는 작업
     PreparedStatement pstmt =null;
     BoardDTO dto =new BoardDTO();
     ResultSet rs = null;
     try {       
     Connection conn= DBConnection.getConnection(); 
     pstmt
     =conn.prepareStatement("Update board set READCNT=READCNT+1 where num=?");
     pstmt.setInt(1, num);
     int i= pstmt.executeUpdate();
     if(i>0) { 
    //sql
    String sql=" select NUM, WRITER, SUBJECT" ;
           sql+=", EMAIL, CONTENT ,PASSWD";
           sql+=", REG_DATE, READCNT, REF";
           sql+=", RE_STEP, RE_LEVEL, ATTACHNM ";
           sql+=" from board ";
           sql+=" where num = ?";
      //connection 얻기
     //받은 num에 해당되는 데이터 갖고 오는 작업
     pstmt = conn.prepareStatement(sql);
     pstmt.setInt(1, num);
     rs = pstmt.executeQuery();
     if(rs.next()) {
    	 //하나의 정보를 저장하는 작업
		 dto.setNum(rs.getInt("num"));
		 dto.setRef(rs.getInt("ref"));
		 dto.setRe_step(rs.getInt("re_step"));
		 dto.setRe_level(rs.getInt("re_level"));
		 dto.setReadcnt(rs.getInt("readcnt"));
		 dto.setWriter(rs.getString("writer"));
		 dto.setSubject(rs.getString("subject"));
		 dto.setContent(rs.getString("content"));
		 dto.setEmail(rs.getString("email"));
		 dto.setReg_date(rs.getString("reg_date"));
		 dto.setPasswd(rs.getString("passwd")); 
		 dto.setAttachNm(rs.getString("attachNm"));
      }
     }//if close
     if (rs!=null) rs.close();
     if(pstmt!=null)pstmt.close();
     if(conn!=null) conn.close();
    }catch(SQLException e) {
    	e.printStackTrace();
    }
      //connection .닫기	
     return dto;
   }
  
  public int boardUpdate(BoardDTO dto) {
	  int r=0;
	  //DBConnection
	  //prepaeredstatement
	  //실행
	  PreparedStatement pstmt =null;
		Connection conn=null;
	  try {
		String sql = " update board set  "; 
			   sql+=" SUBJECT= ? " ; 
			   sql+=", EMAIL = ?" ; 
			   sql+=", CONTENT = ?" ; 
			   sql+=", PASSWD= ?" ; 
		       sql+=", ATTACHNM = ? " ; 
			   sql+=" where num = ? ";  
	    conn= DBConnection.getConnection();
		if(conn!=null) 
		{
		  pstmt = conn.prepareStatement(sql);
		  pstmt.setString(1, dto.getSubject());
		  pstmt.setString(2, dto.getEmail());
		  pstmt.setString(3, dto.getContent());
		  pstmt.setString(4, dto.getPasswd());
		  pstmt.setString(5, dto.getAttachNm());
		  pstmt.setInt(6, dto.getNum());
		  r = pstmt.executeUpdate();
		   }
	} catch (NamingException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} 
	 try { 
     if(pstmt!=null)pstmt.close();
     if(conn!=null) conn.close();
	 }catch(SQLException e) {
	    	e.printStackTrace();
	    }
	  return r;
  }
   public int deleteArticle(int num) {
	   int r=0;
	   PreparedStatement pstmt =null;
	  Connection conn=null;
	   //DB연결
	   try {
		conn = DBConnection.getConnection();
		  //쿼리 만들기
		String sql 
		= "delete from board where num = ?"; 
		 //pstmt만들기
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, num);
		 //실행하고 r=pstmt.executeUpdate();
		 r=pstmt.executeUpdate();
	} catch (NamingException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	   //close
     try { 
	     if(pstmt!=null)pstmt.close();
	     if(conn!=null) conn.close();
		 }catch(SQLException e) {
		    	e.printStackTrace();
		 }
	   //return
	  return r; 
   }

	 public int getAllCount() {
		 //conn, pstmt, rs
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 ResultSet rs =null;
		 int allCnt=0;
		 try {
		 conn= DBConnection.getConnection();
		 String sql = "select count(num) cnt from board";
		 pstmt = conn.prepareStatement(sql);
		 rs = pstmt.executeQuery();
		 if(rs.next()) {
			allCnt= rs.getInt("cnt"); 
		 }
		 //rs, pstmt, conn 닫기
			if(rs!=null) { rs.close();}
			if(pstmt!=null) { pstmt.close();}
			if(conn!=null) { conn.close();}
		 
		 } catch (NamingException e) {
				e.printStackTrace();
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
		return allCnt;
	 }
	 
}
