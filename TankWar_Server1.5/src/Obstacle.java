import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * 这是障碍物模块
 * 用于每一关的障碍物，可以直接把他画出来
 * @author banjnax
 *
 */

public class Obstacle {
	
ArrayList<Wall> ws=new ArrayList<Wall>();
public void paint(Graphics g){
	Color c=g.getColor();
	g.setColor(Color.gray);
	Iterator<Wall> it=ws.iterator();
	while(it.hasNext()){
		Wall wa=it.next();
		wa.paint(g,Color.gray);
	}
	g.setColor(c);
}

public void add(Wall wa){
	ws.add(wa);
}
}
