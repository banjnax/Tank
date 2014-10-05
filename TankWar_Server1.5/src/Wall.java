import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * �������ڹ����ϰ���������ֻ��Ҫ֪�����췽�����У������Ĳ���Ҫ
 * @author banjnax
 *
 */
public class Wall {
	int x,y,w,h;

	ArrayList<Rectangle> partsWall=new ArrayList<Rectangle>();
	
	public Wall(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

		if(w>h){
			for(int i=x;i<x+w;i+=10){
				this.partsWall.add(new Rectangle(i,y,10,5));
				this.partsWall.add(new Rectangle(i,y+5,10,5));
			}
		}
		else{
			for(int i=y;i<y+h;i+=10){
				this.partsWall.add(new Rectangle(x,i,5,10));
				this.partsWall.add(new Rectangle(x+5,i,5,10));
			}
		}
	
	}
	public void paint(Graphics g,Color c){
		//Color c=g.getColor();
		g.setColor(c);
		Iterator<Rectangle> it=partsWall.iterator();
		while(it.hasNext()){
			Rectangle r=it.next();
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		//g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
}
