import java.awt.Color;
	import java.awt.Graphics;
	import java.util.ArrayList;
	import java.util.Iterator;
public class SpecialObstacle{
	ArrayList<Wall> ws=new ArrayList<Wall>();
	public SpecialObstacle(){
		add(new Wall(700,200,100,10));
		add(new Wall(700,210,10,180));
		add(new Wall(700,390,100,10));
		
	}
	public void paint(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.GREEN);
		Iterator<Wall> it=ws.iterator();
		while(it.hasNext()){
			Wall wa=it.next();
			wa.paint(g,Color.green);
		}
		g.setColor(c);
	}

	public void add(Wall wa){
		ws.add(wa);
	}
}
