import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Random;


public class Boss2 extends BaseBoss{
	int stepl=new Random().nextInt(12)+3;
	int speed=5;

	public boolean isGood() {
		return good;
	}
	public Boss2(int x, int y, TankServer t) {
		super(x,y,t);
	}
	void drawBig(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.orange);
		
		g.fillOval(x, y, 20, 20);
		if(flag==0){
		g.drawLine(x-5, y+10, x+25, y+10);
		g.drawLine(x+10, y-5, x+10, y+25);
		}
		if(flag==1){
			g.setColor(Color.red);
			g.drawOval(x-5, y-5, 30, 30);
		}
		g.setColor(Color.green);
		g.drawRect(x-20, y-10, 60, 5);
		g.setColor(Color.red);
		g.fillRect(x+1-20, y-9, 58*life/200, 4);
		g.setColor(c);
		this.move();
	}
	
	@Override
	public void HitSpecialObstacle(SpecialObstacle so){
		//if(t.spFlag==0){
			Iterator<Wall> it=so.ws.iterator();
			while(it.hasNext()){
				Wall w=it.next();
				Iterator<Rectangle> it2=w.partsWall.iterator();
				while(it2.hasNext()){
					Rectangle tr=it2.next();
					if(tr.intersects(getRect())){
						x=oldX;
						y=oldY;
						return;
					}
				}
			}
		//}
	}
	@Override
	public Rectangle getRect(){
		return new Rectangle(this.x,this.y,50,60);
	}
	
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	protected boolean l=false,u=false,r=false,d=false,stop=false;
	Direction dir=Direction.STOP;
	Direction ptDir=Direction.R;
	
	public void move(){
		
		oldX=x;
		oldY=y;
		
		if(x-bullet.WIDTH<0)x=bullet.WIDTH;
		if(x+bullet.WIDTH>TankServer.GAME_WIDTH) x=TankServer.GAME_WIDTH-bullet.WIDTH;
		if(y<30) y=30;
		if(y+bullet.HEIGHT>TankServer.GAME_HEIGHT) y=TankServer.GAME_HEIGHT-bullet.HEIGHT;
		
		switch(dir){
		case L:
			x-=speed;
			break;
		case LU:
			x-=speed;
			y-=speed;
			break;
		case U:
			y-=speed;
			break;
		case RU:
			x+=speed;
			y-=speed;
			break;
		case R:
			x+=speed;
			break;
		case RD:
			x+=speed;
			y+=speed;
			break;
		case D:
			y+=speed;
			break;
		case LD:
			x-=speed;
			y+=speed;
			break;
		case STOP:
			break;
		}
		if(x<0) x=0;
		if((x+50)>TankServer.GAME_WIDTH) x=TankServer.GAME_WIDTH-50;
		if(y<0) y=0;
		if((y+50)>TankServer.GAME_HEIGHT) y=TankServer.GAME_HEIGHT-50;
		if(!good){
			if(stepl==0){
			stepl=new Random().nextInt(20)+3;
			Direction[] dirs=Direction.values();
			int rn=new Random().nextInt(dirs.length);
			dir=dirs[rn];
			}
			stepl--;

			if(life>100){
				if(new Random().nextInt(50)>45) Bigfire();
				}
			else{
				if(new Random().nextInt(51)>48){
					Bigfire2();
					}
			}
		}
	}
	public void Bigfire(){
		if(new Random().nextInt(50)>40){
			t.bullets.add(new bullet(x+8,y+23,TankClass.Direction.D,good,this.t));
			t.bullets.add(new bullet(x-5,y+10,TankClass.Direction.L,good,this.t));
			t.bullets.add(new bullet(x+23,y+10,TankClass.Direction.R,good,this.t));
			t.bullets.add(new bullet(x+8,y-3,TankClass.Direction.U,good,this.t));
		}
		
	}
	public void Bigfire2(){
		if(new Random().nextInt(50)>40){
			t.bullets.add(new bullet(x+8,y+28,TankClass.Direction.D,good,this.t));
			t.bullets.add(new bullet(x-9,y+10,TankClass.Direction.L,good,this.t));
			t.bullets.add(new bullet(x+27,y+10,TankClass.Direction.R,good,this.t));
			t.bullets.add(new bullet(x+8,y-7,TankClass.Direction.U,good,this.t));
			
			t.bullets.add(new bullet(x-3,y-3,TankClass.Direction.LU,good,this.t));
			t.bullets.add(new bullet(x+23,y+23,TankClass.Direction.RD,good,this.t));
			t.bullets.add(new bullet(x-3,y+23,TankClass.Direction.LD,good,this.t));
			t.bullets.add(new bullet(x+23,y-3,TankClass.Direction.RU,good,this.t));
		}
	}
}

