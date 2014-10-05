import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.audio.*;

import java.io.*;


public class bullet {
	int x,y;
	public final int bspeed=7;
	int xspeed=5;
	TankClass.Direction dir;
	public static final int WIDTH=8;
	public static final int HEIGHT=8;
	private boolean live=true;
	private TankServer t;
	private boolean good;
	private boolean landM=false;
	AudioStream as;

	public bullet(int x, int y, TankClass.Direction dir,boolean good) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.good=good;
		try {
			FileInputStream fileau=new  FileInputStream("sound.au");
			 as=new AudioStream(fileau);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public bullet(int x, int y, TankClass.Direction dir,boolean good,TankServer t){
		this(x,y,dir,good);
		this.t=t;
		try {
			FileInputStream fileau=new  FileInputStream("sound.au");
			 as=new AudioStream(fileau);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean isLandM() {
		return landM;
	}

	public boolean isGood() {
		return good;
	}

	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean isLive() {
		return live;
	}
	
	boolean hit(TankClass t){
		if(this.getRect().intersects(t.getRect())&&t.isLive()&&this.good!=t.isGood()){
			
			AudioPlayer.player.start(as);
			
			if(t.life!=0){
				t.life-=20;
				if(t.life<=0){
					t.setLive(false);
				}
			}
			this.live=false;
			this.t.booms.add(new Boom(x,y,this.t));
			return true;
		}
		return false;
	}
	
	void hit(ArrayList<MyTank> tanks){
		Iterator<MyTank> it=tanks.iterator();
		while(it.hasNext()){
			MyTank t=it.next();
			if(this.getRect().intersects(t.getRect())&&t.isLive()&&this.good!=t.isGood()){
				
				AudioPlayer.player.start(as);
				
				if(t.life!=0){
					t.life-=20;
					if(t.life<=0){
						t.setLive(false);
					}
				}
				this.live=false;
				this.t.booms.add(new Boom(x,y,this.t));
			
			}
	
		}
	}
	
	
	
	boolean hit(BaseBoss t){
		
		if(this.getRect().intersects(t.getRect())&&t.isLive()&&this.good!=t.isGood()){
		//if(this.getRect().intersects(t.getRect())&&t.isLive()){
			
			AudioPlayer.player.start(as);
			
			if(t.life!=0){
				t.life-=20;
				if(t.life==0){
					t.setLive(false);
				}
				if(t.life<101)
					t.flag=1;
			}
			this.live=false;
			
			this.t.booms.add(new Boom(x,y,this.t));
			return true;
		}
		return false;
	}
	boolean hitTanks(List<TankClass> enemyTanks){
		for(int i=0;i<enemyTanks.size();i++){
			if(hit(enemyTanks.get(i))&&!enemyTanks.get(i).isLive()){
				enemyTanks.remove(i);
				if(this.t.enemyTanks.size()==0){//boss此处被
					this.t.BE.live=true;
				}
				if(this.t.enemyTanks.size()==0&&t.LEVEL==1){//boss此处被
					t.spFlag=1;
				}
				return true;
			}
		}
		return false;
	}
	boolean hitSpecialObstacle(SpecialObstacle o){
		Iterator<Wall> itr=o.ws.iterator();
		while(itr.hasNext()){
			Wall w=itr.next();
			Iterator<Rectangle> it=w.partsWall.iterator();
			
			//ArrayList<Rectangle> wfd=new ArrayList<Rectangle>();
			while(it.hasNext()){
				Rectangle r=it.next();
				if(this.getRect().intersects(r)){
					
					AudioPlayer.player.start(as);
					//wfd.add(r);
					if(t.spFlag==1){
						w.partsWall.remove(r);
					}
					this.live=false;
					return true;
				}
			}
			
			
		}
		return false;
	}

	
	boolean hitObstacle(Obstacle o){
		Iterator<Wall> itr=o.ws.iterator();
		while(itr.hasNext()){
			Wall w=itr.next();
			Iterator<Rectangle> it=w.partsWall.iterator();
			
			//ArrayList<Rectangle> wfd=new ArrayList<Rectangle>();
			while(it.hasNext()){
				Rectangle r=it.next();
				if(this.getRect().intersects(r)){
					
					AudioPlayer.player.start(as);
					//wfd.add(r);
					w.partsWall.remove(r);
					this.live=false;
					return true;
				}
			}
			
			
		}
		return false;
	}

	void paint(Graphics g){
		if(!this.live){
			t.bullets.remove(this);
			return;
		}
		Color c=g.getColor();
		if(!this.good){
			g.setColor(Color.orange);
			g.fillOval(x, y, WIDTH, HEIGHT);
		}
		else{
		g.setColor(Color.white);
		g.fillOval(x, y, WIDTH, HEIGHT);
		}
		g.setColor(c);	
		bMove();
	}
	void bMove(){
		switch(dir){
		case L:
			x-=bspeed;
			break;
		case LU:
			x-=xspeed;
			y-=xspeed;
			break;
		case U:
			y-=bspeed;
			break;
		case RU:
			x+=xspeed;
			y-=xspeed;
			break;
		case R:
			x+=bspeed;
			break;
		case RD:
			x+=xspeed;
			y+=xspeed;
			break;
		case D:
			y+=bspeed;
			break;
		case LD:
			x-=xspeed;
			y+=xspeed;
			break;
		}
		
		if(x<0||x>TankServer.GAME_WIDTH||y<0||y>TankServer.GAME_HEIGHT) {
			live=false;
		}
	}
}
