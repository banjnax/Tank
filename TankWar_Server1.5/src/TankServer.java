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
 Tank�����߳�
 */
public class TankServer extends Frame{

	private static final long serialVersionUID = 1L;

	private Properties pro;  //�����ļ�
	
	public static final int GAME_WIDTH=800;//��Ϸ���ڵĿ��
	public static final int GAME_HEIGHT=600;
	public static final int SPEED=5;//̹���ƶ����ٶ�
	int x=50,y=50;
	Image vImage=null;//˫����������˸������ͼƬ
	static int LEVEL=0;
	
	List<TankClass> enemyTanks=new ArrayList<TankClass>();//�з�̹�ˣ�����
	Obstacles os=new Obstacles();//ÿ�ص��ϰ���
	BaseBoss BE;
	List<Boom> booms=new ArrayList<Boom>();//��ը����
	List<bullet> bullets=new ArrayList<bullet>();//�ӵ�����
	SpecialObstacle so=new SpecialObstacle();
	public int sfNum;//���и���
	public int lmNum;//���׸���
	public int enemyNum;//���˸���
	public int rLife;//�������
	Thread thread=null;
	Font f=new Font("����",Font.BOLD ,40);
	String gameState="��ս��!";
	public boolean gameover=false;
	Robot robot =null;
	BufferedImage bi=null;
	ArrayList<Process> ps=new ArrayList<Process>();//�߳�����
	ArrayList<MyTank> mytanks=new ArrayList<MyTank>();
    Socket s=null;//�������ܰ�����
    ArrayList<Socket> outputS=new ArrayList<Socket>();//���ڴ���ͼ���sockets
  MyTank2 mytank2=null;
	public int spFlag=0;
	/*
	 * (non-Javadoc)
	 * ��дupdate()��Ϊ��ʵ��˫���壬������˸
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
 * ��Ϸ�����ܵ�ͼ����ʾ������ÿһ�ε�ˢ�£�
 * @see java.awt.Window#paint(java.awt.Graphics)
 */
	public void paint(Graphics g) {
		
		if(BE.isLive()){
			BE.drawBig(g);
			//�����ؿ����������
			if(LEVEL==2){
				BE.RoundHitMyTank(mytanks.get(0));
				BE.RoundHitObstacle(os.get(LEVEL));
			}
		}
		if(LEVEL==1){
			so.paint(g);
			BE.HitSpecialObstacle(so);
			}
		
		os.get(LEVEL).paint(g);//���ϰ���
		
		//��̹����
		Iterator<MyTank> it=mytanks.iterator();
	//	System.out.println("��:"+mytanks.size());
		while(it.hasNext()){
			MyTank t=it.next();
			t.paint(g);
			t.TankHitObstacle(os.get(LEVEL));
			if(LEVEL==1)
				t.HitSpecialObstacle(so);
			t.TankHitTank(enemyTanks);//�ж��ҷ�̹����з�̹���Ƿ���ײ
			if(this.BE.isLive())//����boss����
			t.TankHitBigEnemy(t);
		}
	
		
		/*
		if(this.rLife==0&&!this.myTank.isLive()){
			this.gameState="���ɵ�������!!";
			gameover=true;
		}
		*/
		
		Color c=g.getColor();
		g.setColor(Color.white);
		g.drawString("ʣ��л���"+enemyTanks.size(), 10, 50);
		g.drawString("ʣ����У�"+this.sfNum, 10, 70);
		g.drawString("ʣ����ף�"+this.lmNum, 10, 90);
		g.drawString("ʣ�ิ�������"+this.rLife, 10, 110);
		Font pf=g.getFont();
		g.setFont(f);
		g.drawString("��Ϸ״̬��"+this.gameState, 200, 580);
		g.setFont(pf);
		
		for(int i=0;i<bullets.size();i++){//���ӵ�
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
			this.gameState="���ɵ�������!!";
			gameover=true;
		}
		/*
		if(this.enemyTanks.size()==0&&!this.BE.isLive()&&this.myTank.isLive()){
			this.gameState="��"+LEVEL+"����Ϸʤ��^_^!!";
			gameover=true;
		}
		*/
		if(this.enemyTanks.size()==0&&!this.BE.isLive()&&mytanks.size()!=0){
			this.gameState="��"+(LEVEL+1)+"��";
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
		
		InputStream fis = TankServer.class.getResourceAsStream("prop.properties");//new FileInputStream("prop.properties");//�����ļ���   
		pro = new Properties();//���Լ��϶���   
		pro.load(fis);//�������ļ���װ�ص�Properties������  
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
			new Thread(new Accept(this)).start();//�����߳�������������ÿ���ܵ�һ���ͻ������飬������һ���߳�
			
		//	mytank2=new MyTank2(50,50,SPEED,true,this);
			    
			this.setProperties();
			this.setLocation(300,100);
			this.setSize(GAME_WIDTH,GAME_HEIGHT);
			this.setTitle("̹�˴�ս");
			this.setResizable(false);
			this.setVisible(true);
			this.setBackground(Color.black);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
						System.out.println("ϵͳ�˳���");
						System.exit(0);
				}
				
			} );
			
			thread=new Thread(new RepaintThread());
			thread.start();
		}
	
	private class RepaintThread implements Runnable{
		public void run() {
			while(LEVEL<3){
				System.out.println("��"+LEVEL);
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
	
//��ͼƬ�߳�
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
						bi=robot.createScreenCapture(new Rectangle(300,100,800,600)); // ����ָ��������(1300,800)ץȡ��Ļ��ָ������
						imi=new ImageIcon(bi);
						//��һ����������
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
	
	
	//������̣߳����ܶ���ͻ�������
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
						t.outputS.add(s);//��һ�����ϵ���������ͼƬ�����
						new Thread(new SendImage(s)).start();
					}
					else{
						new Thread(new Process(s,t)).start();//�ڶ������ϵ�����������ͻ��˰���,����ige�߳̽���ʵʱ��ظ����ͻ��˵����
						i=0;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//���̴߳�����
	class Process implements Runnable{
		public Socket s=null;
		TankServer t;
		InputStream ips=null;//���ܰ���
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


