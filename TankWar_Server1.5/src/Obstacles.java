import java.util.ArrayList;
/**
 * ���class�Ƕ�������Ϸ���ϰ������ƣ������������������Ϊ���ϰ���
 * @author banjnax
 *
 */

public class Obstacles {
int index;
ArrayList<Obstacle> os=new ArrayList<Obstacle>();
public Obstacles(){
	//��һ��
	Obstacle o1=new Obstacle();
	o1.add(new Wall(150,200,400,10));
	o1.add(new Wall(350,200,10,300));
	os.add(o1);
	

	//�ڶ���
	Obstacle o2=new Obstacle();
	o2.add(new Wall(150,200,400,10));
	os.add(o2);
	
	//������
	Obstacle o3=new Obstacle();
	o3.add(new Wall(150,200,400,10));
	os.add(o3);
	//�ϰ���ʵ��
	/*

	//��N��
	Obstacle o1=new Obstacle();
	//�ϰ���ʵ��
	os.add(o1);
*/
	
}
public Obstacle get(int index){
	return os.get(index);
}
}
