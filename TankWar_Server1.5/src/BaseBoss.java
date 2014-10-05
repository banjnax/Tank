import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;

public abstract class BaseBoss {
	int x,y;//初始位置
	int life=200;//生命值
	TankServer t;
	public boolean live=false;
	public boolean good=false;
	public int mdir=1;
	public int BSPEED=6;
	public int flag=0;
	int oldX,oldY;
	public abstract void move();
	abstract void Bigfire();
	abstract void drawBig(Graphics g);
	public abstract Rectangle getRect();
	public abstract boolean isGood();
	public boolean intersectRoundBullets(Rectangle r){
		return true;
	}
	public void RoundHitMyTank(MyTank mt){}
	public void RoundHitObstacle(Obstacle o){}
	public void HitSpecialObstacle(SpecialObstacle so){
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
	public BaseBoss(int x, int y, TankServer t) {
		this.x = x;
		this.y = y;
		this.t = t;
	}
	 
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
}

