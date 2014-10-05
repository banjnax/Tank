import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Boss extends BaseBoss {
	public boolean isGood() {
		return good;
	}
	public Boss(int x, int y, TankServer t) {
		super(x,y,t);
	}
	void drawBig(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.orange);
		g.fillRect(x, y,20,40);
		g.fillRect(x+10, y+30, 40, 40);
		g.fillRect(x+40, y,20,40);
		g.drawLine(x+30, y+50, x, y+80);
		g.drawLine(x+30, y+50, x+60, y+80);
		g.drawLine(x+30, y+50, x+30, y+85);
		g.setColor(Color.green);
		g.drawRect(x, y-10, 60, 5);
		g.setColor(Color.red);
		g.fillRect(x+1, y-9, 58*life/200, 4);
		g.setColor(c);
		this.move();
	}
	public Rectangle getRect(){
		return new Rectangle(this.x,this.y,60,70);
	}
	public void move(){
		if(this.x>TankServer.GAME_WIDTH-60) this.mdir=0;
		if(this.x<0) this.mdir=1;
		if(this.mdir==1) x+=BSPEED;
		if(this.mdir==0) x-=BSPEED;
		
	}
	void Bigfire(){
		t.bullets.add(new bullet(x+17,y+55,TankClass.Direction.LD,good,this.t));
		t.bullets.add(new bullet(x+26,y+55,TankClass.Direction.D,good,this.t));
		t.bullets.add(new bullet(x+33,y+55,TankClass.Direction.RD,good,this.t));
		if(new Random().nextInt(30)>20){
			t.bullets.add(new bullet(x+26,y+55,TankClass.Direction.L,good,this.t));
			t.bullets.add(new bullet(x+33,y+55,TankClass.Direction.R,good,this.t));
		}
		
	}
}

