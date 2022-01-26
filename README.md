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
