package kr.edu.mit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class FruitStoreDAOImpl implements FruitStoreDAO {
	Connection conn=null;
	PreparedStatement pstmt=null;
	ResultSet result=null;
	
	@Override
	public void insertFruit(FruitVO vo) {
		//DB연결해서 과일등록
		//JAVA에서 DB연결하는 방법 JDBC
		//1.DB연결
		//1-1. JDBC드라이버 로드
		//1-2. 연결해서 Connection 객체생성
		//2.쿼리작업
		//2-1. 커넥션객체를 가지고 Statement 객체생성
		//2-2. 스테이먼트 객체를 가지고 query 작업 (select 문의결과는 ResultSet 객체로 받아서 작업)
		dbConn();

		try {
			pstmt=conn.prepareStatement("insert into fruit(fruit_name,price,quantity) value(?,?,?)");
			pstmt.setString(1, vo.getFruit_name());  //? 채우기
			pstmt.setInt(2, vo.getPrice()); 
			pstmt.setInt(3, vo.getQuantity());  
			
			pstmt.executeUpdate(); //삽입,삭제,수정시에는 exceuteUpdate()를 -반환값 int 처리된 행의개수
			                      //read(select)시에는 executeQuery() 이용 -반환값 ResultSet 객체를 결과값을 돌려준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		//3.사용후 DB연결 끊기
		// ResultSet,Statement,Connection 객체 닫아주기
		dbClose();
		

		
	}

	@Override
	public ArrayList<FruitVO> listFruit() {
		ArrayList<FruitVO> list = new ArrayList<>();
		//1.db연결
		dbConn();
		
		try {
			//2.쿼리작업후 결과가져오기(ResultSet) 
			pstmt=conn.prepareStatement("select * from fruit order by fruit_code");
			ResultSet rs=pstmt.executeQuery(); 
			//3.리턴타입으로 변환하기
			while(rs.next()) {                 //next() 다음행을 가르킴 , 리턴은 다음행가르키는 성공이면 true 없으면 false
				FruitVO vo = new FruitVO();				
				vo.setFruit_code(rs.getInt("fruit_code"));
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setPrice(rs.getInt("price"));
				vo.setQuantity(rs.getInt("quantity"));
				list.add(vo);
				
				//System.out.println(code+" "+name+" "+price+" "+quantity);
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//3.리턴타입으로 변환하기
		//4.db닫기
		dbClose();
		//5.변환한거 리턴
		
		return list;
	}
	

	
	//db연결
	void dbConn() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");
			System.out.println("연결성공");                                
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	//db닫기
	void dbClose() {
		if(result!= null) try{ result.close(); } catch (SQLException e){e.printStackTrace();} 
		if(pstmt != null) try{ pstmt.close();  } catch (SQLException e){e.printStackTrace();} 
		if(conn  != null) try{ conn.close();  } catch (SQLException e){e.printStackTrace();} 
	}

	@Override
	public void updateQuantityFruit(FruitVO vo) {
		dbConn();
		try {
			pstmt=conn.prepareStatement("update fruit set quantity=quantity+? where fruit_code=?");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
				
		
		
	}

	@Override
	public int totalFruit(FruitVO vo) {
		dbConn();
		int total=-1;
		try {
			pstmt=conn.prepareStatement("select price*? from fruit where fruit_code=?");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());  //Query 완성
			result=pstmt.executeQuery();
			result.next(); //첫번째 행을 가르키고
			total=result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			dbClose();
		}
		return total;
		
		
	}

	@Override
	public void insertSales(int fruit_code,int quantity) {
		dbConn();
		try {
			conn.setAutoCommit(false);  //오토커밋 금지
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			pstmt=conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1,quantity );
			pstmt.executeUpdate();
			
			result=pstmt.executeQuery("select last_insert_id()"); //입력된 키값 확인
			result.next();
			int key=result.getInt(1);
						
			//pstmt.executeLargeUpdate("insert into fruit_has_sales values("  +fruit_code+  "," +key+  ")");
			//or
		
			pstmt.close();
			pstmt=conn.prepareStatement("insert into fruit_has_sales values(?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.executeUpdate();
			
			
						
			pstmt.close();
			pstmt=conn.prepareStatement("update fruit set quantity=quantity-? where fruit_code=?");
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();
			
			conn.commit(); //정상이면 커밋
			
		} catch (SQLException e) {
			System.out.println("판매실패");
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();} //중간에 문제가 생기면 롤백
			e.printStackTrace();
		} finally {
			dbClose();
		}
		

			
		
		
	}

	@Override
	public long totalPrice() {
		dbConn();
		long totalPrice=-1;
		try {
			//수행할 쿼리를 만들고
			String query="select sum(price*sales_quantity)" + 
					" from fruit " + 
					"     join" + 
					"	 (select fruit_fruit_code, sales_date, sales_quantity" + 
					"	  from fruit_has_sales" + 
					"		   join " + 
					"           sales " + 
					"           on fruit_has_sales.sales_sales_code=sales.sales_code" + 
					"      ) t1" + 
					"      on fruit.fruit_code=t1.fruit_fruit_code  ";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			result=pstmt.executeQuery(); //쿼리를 수행해서 결과를 가져오고
			result.next(); //결과에 첫번째 행을 가르키고
			totalPrice=result.getLong(1); //첫번째 속성값을 long타입으로 변화해서 읽어온다.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}

	@Override
	public List<SalesVO> listSales() {
		List<SalesVO> list = new ArrayList<>();
		//1.db연결
		dbConn();
		
		try {
			//2.쿼리작업후 결과가져오기(ResultSet) 
			String query="select sales_code,fruit_name,sales_quantity,sales_date, sales_quantity*price total" + 
					" from sales join" + 
					"	(select * from fruit join fruit_has_sales on(fruit.fruit_code=fruit_has_sales.fruit_fruit_code)) t1" + 
					"    on(sales.sales_code=t1.sales_sales_code)";
			System.out.println(query);
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery(); 
			//3.리턴타입으로 변환하기
			while(rs.next()) {                 //next() 다음행을 가르킴 , 리턴은 다음행가르키는 성공이면 true 없으면 false
				SalesVO vo = new SalesVO();				
				vo.setFruit_code(rs.getInt("sales_code"));
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setTotal(rs.getInt("total"));
				vo.setSales_date(rs.getDate("sales_date"));
				vo.setSales_quantity(rs.getInt("sales_quantity"));
				list.add(vo);
				
				//System.out.println(code+" "+name+" "+price+" "+quantity);
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//3.리턴타입으로 변환하기
		//4.db닫기
		dbClose();
		//5.변환한거 리턴
		
		return list;
	}

}








