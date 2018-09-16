<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>전국노래자랑</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" />
<script src="<c:url value='/resources/js/common.js'/>" charset="utf-8"></script>
<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
</head>
<body>
	<script type="text/javascript">
	// 페이지 로딩이 완료되고, jQuery 스크립트 실행된다
	$(document).ready(function() {

		showSF02List();
		
		// 저장 버튼을 클릭할때 이벤트 발생
		$("#btn_insert_button").on("click", function(e) {
			
			if(!fn_PreSave()) {
				return ;
			}
			
			re_showSF02List();
		});
		
		// 삭제 버튼을 클릭할때 이벤트 발생
		$("#button_delete").on("click", function(e) {
			if(confirm("삭제할까요?")) {
				$("#form_delete").submit();	
			}
		});
		
		// 좋아요 버튼을 클릭할때 이벤트 발생
		$("#button_like").on("click", function(e) {
			if(confirm("좋아요 할까요?")) {
				like_sf01();
			}
		});
		
		// 싫어요 버튼을 클릭할때 이벤트 발생
		$("#button_hate").on("click", function(e) {
			if(confirm("싫어요 할까요?")) {
				hate_sf01();
			}
		});
		
		fn_PreSave = function() {

			if(gfn_isNull($("#form #text").val().trim())) {
				alert("댓글 내용을 입력하세요");
				return false;
			}
	
			return true;
		}	
	});
	
	function deleteSF02(idx) {
		
		if(confirm("삭제할까요?")) {
			
			var sendData = JSON.stringify({
		    	"seq" : $("#seq" + idx).text(),
		    	"seq01" : $("#seq01" + idx).text()
		    });
			
			gfn_ajax("sf02delete.do","POST" , sendData , function(data) {
				var html = "";
		        for (var i = 0; i < data.list.length; i++) {
		            html += '<tr>';
		            html += '<td scope="col" width="50">' + data.list[i].userid + '</td>';
		            html += '<td scope="col" width="100">' + data.list[i].text + '</td>';
		            html += '<td scope="col" width="30">' + data.list[i].good + '</td>';
		            html += '<td scope="col" width="70">' + gfn_dateFormat(data.list[i].regdate) + '</td>';
		            if(data.list[i].deleteYn) {
		            	html += '<td scope="col" width="50">' + 
		            	'<input type="button" value="삭제" onclick="deleteSF02('+ i +')">'
		            	+ '</td>';
		            	 html += '<td id="seq' + i + '" style="display:none;">' + data.list[i].seq + '</td>';
		            	 html += '<td id="seq01' + i + '" style="display:none;">' + data.list[i].seq01 + '</td>';
		            } else {
		            	html += '<td scope="col" width="50"></td>';
		            }
		            
		            html += '</tr>';
		        }

		        $("#SF02viewTbody").html(html);
		    	 // 페이징 함수 호출
		        gfn_paging("pagenation", "showSF02List" , data.size);
			});
		}
	}
	
	function showSF02List(nowPage) {
		
		if(!gfn_isNull(nowPage)) {
			page = nowPage;
		} else {
			page = 0;
		}
		
		var sendData =  JSON.stringify({
	    	"seq01" : parseInt($("#seq01").val()),
	    	"nowPage" : page
	    });
		
		
		gfn_ajax("sf02show.do","POST" , sendData , function(data) {
			var html = "";
			
			for (var i = 0; i < data.list.length; i++) {
	        	 html += '<tr>';
		         html += '<td scope="col" width="50">' + data.list[i].userid + '</td>';
		         html += '<td scope="col" width="100">' + data.list[i].text + '</td>';
		         html += '<td scope="col" width="30">' + data.list[i].good + '</td>';
		         html += '<td scope="col" width="70">' + gfn_dateFormat(data.list[i].regdate) + '</td>';
		         if(data.list[i].deleteYn) {
		          	html += '<td scope="col" width="50">' + 
		           	'<input type="button" value="삭제" onclick="deleteSF02('+ i +')">'
		           	+ '</td>';
		           	 html += '<td id="seq' + i + '" style="display:none;">' + data.list[i].seq + '</td>';
		           	 html += '<td id="seq01' + i + '" style="display:none;">' + data.list[i].seq01 + '</td>';
		         } else {
		          	html += '<td scope="col" width="50"></td>';
		         }   
		         html += '</tr>';
	        }

	        $("#SF02viewTbody").html(html);
	        
	     	// 페이징 함수 호출
	        gfn_paging("pagenation", "showSF02List" , data.size);
		});
	};
	
	function re_showSF02List() {
	
		
		var sendData = JSON.stringify({
			"seq01" : parseInt($("#seq01").val()),
        	"text" : $("#form #text").val().trim()
        });

		gfn_ajax("sf02insert.do","POST" , sendData , function(data) {
			var html = "";
	        for (var i = 0; i < data.list.length; i++) {
	            html += '<tr>';
	            html += '<td scope="col" width="50">' + data.list[i].userid + '</td>';
	            html += '<td scope="col" width="100">' + data.list[i].text + '</td>';
	            html += '<td scope="col" width="30">' + data.list[i].good + '</td>';
	            html += '<td scope="col" width="70">' + gfn_dateFormat(data.list[i].regdate) + '</td>';
	            if(data.list[i].deleteYn) {
	            	html += '<td scope="col" width="50">' + 
	            	'<input type="button" value="삭제" onclick="deleteSF02('+ i +')">'
	            	+ '</td>';
	            	 html += '<td id="seq' + i + '" style="display:none;">' + data.list[i].seq + '</td>';
	            	 html += '<td id="seq01' + i + '" style="display:none;">' + data.list[i].seq01 + '</td>';
	            } else {
	            	html += '<td scope="col" width="50"></td>';
	            }
	            
	            html += '</tr>';
	        }
	        $("#SF02viewTbody").html(html);
	    	 // 페이징 함수 호출
	        gfn_paging("pagenation", "showSF02List" , data.size);
		});
	};
	
	function like_sf01() {
		
		var sendData = JSON.stringify({
			"seq" : parseInt($("#seq01").val()),
        	"title" : $("#title").val(),
			"good" : parseInt($("#good").text())
        	
        });
		
		gfn_ajax("sf01like.do","POST" , sendData , function(data) {
			if(data.result == 1) {
				$("#good").text(data.like);
				$("#button_like").attr('disabled', true);
			
			}
		});
	}
	
	function hate_sf01() {
		
		var sendData = JSON.stringify({
			"seq" : parseInt($("#seq01").val()),
        	"title" : $("#title").val(),
			"good" : parseInt($("#good").text())
        });
		
		gfn_ajax("sf01hate.do","POST" , sendData , function(data) {
			if(data.result == 1) {
				$("#good").text(data.like);
				$("#button_hate").attr('disabled', true);
			}
		});
	}
