$(function(){
  $('#reply').click(function(){
	  document.parentForm.action='writeForm.do';
	  document.parentForm.submit();	  
  });
  
  $('#list1').click(function(){
	  document.parentForm.action='list.do';
	  document.parentForm.submit();	
	  });
  
  $('#list2').click(function(){
//	  document.writeForm.action='list.do';
//	  document.writeForm.submit();	
	  javascript:location.href='list.do';
	  });
  
  $('#list3').click(function(){
	  document.updateForm.action='list.do';
	  document.updateForm.submit();	
	  });
  })
  
  
  
  