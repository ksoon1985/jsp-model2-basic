package common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import board.BoardDTO;
import board.PageDTO;

public class ServletUpload {
	    private static final int MEMORY_THRESHOLD   
	        = 1024 * 1024 * 3;  // 3MB
	    private static final int MAX_FILE_SIZE      
	       = 1024 * 1024 * 5; // 5MB
	    private static final int MAX_REQUEST_SIZE   
	        = 1024 * 1024 * 5; // 5MB
	   //메소드
  public static Map<String,Object> 
       uploadEx(HttpServletRequest req, 
    		        HttpServletResponse res) {
		PageDTO pdto= new PageDTO();
	    BoardDTO dto =  new BoardDTO();
		try {
			//1. upload 절대 경로 지선언
		    String UPLOAD_DIR = "D:/apri_ncs/upload";
		    String newfileName ="";
		    //2.파일을 읽기위한 객체 생성 
		    DiskFileItemFactory factory = 
		    		 new DiskFileItemFactory();
		    // 3. 업로드 요청을 처리하는 ServletFileUpload 생성
		    ServletFileUpload upload = 
		    		  new ServletFileUpload(factory);
		    // 4. 업로드 요청 파싱해서 FileItem 목록 구함
		    List<FileItem> items;
				items = upload.parseRequest(req);
		    //순서대로 가져오기
		    Iterator<FileItem> iter = items.iterator();
		    int currentPage =0;
		    int currPageBlock=0;
		    while(iter.hasNext()){
		        FileItem item = iter.next();
		        String name = null;
		        String value = null;
		        // 파일인지 여부 확인 : isFormFile() 
		            //-> type=file 이외 폼 데이터 인지 확인
		            if (item.isFormField()) { // 텍스트 입력인 경우
		                 name = item.getFieldName(); //태그 name
						 value = item.getString("utf-8");
						
		              if(name.equals("currentPage")) {
	            	     if(value==null || value.equals("0")) {
			       			 currentPage = 1; 
			       		   }else {
			       			 currentPage=Integer.parseInt(value);
			       		   }  
		              }
		              if(name.equals("currPageBlock")) {
		            	  if(value==null|| value.equals("0")) {
		            		   currPageBlock = 1; 
				       		 }else {
				       			currPageBlock=
				       					Integer.parseInt(value);
				       		 } 
		              }
		       			pdto.setCurrentPage(currentPage);
		       			pdto.setCurrPageBlock(currPageBlock);	
		       		    //  writeForm에서 보내준 데이터 모두 받기
			       		//num, ref, re_step, re_level
			       		//writer, subject, content, passwd, email, attachNm
	       			 if(name.equals("num")) 
		            	  dto.setNum(Integer.parseInt(value));
	       			 if(name.equals("ref"))
		       			dto.setRef(Integer.parseInt(value));
	      			 if(name.equals("re_step"))
	      				 dto.setRe_step(Integer.parseInt(value));
	      			 if(name.equals("re_level"))
	      				 dto.setRe_level(Integer.parseInt(value));
	      			 if(name.equals("writer")) dto.setWriter(value);
	      			 if(name.equals("subject")) dto.setSubject(value);
	      			 if(name.equals("content")) dto.setContent(value);
	      			 if(name.equals("email")) dto.setEmail(value);
	      			 if(name.equals("passwd")) dto.setPasswd(value);
//	      			 Map<String, String> dto2 
//	      			    = new HashMap<String, String>();
//	      			 dto2.put(name, value);
		            }else{//아니면 다른 필드냐..
		              name = item.getFieldName();
		              String fileName = item.getName(); //파일이름
		              String contentType = item.getContentType();
		              long sizeInBytes = item.getSize(); //파일 사이즈
		              if(item.getString("utf-8")!=null 
		            		  && !item.getString("utf-8").equals("")) {
		              //현재 날짜시간분까지 생성해서 파일명에 적용
			              String now = 
			            	  new SimpleDateFormat("yyyyMMddHm").
			            	        format(new Date());  //현재시간
			              int ext=-1;
			              ext=fileName.lastIndexOf('.');
			              // 저장하고자 하는 파일의 이름
			              newfileName = fileName.substring(0,ext)
			              		    +"_"+now+fileName.substring(ext,
			              		    		fileName.length());
			              // 데이터 저장 File(위치, 파일명)
			              dto.setAttachNm(UPLOAD_DIR+"/"+newfileName);
			              item.write(new File(UPLOAD_DIR, newfileName));
		              }
		            }
		        }
		    } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		    } catch (FileUploadException e1) {
				e1.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		Map<String,Object> multiDTO =
				new HashMap<String,Object>();
		multiDTO.put("pdto",pdto);
		multiDTO.put("dto",dto);
		return multiDTO;
	  }
	
}
