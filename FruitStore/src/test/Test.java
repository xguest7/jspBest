package test;

import kr.edu.mit.FruitStoreDAO;
import kr.edu.mit.FruitStoreDAOImpl;
import kr.edu.mit.FruitVO;
import kr.edu.mit.SalesVO;

public class Test {

	public static void main(String[] args) {
		// �ڹ�Ŭ���� �׽�Ʈ
		FruitStoreDAO dao = new FruitStoreDAOImpl();
//		FruitVO vo = new FruitVO();
//		vo.setFruit_name("apple");
//		vo.setPrice(999);
//		vo.setQuantity(99);
//		dao.insertFruit(vo);
		
		
		for(SalesVO vo : dao.listSales()) {
			System.out.println(vo);
		}
		
	}

}