</script>
 <jsp:include page="banner.jsp" /> 
	<header>
	<h1 class="logo">
		<span class="word1">전국</span> <span class="word2">노래자랑</span>
	</h1>
	</header>
	<nav> <jsp:include page="sidebar.jsp" /> </nav>
	<section>
		<div>
		<form id="form_delete" method="post" action="/common/sf01delete.do">
						
			<input type="hidden" id="seq01" name="seq" value="${sf01Vo.seq}" />
			<input type="hidden" id="title" name="title" value="${sf01Vo.title}" />
			<p>작성자 : ${sf01Vo.userid}</p>
			<p>제목 : ${sf01Vo.title}</p>
			<p>등록일자 : ${sf01Vo.showDate}</p>
			<c:if test="${sf01Vo.deleteYn}" var="result" scope="page">
       			<input id="button_delete" type="button" value="삭제">
    		</c:if>
    	</form>
		</div>
		<div>
			<div>내용 :<p>${sf01Vo.text}</p></div>
			<div>조회수 :<p>${sf01Vo.hit}</p></div>
			<div>좋아요 :<p id="good">${sf01Vo.good}</p></div>
			
			<c:choose>
				<c:when test="${sf01Vo.goodlog!='Y'}">
					<input id="button_like" type="button" value="좋아요">
				</c:when>
				<c:otherwise>
					<input id="button_like" type="button" value="좋아요" disabled>
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${sf01Vo.hatelog!='Y'}">
					<input id="button_hate" type="button" value="싫어요">
				</c:when>
				<c:otherwise>
					<input id="button_hate" type="button" value="싫어요" disabled>
				</c:otherwise>
			</c:choose>
			
			<p>파일명 : ${sf01Vo.filename}</p>
			<a href="/common/selectFile.do?seq=${sf01Vo.seq}&regdate=${sf01Vo.regdate}">파일 받기</a>
		</div>
		
		<div class="container">
		<form id="form" method="post"  action="/common/sf02insert.do">
				댓글 내용<textarea id="text" name="text" rows="2" cols="40">
				</textarea>
				<input type="hidden" id="seq01" name="seq01" value="${sf01Vo.seq}" />
				<button id="btn_insert_button" type="button" >저장</button>
		</form>
		<table border = "1">
			<colgroup>
				<col width="50">
				<col width="200">
				<col width="30">
				<col width="70">
				<col width="50">
			</colgroup>
			<thead>
			<tr>
				<th scope="col">글쓴이</th>
				<th scope="col">내용</th>
				<th scope="col">좋아요</th>
				<th scope="col">등록일자</th>
				<th scope="col">삭제</th>
			</tr>
			</thead>
			<tbody id="SF02viewTbody">
		
			</tbody>
		</table>
		<div id="pagenation">
		
		</div>
		</div>
	</section>
	<p>
	<footer> <jsp:include page="footer.jsp" /> </footer>
</body>
</html>
