<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 
    uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ page import="board.*" %>       
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link href="./css/board.css" rel="stylesheet">          
<script type="text/javascript" src="./js/boardScript.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="./js/jquery_board.js" type="text/javascript"></script>
</head>
<body>
<form action="writePro.do" method="post" name="writeForm" onsubmit="return sendData()"
       enctype="multipart/form-data" >
    <input type="text" name="num" value="${num}">
    <input type="text" name="ref" value="${ref}">
    <input type="text" name="re_step" value="${re_step}">
    <input type="hidden" name="re_level" value="${re_level}">
    <input type="text" name="currentPage" value='<c:out value="${pdto.currentPage}"/>'/> 
    <input type="text" name="currPageBlock" value='<c:out value="${pdto.currPageBlock}"/>'/>
  <table>
    <thead class="class01 class-center">
      <tr>
         <th colspan=2 > <h3>글쓰기</h3> 
         </th>
       </tr>
    </thead>
    <tbody class="class02 class-left">
        <tr>
            <th>제목: </th>
            <c:if test="${num==0}">
            <td>
            	<input type="text" size="80%" placeholder="제목을 입력하세요. " name="subject"/>
            </td>
            </c:if>
            <c:if test="${num!=0}">
            <td>
            	<input type="text" size="80%" placeholder="제목을 입력하세요. " name="subject" value="[답글]"/>
            </td>
            </c:if>
        </tr>
        <tr>
            <th > 내용: </th>
            <td>
            	<textarea cols="80" rows="20" placeholder="내용을 입력하세요. " name="content"></textarea>
            </td>
        </tr>
        <tr>
            <th>첨부파일: </th>
            <td class="class-left"><input type="file" 
                  placeholder="파일을 선택하세요. " 
                  name="filename"/></td>
        </tr>
        <tr>
            <th>작성자: </th>
            <td><input type="text" 
                 placeholder="작성자를 입력하세요" 
                 name ="writer"/></td>
        </tr>
        <tr>
            <th>이메일: </th>
            <td><input type="text" 
                 placeholder="메일주소를 작성해 주세요" 
                 name ="email"/></td>
        </tr>
        <tr>
            <th>비밀번호: </th>
            <td><input type="password" 
                 placeholder="비밀번호를 입력하세요" 
                 name ="passwd"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" name="submit" value="등록"/>
                <input type="button"  name="listbtn" value="글 목록으로... " id="list2"/>
            </td>
        </tr>
    </tbody>
  </table>
</form>
</body>
</html>