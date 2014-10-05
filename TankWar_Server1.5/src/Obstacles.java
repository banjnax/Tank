import java.util.ArrayList;
/**
 * 这个class是对整个游戏的障碍物的设计，可以在这里设计你认为的障碍物
 * @author banjnax
 *
 */

public class Obstacles {
int index;
ArrayList<Obstacle> os=new ArrayList<Obstacle>();
public Obstacles(){
	//第一关
	Obstacle o1=new Obstacle();
	o1.add(new Wall(150,200,400,10));
	o1.add(new Wall(350,200,10,300));
	os.add(o1);
	

	//第二关
	Obstacle o2=new Obstacle();
	o2.add(new Wall(150,200,400,10));
	os.add(o2);
	
	//第三关
	Obstacle o3=new Obstacle();
	o3.add(new Wall(150,200,400,10));
	os.add(o3);
	//障碍物实现
	/*

	//第N关
	Obstacle o1=new Obstacle();
	//障碍物实现
	os.add(o1);
*/
	
}
public Obstacle get(int index){
	return os.get(index);
}
}
