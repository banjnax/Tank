import java.awt.*;
import java.awt.event.*;;

public class MyTank extends TankClass{
	
	public int SNum;

	public boolean TankHitBigEnemy(TankClass tc){
		if(tc.getRect().intersects(this.t.BE.getRect())){
			tc.setLive(false);
			t.booms.add(new Boom(tc.x,tc.y,t));
			return true;
		}
		return false;
	}
	public MyTank(int x, int y,int speed,boolean good) {
		super(x,y,speed,good);
	}
	public MyTank(int x,int y,int speed,boolean good,TankServer t){
		super(x,y,speed,good,t);
	}
	
	public void paint(Graphics g){
		if(!live){
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.white);
		BloodBar bb=new BloodBar(x,y,WIDTH,HEIGHT,this);
		g.drawString("Player"+SNum,x-10,y-10);
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
	public void landMine(){
		if(this.live){	
		fire(Direction.STOP);
		t.lmNum--;
		}
	}
  	public void tankControl(int key){
		//int key=e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT:
			l=true;
			break;
		case KeyEvent.VK_UP:
			u=true;
			break;
		case KeyEvent.VK_RIGHT:
			r=true;
			break;
		case KeyEvent.VK_DOWN:
			d=true;
			break;
		}
		locat();
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

	public void keyReleased(int key) {	
		//int key=e.getKeyCode();
		switch(key){
		case KeyEvent.VK_F2:
			if(!live&&t.rLife!=0){
				live=true;
				life=100;
				t.rLife--;
			}
			break;
		case KeyEvent.VK_S:
			if(t.sfNum!=0){
			superFire();
			}
			break;
		case KeyEvent.VK_A:
			if(t.lmNum!=0){
				landMine();
			}
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			l=false;
			break;
		case KeyEvent.VK_UP:
			u=false;
			break;
		case KeyEvent.VK_RIGHT:
			r=false;
			break;
		case KeyEvent.VK_DOWN:
			d=false;
			break;
		}
		locat();
	}
}
