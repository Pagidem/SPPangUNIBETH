package org.sp;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Gj
 */

public class SPPang extends JFrame implements Runnable{
	//Variables que almacenan las imagenes.
	Image imgBola;
	Image imgNave;
	Image imgPantallas[]=new Image[4];
	Image imgPortada;
	
        //Instancia de la clase MPang
	MPang mp=new MPang();
	
        //Se crea una imagen para evitar los parpadeos.
	BufferedImage bi=new BufferedImage(800,600,BufferedImage.TYPE_3BYTE_BGR);
	Graphics gbi=bi.getGraphics();
	
        
        //Metodo que carga las imagenes en las variables.
	public void cargarImagenes(){
		Toolkit tk=Toolkit.getDefaultToolkit();
		imgBola = tk.getImage(getClass().getResource("Bola.png"));
		imgNave = tk.getImage(getClass().getResource("Nave.gif"));
		for (int n=0;n<4;n++)
			imgPantallas[n] = tk.getImage(getClass().getResource("Pantalla0" + (n+1) + ".jpg"));
		imgPortada = tk.getImage(getClass().getResource("Portada.gif"));
	}
	
        
        //Pintar la navecita gif.
	public void pintarNave(Graphics g){
		int x=mp.getX();
		int y[]=mp.getYSuelo();
		float ang[]=mp.getAnguloSuelo();
		BufferedImage biNave=new BufferedImage(200,150, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d=(Graphics2D)biNave.getGraphics();
		g2d.rotate(ang[x+2], 100, 100);
		g2d.drawImage(imgNave, 50, 0, this);
		g.drawImage(biNave,x-100,y[x]+15-100,this);
	}
	
        
        //Pintar las bolas, no el movimiento.
	public void pintarBolas(Graphics g){
		ArrayList<Bola> bolas=mp.getBolas();
		for (Bola b:bolas){
			int tam=b.getTam();
			g.drawImage(imgBola, b.getX()-tam , b.getY()-tam, tam*2, tam*2,this);
		}
	}

        
        //Pinta la linea que servira de disparo
	public void pintarDisparo(Graphics g){
		int dis=mp.getDisDisparo();
		if (dis>0){
			int x=mp.getXDisparo();
			float ang=mp.getAngDisparo();
			int suelo[]=mp.getYSuelo();
			int xI=x;
			int yI=suelo[x];
			int xF=(int)(xI+Math.cos(ang)*dis);
			int yF=(int)(yI+Math.sin(ang)*dis);
			
			for (int n=0;n<5;n++){
				if (n%2==0){
					g.setColor(Color.WHITE);
				}else{
					g.setColor(Color.YELLOW);
				}
				g.drawLine(xI+n-4, yI, xF, yF);
			}
		}
	}
	
	public void pintarSuelo(Graphics g){
//		Color marron=new Color(80,0,0);
//		Color verde=new Color(0,80,0);
		int y[]=mp.getYSuelo();
		for (int n=0;n<800;n++){
			g.setColor(Color.WHITE);
			g.drawLine(n, y[n], n, y[n]+3);
			
		}
	}
	
	public void update(Graphics g){
		paint(g);
	}

	public void paint(Graphics g){
		switch (mp.getEstado()){
		case 0:
			for (int n=0;n<imgPantallas.length;n++){
				gbi.drawImage(imgPantallas[n], 0,0, this);
			}
			gbi.drawImage(imgPortada, 0,0, this);
			break;
		case 1:
		case 2:
			gbi.drawImage(imgPantallas[mp.getFase()%4], 0,0, this);
			pintarSuelo(gbi);
			pintarNave(gbi);
			pintarBolas(gbi);
			pintarDisparo(gbi);
			break;
		}
		g.drawImage(bi,0,0,this);
	}
	
	
	public SPPang(){
		setBounds(0,0,800,600);                
		setTitle("Tutorial de SPPang UNIBETH");
		cargarImagenes();
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				mp.pulsaTecla(e.getKeyCode());
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyReleased(e);
				mp.sueltaTecla(e.getKeyCode());
			}			
		});
		Thread hilo=new Thread (this);
		hilo.start();
	}
	
	public static void main(String s[]){
		new SPPang().setVisible(true);
	}
	
	public void run(){
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
			}
			mp.ejecutarFrame();
			repaint();
		}
	}

}
