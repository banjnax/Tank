import java.awt.*;
public class Boom {
	
	int x,y;
	int[] diameter={1,3,6,10,15,20,30,40,20,10,4,1};
	int step=0;
	private boolean live=true;
	private TankServer t;
	
	public Boom(int x, int y,TankServer t) {
		this.x = x;
		this.y = y;
		this.t=t;
	}

	public void paintBoom(Graphics g){
		if(!live){
			t.booms.remove(this);
			return;
		}
		if(step==diameter.length){
			step=0;
			this.live=false;
		}
		Color c=g.getColor();
		if((step>=0&&step<3)||(step>=9&&step<11)){
			g.setColor(Color.white);
			g.fillOval(x+bullet.WIDTH/2-diameter[step]/2, y+bullet.HEIGHT-diameter[step]/2, diameter[step], diameter[step]);
			g.setColor(c);
		}
		else{
		g.setColor(Color.cyan);
		g.fillOval(x+bullet.WIDTH/2-diameter[step]/2, y+bullet.HEIGHT-diameter[step]/2, diameter[step], diameter[step]);
		g.setColor(c);
		}
		step++;
	}
	
}
