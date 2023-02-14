<%@page import="java.util.List"%>
<%@page import="kr.edu.mit.SalesVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>매출확인</title>
</head>
<body>
<%
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	List<SalesVO> list = dao.listSales();
%>
<table border="1">
	<caption>[판매목록]</caption>
	
	<tr> <th>과일코드</th> <th>과일이름</th> <th>판매수량</th> <th>판매금액</th> <th>판매일자</th>
	</tr>
<%	
	for(SalesVO vo: list){ %>
	<tr>
		<td><%=vo.getFruit_code() %></td>
		<td><%=vo.getFruit_name() %></td>
		<td><%=vo.getSales_quantity() %></td>
		<td><%=vo.getTotal() %></td>
		<td><%=vo.getSales_date() %></td>
	</tr>
<%	}
%>	
	<tr> <td colspan="5"><button type="button" onclick="gotitle()">매뉴로 돌아가기</button></td>
</table>

<script>
function gotitle(){
	history.back(); //뒤로가기
}
</script>

</body>
</html>