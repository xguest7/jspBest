package kr.edu.mit;

import java.util.List;
import java.util.Scanner;

public class StoreMain {

	public static void main(String[] args) {
		//1.�����԰�, 2.����ľ�, 3.�Ǹ��ϱ�, 4.����Ȯ��
		Scanner in = new Scanner(System.in);
		
		FruitStoreDAO dao = new FruitStoreDAOImpl();
		
		int menuNum;
		do {
			//�Ŵ�����ϱ�
			System.out.println("1.�����Է�");
			System.out.println("2.����ľ�");
			System.out.println("3.�Ǹ��ϱ�");
			System.out.println("4.����Ȯ��");
			System.out.print("�Ŵ��� �������ּ���.(0�� ����): ");
			
			//������Է¹ް� �ش�޴� �����ϱ�
			menuNum=in.nextInt();
			if(menuNum==1) {
				System.out.println("�����Է±���Դϴ�.");
				//1.���ϸ�Ϻ����ֱ�
				//2.�������� �߰����� �Է¹ް�
				//3-1.������ ���
				//    �԰���� �ް� DBó��(������Ʈ) ->��������� ���� �����ڵ�,����
				//3-2.�߰��� ���
				//    �����̸�,����,���� �ް� DBó��(����) 
		
			}else if(menuNum==2) {
				System.out.println("����ľǱ���Դϴ�.");
			}else if(menuNum==3) {
				//1.���ϸ�Ϻ����ֱ� -(DB)���ϸ�Ϻ����ֱ� 
				List<FruitVO> list =dao.listFruit();
				for(FruitVO vo: list) {
					System.out.println(vo);
				}
				System.out.println("���ϸ���Դϴ�.���Ϲ�ȣ�� �������ּ��� : ");
				
				//2.����ڰ� ����(�ڵ�,����)
				int fruit_code=in.nextInt(); //���Ϲ�ȣ �Է�
				System.out.println("������ ������ �Է��� �ּ���: ");
				int quantity=in.nextInt(); //���ϼ���
								
				//3.���ұݾ׾ȳ� -(DB)���Ϻ� �Ѱ��ݾ˷��ֱ�
				FruitVO vo = new FruitVO();
				vo.setFruit_code(fruit_code);
				vo.setQuantity(quantity);
				System.out.println("�ѱ��űݾ���"+dao.totalFruit(vo)+"�Դϴ�.");
				System.out.println("�����Ͻðڽ��ϱ�?(1:����, 2:���)");
				
				if(in.nextInt()==1) {
					//4.�ǸſϷ� -(DB)�Ǹ�ó��
					System.out.println("�Ǹ�DB�ۼ� ��");
					dao.insertSales(fruit_code, quantity);
				}
			}else if(menuNum==4) {
				System.out.println("����Ȯ�α���Դϴ�.");
			}else if(menuNum==0) {
				System.out.println("�̿����ּż� �����մϴ�.");
			}
		}while(menuNum!=0);
	}

}
