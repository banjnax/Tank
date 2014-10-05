import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
public class Face extends JFrame implements ActionListener {
test b2
	private static final long serialVersionUID = 1L;
JButton button;
 JButton help;
 public Face(){
  super("Tank");
  this.setSize(932,474);
ImageIcon bg= new ImageIcon("bg1.JPG");
JLabel label = new JLabel(bg);
label.setBounds(0,0,bg.getIconWidth(),bg.getIconHeight());
this.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
JPanel jp=(JPanel)this.getContentPane();
jp.setOpaque(false);
JPanel panel=new JPanel();
panel.setOpaque(false);
setLocation(300, 100);


button=new JButton("¿ªÊ¼ÓÎÏ·");
button.setFont(new java.awt.Font("Î¢ÈíÑÅºÚ", 1, 15)); 
button.addActionListener(this);
button.addMouseListener(new Monitor());
button.setSize(70,40);
button.setLocation(430,120);
button.setBorder(null);
button.setContentAreaFilled(false);
button.setFocusPainted(false);

help=new JButton("°ïÖú");
help.setFont(new java.awt.Font("Î¢ÈíÑÅºÚ", 1, 15)); 
help.addActionListener(this);
help.addMouseListener(new Monitor());
help.setSize(70,40);
help.setLocation(430,180);
help.setBorder(null);
help.setContentAreaFilled(false);
help.setFocusPainted(false);


panel.setLayout(null);
panel.add(button);
panel.add(help);
this.add(panel);
this.setResizable(false);
  

this.setVisible(true);
  
 }
 public void actionPerformed(ActionEvent e) {
  if(e.getSource()==button){//button
  this.setVisible(false);
  try {
	new TankClient().launchFrame();
} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
  }
  if(e.getSource()==help){//button
	  this.setVisible(false);
	  new Help();
	  }
 }
 
class Monitor extends MouseAdapter{
	 
public void mouseEntered(MouseEvent e){
	JButton btn = (JButton) e.getSource();
	btn.setContentAreaFilled(true);
	btn.setBackground(Color.white);
	System.out.println("½øÈë");
}

public void mouseExited(MouseEvent e){
	JButton btn = (JButton) e.getSource();
	btn.setContentAreaFilled(false);
	btn.setBorder(null);
	System.out.println("Àë¿ª");
}
 }
   
}