package kr.edu.mit;

import java.util.ArrayList;
import java.util.List;

public interface FruitStoreDAO {
	
	////////////�������� ����////////////
	//���ϵ��
	void insertFruit(FruitVO vo);
	//���ϸ�Ϻ����ֱ�
	ArrayList<FruitVO> listFruit();
	//����������Ʈ
	void updateQuantityFruit(FruitVO vo);
	
	//���Ϻ� �Ѱ��ݾ˷��ֱ�
	int totalFruit(FruitVO vo);
	
	//�Ǹ�ó��
	//1.�Ǹų��� �߰� - 2.�Ǹų��� �߰� Ű�� �˾ƿ��� - 3.�������̺� ����(�߰�) - 4.������ó��
	void insertSales(int fruit_code,int quantity);  //Ű���� ����
	
	//���Ǹűݾ�
	long totalPrice();
	
	//���⳻������
	List<SalesVO> listSales();
	
	    
	
}
