import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Boss3 extends BaseBoss {

	double sita=2;
	AudioStream as;
	int flag=0;
	int ch=0;
	int oldX,oldY;
	int stepl=new Random().nextInt(12)+3;
	int speed=5;
	Rectangle rbs;
	public boolean isGood() {
		return good;
	}
	public Boss3(int x, int y, TankServer t) {
		super(x,y,t);
		rbs=new Rectangle(x-50,y-50,8,8);
		oldX=x-50;
		oldY=y-50;
		try {
			FileInputStream fileau=new  FileInputStream("sound.au");
			 as=new AudioStream(fileau);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void drawBig(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.orange);
		g.fillRect(x, y, 50, 50);
		
		if(flag==0){
			g.fillRect(x+24, y+50, 2, 10);
			ch++;
			if(life==50){
				flag=2;
			}
			if(ch==50){
			flag=1;
			ch=0;
			}
		}
		else if(flag==1){
			g.fillRect(x+9, y+50, 2, 10);
			g.fillRect(x+38, y+50, 2, 10);
			ch++;
			if(ch==25){
			flag=0;
			}
		}
		else if(flag==2){
			g.fillRect(x+24, y+50, 2, 10);
			g.fillRect(x+9, y+50, 2, 10);
			g.fillRect(x+38, y+50, 2, 10);
		}else{
			
		}
		
		g.fillOval(rbs.x, rbs.y, 8, 8);
	//	g.drawString("1.1",rbs.x-5, rbs.y-5);

		g.setColor(Color.green);
		g.drawRect(x, y-10, 50, 5);
		g.setColor(Color.red);
		g.fillRect(x+1, y-9, 48*life/200, 4);
		g.setColor(c);
		sita+=5;
		this.move();
	}
	public Rectangle getRect(){
		return new Rectangle(this.x,this.y,30,30);
	}
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	protected boolean l=false,u=false,r=false,d=false,stop=false;
	Direction dir=Direction.STOP;
	Direction ptDir=Direction.R;
	
	public void move(){
		oldX=x;
		oldY=y;
		if(new Random().nextInt(100)<80){
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
				if(new Random().nextInt(50)>40) Bigfire();
			}
			
		}
		else{
			int temx=t.mytanks.get(0).x-x;
			int temy=t.mytanks.get(0).y-y;
			if(temx>=0) x+=speed;
			else x-=speed;
			if(temy>=0) y+=speed;
			else y-=speed;
		}
		int tx,ty;
		//if(x<0) rbs.x=x+100;
		//if(y<0) rbs.y=y+100;

		tx=(int)(106*Math.cos(sita));
		ty=(int)(106*Math.sin(sita));
		
		rbs.x=tx+25+x;
		rbs.y=ty+25+x;
		
		if(sita>360) sita=sita%360;
		
		
	}
	public Rectangle getRound(){
		return rbs;
	}
	@Override
	public void RoundHitMyTank(MyTank mt){
		if(intersectRoundBullets(mt.getRect())){
			mt.life-=20;
		}
	}
	@Override
	public void RoundHitObstacle(Obstacle o){
		Iterator<Wall> itr=o.ws.iterator();
		while(itr.hasNext()){
			Wall w=itr.next();
			Iterator<Rectangle> it=w.partsWall.iterator();
			ArrayList<Rectangle> wfd=new ArrayList<Rectangle>();//因为不能在遍历的时候删除元素，因此先用此List暂存待删除元素
			while(it.hasNext()){
				Rectangle r=it.next();
				if(intersectRoundBullets(r)){
					AudioPlayer.player.start(as);
					wfd.add(r);
				}
			}
			
			w.partsWall.removeAll(wfd);
		}
	
	}
	@Override
	public boolean intersectRoundBullets(Rectangle r) {
		
				if(rbs.intersects(r)) return true;
			
			return false;
	
	}
	void Bigfire(){
		if(flag==0){
			t.bullets.add(new bullet(x+21,y+50,TankClass.Direction.D,good,this.t));
		}
		else if(flag==1){
			t.bullets.add(new bullet(x+6,y+50,TankClass.Direction.D,good,this.t));
			t.bullets.add(new bullet(x+35,y+50,TankClass.Direction.D,good,this.t));
		}
		else{
			t.bullets.add(new bullet(x+21,y+50,TankClass.Direction.D,good,this.t));
			t.bullets.add(new bullet(x+6,y+50,TankClass.Direction.D,good,this.t));
			t.bullets.add(new bullet(x+35,y+50,TankClass.Direction.D,good,this.t));
		}
	}
	
}

