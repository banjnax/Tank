import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.*;
import javax.swing.ImageIcon;
/*
 Tank是主线程
 */
public class TankServer extends Frame{

	private static final long serialVersionUID = 1L;

	private Properties pro;  //配置文件
	
	public static final int GAME_WIDTH=800;//游戏窗口的宽度
	public static final int GAME_HEIGHT=600;
	public static final int SPEED=5;//坦克移动的速度
	int x=50,y=50;
	Image vImage=null;//双缓冲消除闪烁的虚拟图片
	static int LEVEL=0;
	
	List<TankClass> enemyTanks=new ArrayList<TankClass>();//敌方坦克，多量
	Obstacles os=new Obstacles();//每关的障碍物
	BaseBoss BE;
	List<Boom> booms=new ArrayList<Boom>();//爆炸容器
	List<bullet> bullets=new ArrayList<bullet>();//子弹容器
	SpecialObstacle so=new SpecialObstacle();
	public int sfNum;//大招个数
	public int lmNum;//地雷个数
	public int enemyNum;//敌人个数
	public int rLife;//复活次数
	Thread thread=null;
	Font f=new Font("宋体",Font.BOLD ,40);
	String gameState="激战中!";
	public boolean gameover=false;
	Robot robot =null;
	BufferedImage bi=null;
	ArrayList<Process> ps=new ArrayList<Process>();//线程连接
	ArrayList<MyTank> mytanks=new ArrayList<MyTank>();
    Socket s=null;//用来接受按键的
    ArrayList<Socket> outputS=new ArrayList<Socket>();//用于传输图像的sockets
  MyTank2 mytank2=null;
	public int spFlag=0;
	/*
	 * (non-Javadoc)
	 * 重写update()：为了实现双缓冲，消除闪烁
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */
	public void update(Graphics g) {
		if(vImage==null){
			vImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics vImagePaint=vImage.getGraphics();
		Color c=vImagePaint.getColor();
		vImagePaint.setColor(Color.black);
		vImagePaint.fillRect(0, 0,GAME_WIDTH,GAME_HEIGHT);
		vImagePaint.setColor(c);
		paint(vImagePaint);
		g.drawImage(vImage, 0, 0, null);
	}
/*
 * (non-Javadoc)
 * 游戏窗口总的图像显示方法（每一次的刷新）
 * @see java.awt.Window#paint(java.awt.Graphics)
 */
	public void paint(Graphics g) {
		
		if(BE.isLive()){
			BE.drawBig(g);
			//第三关开启检测以下
			if(LEVEL==2){
				BE.RoundHitMyTank(mytanks.get(0));
				BE.RoundHitObstacle(os.get(LEVEL));
			}
		}
		if(LEVEL==1){
			so.paint(g);
			BE.HitSpecialObstacle(so);
			}
		
		os.get(LEVEL).paint(g);//画障碍物
		
		//画坦克类
		Iterator<MyTank> it=mytanks.iterator();
	//	System.out.println("有:"+mytanks.size());
		while(it.hasNext()){
			MyTank t=it.next();
			t.paint(g);
			t.TankHitObstacle(os.get(LEVEL));
			if(LEVEL==1)
				t.HitSpecialObstacle(so);
			t.TankHitTank(enemyTanks);//判断我方坦克与敌方坦克是否相撞
			if(this.BE.isLive())//碰到boss就死
			t.TankHitBigEnemy(t);
		}
	
		
		/*
		if(this.rLife==0&&!this.myTank.isLive()){
			this.gameState="被干掉啦：（!!";
			gameover=true;
		}
		*/
		
		Color c=g.getColor();
		g.setColor(Color.white);
		g.drawString("剩余敌机："+enemyTanks.size(), 10, 50);
		g.drawString("剩余大招："+this.sfNum, 10, 70);
		g.drawString("剩余地雷："+this.lmNum, 10, 90);
		g.drawString("剩余复活次数："+this.rLife, 10, 110);
		Font pf=g.getFont();
		g.setFont(f);
		g.drawString("游戏状态："+this.gameState, 200, 580);
		g.setFont(pf);
		
		for(int i=0;i<bullets.size();i++){//画子弹
			bullet b=bullets.get(i);
			if((BE.isLive()&&LEVEL!=1)||spFlag==1){
				b.hit(BE);
			}//else{
				//if(!this.BE.isLive()&&this.enemyTanks.size()!=0){
				//if(this.enemyTanks.size()!=0){
			if(LEVEL!=1||(LEVEL==1&&enemyTanks.size()!=0))
					b.hitTanks(enemyTanks);
				//}
		//	}
			b.hit(mytanks);
			if(LEVEL==1)
				b.hitSpecialObstacle(so);
			b.hitObstacle(os.get(LEVEL));
			b.paint(g);
		}
		for(int i=0;i<enemyTanks.size();i++){
			TankClass enemyTank=enemyTanks.get(i);
			
			enemyTank.TankHitObstacle(os.get(LEVEL));
			if(LEVEL==1)
				enemyTank.HitSpecialObstacle(so);
			enemyTank.TankHitTank(enemyTanks);
			enemyTank.paint(g);
		}
		for(int i=0;i<booms.size();i++){
			Boom b=booms.get(i);
			b.paintBoom(g);
		}
		g.setColor(c);
		if(this.rLife==0&&mytanks.size()==0){
			this.gameState="被干掉啦：（!!";
			gameover=true;
		}
		/*
		if(this.enemyTanks.size()==0&&!this.BE.isLive()&&this.myTank.isLive()){
			this.gameState="第"+LEVEL+"关游戏胜利^_^!!";
			gameover=true;
		}
		*/
		if(this.enemyTanks.size()==0&&!this.BE.isLive()&&mytanks.size()!=0){
			this.gameState="第"+(LEVEL+1)+"关";
			gameover=true;
		}
		if(!BE.isLive()&&LEVEL==3&&mytanks.size()!=0){
			gameState="Win Y^_^Y";
		}
		
	}

	public static void main(String[] args) {
		new Face();
	}
	public void init(int level){
		
		switch(level){
		case 0:init0();
			break;
		case 1:init1();
			break;
		case 2:init2();
			break;
			
		}
	}
	public void init0(){
		BE=new Boss(160,40,this);
		for(int i=0;i<this.enemyNum;i++){
			if(i>10){
			enemyTanks.add(new TankClass(70+40*(i+1-10),500,SPEED,false,this));	
			}
			//else
	//enemyTanks.add(new TankClass(70+40*(i+1),70,SPEED,false,this));
			//enemyTanks.add(new TankClass(new Random().nextInt(GAME_WIDTH),new Random().nextInt(GAME_HEIGHT),SPEED,false,this));
		}
	}
	public void init1(){
		this.BE=new Boss2(720,250,this);
		BE.setLive(true);
		BE.life=200;
		for(int i=0;i<this.enemyNum;i++){
			if(i>10){
			enemyTanks.add(new TankClass(70+40*(i+1-10),500,SPEED,false,this));	
			}
			//enemyTanks.add(new TankClass(new Random().nextInt(GAME_WIDTH),new Random().nextInt(GAME_HEIGHT),SPEED,false,this));
		}
	}
	
	public void init2(){
		//this.BE=new Boss(160,40,this);
		this.BE=new Boss3(200,200,this);
		BE.life=200;
		//this.BE=new Boss2(740,290,this);
		//this.BE.live=true;
		for(int i=0;i<this.enemyNum;i++){
			if(i>10){
			enemyTanks.add(new TankClass(70+40*(i+1-10),500,SPEED,false,this));	
			}
			//else
	//enemyTanks.add(new TankClass(70+40*(i+1),70,SPEED,false,this));
			//enemyTanks.add(new TankClass(new Random().nextInt(GAME_WIDTH),new Random().nextInt(GAME_HEIGHT),SPEED,false,this));
		}
	}
	
	public void setProperties() throws IOException{
		
		InputStream fis = TankServer.class.getResourceAsStream("prop.properties");//new FileInputStream("prop.properties");//属性文件流   
		pro = new Properties();//属性集合对象   
		pro.load(fis);//将属性文件流装载到Properties对象中  
		fis.close();
		this.sfNum=Integer.parseInt(pro.getProperty("sfNum"));
		this.lmNum=Integer.parseInt(pro.getProperty("lmNum"));
		this.enemyNum=Integer.parseInt(pro.getProperty("enemyNum"));
		this.rLife=Integer.parseInt(pro.getProperty("rLife"));
	}
	
	void launchFrame() throws IOException{
		
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
			new Thread(new Accept(this)).start();//接受线程启动，在里面每接受到一个客户端求情，就另起一个线程
			
		//	mytank2=new MyTank2(50,50,SPEED,true,this);
			    
			this.setProperties();
			this.setLocation(300,100);
			this.setSize(GAME_WIDTH,GAME_HEIGHT);
			this.setTitle("坦克大战");
			this.setResizable(false);
			this.setVisible(true);
			this.setBackground(Color.black);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
						System.out.println("系统退出！");
						System.exit(0);
				}
				
			} );
			
			thread=new Thread(new RepaintThread());
			thread.start();
		}
	
	private class RepaintThread implements Runnable{
		public void run() {
			while(LEVEL<3){
				System.out.println("第"+LEVEL);
				gameover=false;
				
				init(LEVEL);
				
				while(!gameover){
					repaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				LEVEL++;
			}
		}
		
	}
	
//发图片线程
	class SendImage implements Runnable{
		Socket s=null;
		ObjectOutputStream oos=null;
		public SendImage(Socket s){
			this.s=s;
		}
		BufferedImage bi=null;
		ImageIcon imi=null;
		public void run(){
			while(s.isConnected()){
				try {
						bi=robot.createScreenCapture(new Rectangle(300,100,800,600)); // 根据指定的区域(1300,800)抓取屏幕的指定区域
						imi=new ImageIcon(bi);
						//按一个对象来发
						oos=new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(imi);
						oos.flush();
				} catch (IOException e) {
					if(s!=null)	s=null;
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//处理多线程，接受多个客户端连接
	class Accept implements Runnable{
		private ServerSocket ss=null;
		Socket s=null;
		TankServer t;
		public Accept(TankServer t){
			this.t=t;
		}
		public void run(){
			try {
				ss=new ServerSocket(6000);
				int i=0;
				while(true){
					s=ss.accept();
					i++;
					if(i==1){
						t.outputS.add(s);//第一个连上的是用来传图片对象的
						new Thread(new SendImage(s)).start();
					}
					else{
						new Thread(new Process(s,t)).start();//第二个连上的是用来处理客户端按键,另起ige线程进行实时监控各个客户端的情况
						i=0;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//子线程处理函数
	class Process implements Runnable{
		public Socket s=null;
		TankServer t;
		InputStream ips=null;//接受按键
		MyTank mt=null;
		int preKey=0;
		public Process(Socket s,TankServer t){
			this.s=s;
			this.t=t;
			int i=t.mytanks.size();
			mt=new MyTank(50*i,50*i,SPEED,true,t);
			
		}
		public void run(){
			t.ps.add(this);
			mt.SNum=t.ps.size();
			t.mytanks.add(mt);
			
			try {
				while(s.isConnected()){
						ips=s.getInputStream();
						int x=ips.read();
						mt.keyReleased(preKey);
						mt.tankControl(x);
						preKey=x;
				}
				
				ips.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}


