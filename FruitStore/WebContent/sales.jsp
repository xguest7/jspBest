<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일구매</title>
</head>
<body>
<%
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	ArrayList<FruitVO> list=dao.listFruit();
	
%>


<table border="1">
	<tr> <th>과일코드</th> <th>이름</th> <th>가격</th> <th>재고</th>
	</tr>
<%
	for(FruitVO vo:list){ %>
		<tr> <td><%=vo.getFruit_code()%></td>
		     <td><%=vo.getFruit_name()%></td> 
		     <td><%=vo.getPrice()%></td> 
		     <td><%=vo.getQuantity()%></td></tr>   
<%	}
%>	
	
	
</table>
<form action="sales.jsp" method="post">
	과일코드:<input type="number" name="fruit_code">
	, 구매수량:<input type="number" name="sales_quantity">
	<input type="submit" value="확인"> <br>

</form>
<%
	String fruit_code=request.getParameter("fruit_code");  //확인시 입력한 과일코드
	String sales_quantity=request.getParameter("sales_quantity"); //확인시 입력한 구매수량
	if((fruit_code!=null && sales_quantity!=null)){  //총가격을 보여줄려면 최소접속이 아니고
		if((fruit_code.length()!=0 && sales_quantity.length()!=0)){  //두개다 값이 있어야 한다.
			FruitVO vo = new FruitVO();
			vo.setFruit_code(Integer.parseInt(fruit_code));
			vo.setQuantity(Integer.parseInt(sales_quantity)); 
			int price=dao.totalFruit(vo);  //구매가격 알아오기
%>
<form action="salesPro.jsp" method="post">
	<input type="hidden" name="fruit_code" value="${fruit_code}">
	<input type="hidden" name="sales_quantity" value="${sales_quantity}">
	총가격은 ${price}원입니다. 
	<input type="submit" value="구매">
</form>
<%	}
} %>
</body>
</html>