import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * �����ϰ���ģ��
 * ����ÿһ�ص��ϰ������ֱ�Ӱ���������
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
