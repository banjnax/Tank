import java.awt.*;
import java.util.*;
import java.util.List;;

public class TankClass {
	int x,y;
	int speed;
	public static final int WIDTH=30;
	public static final int HEIGHT=30;
	TankServer t=null;
	protected boolean good=true;
	protected boolean live=true;
	protected static Random Rand=new Random();
	protected int stepl=Rand.nextInt(12)+3;
	protected int oldX;
	protected int oldY;
	int life=100;
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isGood() {
		return good;
	}

	public void stay(){
		x=oldX;
		y=oldY;
	}
	public boolean TankHitObstacle(Obstacle o){
		Iterator<Wall> itr=o.ws.iterator();
		while(itr.hasNext()){
			Wall w=itr.next();
			Iterator<Rectangle> it=w.partsWall.iterator();
			while(it.hasNext()){
				Rectangle r=it.next();
				if(this.live&&this.getRect().intersects(r)){
					this.stay();
					return true;
				}
			}
		}
		return false;
	}
	
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
	
	public boolean TankHitBigEnemy(TankClass tc){
		if(tc.getRect().intersects(this.t.BE.getRect())){
			tc.setLive(false);
			t.booms.add(new Boom(tc.x,tc.y,t));
			return true;
		}
		return false;
	}
	public boolean TankHitTank(List<TankClass> tanks){
		for(int i=0;i<tanks.size();i++){
			TankClass t=tanks.get(i);
			if(this!=t){
				if(this.live&&t.isLive()&&this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	protected boolean l=false,u=false,r=false,d=false,stop=false;
	Direction dir=Direction.STOP;
	Direction ptDir=Direction.R;
	public TankClass(int x, int y,int speed,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX=x;
		this.oldY=y;
		this.speed=speed;
		this.good=good;
	}
	public TankClass(int x,int y,int speed,boolean good,TankServer t){
		this(x,y,speed,good);
		this.t=t;
	}
	public void paint(Graphics g){
		if(!this.live){
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.white);
		BloodBar bb=new BloodBar(x,y,WIDTH,HEIGHT,this);
		bb.drawBB(g);
		if(good){
				g.fillOval(x, y, WIDTH, HEIGHT);
			}
		else {
			g.fillRect(x, y, WIDTH, HEIGHT);
		}
		switch(ptDir){
		case L:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x-bullet.WIDTH/2, y+HEIGHT/2);
			break;
		case LU:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH/2, y-bullet.HEIGHT/2);
			break;
		case RU:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y);
			break;
		case R:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH+bullet.WIDTH/2, y+HEIGHT/2);
			break;
		case RD:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y+HEIGHT);
			break;
		case D:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH/2, y+HEIGHT+bullet.HEIGHT/2);
			break;
		case LD:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y+HEIGHT);
			break;
		}
		if(dir!=Direction.STOP)ptDir=dir;
		g.setColor(c);	
		move();
	}
	
	public void move(){
		if(x-bullet.WIDTH<0)x=bullet.WIDTH;
		if(x+WIDTH+bullet.WIDTH>TankServer.GAME_WIDTH) x=TankServer.GAME_WIDTH-WIDTH-bullet.WIDTH;
		if(y<30) y=30;
		if(y+HEIGHT+bullet.HEIGHT>TankServer.GAME_HEIGHT) y=TankServer.GAME_HEIGHT-HEIGHT-bullet.HEIGHT;
		
		this.oldX=x;
		this.oldY=y;
		
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
		if(!good){
			if(stepl==0){
			stepl=Rand.nextInt(20)+3;
			Direction[] dirs=Direction.values();
			int rn=Rand.nextInt(dirs.length);
			dir=dirs[rn];
			}
			stepl--;
			if(Rand.nextInt(50)>48) fire();
		}
		if(this.t.BE.isLive()){
		if(Rand.nextInt(50)>40) this.t.BE.Bigfire();
		}
		
	}

	public class BloodBar{
		
		int x,y,w,h;
		TankClass t;
		
		public BloodBar(int x, int y, int w, int h, TankClass t) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.t = t;
		}
		
		void drawBB(Graphics g){
			Color c=g.getColor();
			g.setColor(Color.green);
			g.drawRect(x, y-10, w, 5);
			g.setColor(Color.red);
			g.fillRect(x+1, y-9, (w-2)*t.life/100, 4);
			g.setColor(c);
					
		}
	}
	public void fire(){
		if(!live) return;
		int bx=this.x+TankClass.WIDTH/2-bullet.WIDTH/2;
		int by=this.y+TankClass.HEIGHT/2-bullet.HEIGHT/2;
		t.bullets.add(new bullet(bx,by,ptDir,good,this.t));
	}
	public void fire(Direction d){
		if(!live) return;
		int bx=this.x+TankClass.WIDTH/2-bullet.WIDTH/2;
		int by=this.y+TankClass.HEIGHT/2-bullet.HEIGHT/2;
		t.bullets.add(new bullet(bx,by,d,good,this.t));
	}
	public void superFire(){
		if(this.isLive()){
			Direction[] dirs=Direction.values();
			for(int i=0;i<dirs.length-1;i++){
				for(int j=0;j<5;j++){
				fire(dirs[i]);
				}
			}
			t.sfNum--;
		}
	}
 
	public void locat(){
		if(l&&!u&&!r&&!d) dir=Direction.L;
		else if(l&&u&&!r&&!d) dir=Direction.LU;
		else if(!l&&u&&!r&&!d) dir=Direction.U;
		else if(!l&&u&&r&&!d) dir=Direction.RU;
		else if(!l&&!u&&r&&!d) dir=Direction.R;
		else if(!l&&!u&&r&&d) dir=Direction.RD;
		else if(!l&&!u&&!r&&d) dir=Direction.D;
		else if(l&&!u&&!r&&d) dir=Direction.LD;
		else dir=Direction.STOP;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public boolean isStop() {
		return stop;
	}
}
