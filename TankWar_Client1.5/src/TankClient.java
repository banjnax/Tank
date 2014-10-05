import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
/*
 Tank是主线程
 */
public class TankClient extends JFrame{
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH=800;//游戏窗口的宽度
	public static final int GAME_HEIGHT=600;
	public static final int SPEED=5;//坦克移动的速度
	Socket inputS=null;
	Socket outputS=null;
	int x=50,y=50;
	ImageIcon bg=null;
	Thread thread=null;
	
	OutputStream os=null;//发送按键
	ObjectInputStream ois=null;//接受对象

	public void paint(Graphics g) {
		//接受照片流
			try {
				ois=new ObjectInputStream(inputS.getInputStream());
				bg=(ImageIcon)ois.readObject();
				g.drawImage(bg.getImage(),0,0,GAME_WIDTH,GAME_HEIGHT,null);
				System.out.println("绘图结束");
				
			} catch (Exception e) {
				try {
					ois.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		new Face();
	}

	void launchFrame() throws IOException{
		inputS=new Socket("localhost",6000);
	//	inputS=new Socket("172.31.24.92",6000);
			
			this.addKeyListener(new TankDirection());
			this.setLocation(300,100);
			this.setSize(GAME_WIDTH,GAME_HEIGHT);
			this.setTitle("坦克大战");
			this.setResizable(false);
		outputS=new Socket("localhost",6000);
	//		outputS=new Socket("172.31.24.92",6000);
			this.setVisible(true);
			this.setBackground(Color.black);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
						System.out.println("系统退出！");
						try {
							os.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						System.exit(0);
				}
				
			} );
			thread=new Thread(new RepaintThread());
			thread.start();
		}
	
	
	private class RepaintThread implements Runnable{
		public void run() {
				while(true){
					repaint();
				}
		}
		
	}
	
	
	private class TankDirection extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
				try {
					os=outputS.getOutputStream();
					os.write(e.getKeyCode());
					os.flush();
					System.out.println("传输完成："+e.getKeyCode());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
		}
		
	}
	
}
