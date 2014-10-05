import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
public class Help extends JFrame implements ActionListener {

private static final long serialVersionUID = 1L;
JButton start;
 JButton back;
 public Help(){
  super("Tank");
  this.setSize(932,474);
ImageIcon bg= new ImageIcon("help.JPG");
JLabel label = new JLabel(bg);
label.setBounds(0,0,bg.getIconWidth(),bg.getIconHeight());
this.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
JPanel jp=(JPanel)this.getContentPane();
jp.setOpaque(false);
JPanel panel=new JPanel();
panel.setOpaque(false);

back=new JButton("返回");
back.setFont(new java.awt.Font("微软雅黑", 1, 15)); 
back.addActionListener(this);
back.addMouseListener(new Monitor());
back.setSize(70,40);
back.setLocation(430,240);
back.setBorder(null);
back.setContentAreaFilled(false);
back.setFocusPainted(false);


start=new JButton("开始游戏");
start.setFont(new java.awt.Font("微软雅黑", 1, 15)); 
start.addActionListener(this);
start.addMouseListener(new Monitor());
start.setSize(70,40);
start.setLocation(430,300);
start.setBorder(null);
start.setContentAreaFilled(false);
start.setFocusPainted(false);

panel.setLayout(null);
panel.add(start);
panel.add(back);
this.add(panel);
this.setResizable(false);
  

this.setVisible(true);
  
 }
 public void actionPerformed(ActionEvent e) {
  if(e.getSource()==start){//button
  this.setVisible(false);
  try {
	new TankClient().launchFrame();
} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
  }
  if(e.getSource()==back){//button
	  this.setVisible(false);
	  new Face();
	  }
 }
 
class Monitor extends MouseAdapter{
	 
public void mouseEntered(MouseEvent e){
	JButton btn = (JButton) e.getSource();
	btn.setContentAreaFilled(true);
	btn.setBackground(Color.BLUE);
	System.out.println("进入");
}

public void mouseExited(MouseEvent e){
	JButton btn = (JButton) e.getSource();
	btn.setContentAreaFilled(false);
	btn.setBorder(null);
	System.out.println("离开");
}
 }
   
}