package org.sikuli.bot;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import org.sikuli.api.DefaultScreenLocation;
import org.sikuli.api.Screen;
import org.sikuli.api.ScreenLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotRemote extends JFrame {
	private AndroidMouse mouse;
	private AndroidScreen screen;	
	private Makerbot bot;
	
	private Point dragOrigin;
	
	boolean dragging = false;
	
	Logger log = LoggerFactory.getLogger(BotRemote.class);

	public BotRemote() {
		setTitle("Sikuli Bot - Remote");
		setSize(1280, 800+60);
		label = new JLabel();
		Container contentPane = getContentPane();
		
		
		
		String name = "screen.png";
		label.setIcon(new ImageIcon(name));
		
		JLabel buttons = new JLabel(new ImageIcon(getClass().getResource("/buttons.png")));
		contentPane.add(label, BorderLayout.CENTER);
		contentPane.add(buttons, BorderLayout.PAGE_END);


		bot = new Makerbot();		
		try{
			bot.connect();
		}catch(IllegalStateException e){
			log.error("bot.connect:" +e);
			log.error("Makerbot is not connected.");			
			bot = null;
		}
		
		mouse = new AndroidMouse(bot);
		screen = new AndroidScreen();
		
		
		
		addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				log.trace("mouseDragged: " + e);
				if (!dragging){
					dragOrigin = e.getPoint();
				}
				dragging = true;
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				log.trace("mouseMoved: " + e);				
			}
		});

		addMouseListener(new MouseAdapter(){			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				log.info("mouseListener: clicked (" + e.getX() + "," + e.getY() + ")");
				
				if (e.getY() > 820){					
					int x = 1280/2;
					if (e.getX() > 1280/2 + 100){
						// back
						x = x + 150;
					}else if (e.getX() < 1280/2 - 100){
						// tasks
						x = x - 150;
					}
					int y = 800 + 80;
					ScreenLocation loc = new DefaultScreenLocation(screen, x, y);
					mouse.click(loc);
				}else{
					ScreenLocation loc = new DefaultScreenLocation(screen, e.getX(), e.getY());
					mouse.click(loc);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				log.trace("mouseReleased: " + e);
				dragging = false;
				if (dragOrigin != null){		
					Point from = dragOrigin;
					Point to = e.getPoint();
					log.info("mouseListener: dragged from " + from + " to " + to);
					dragOrigin = null;
				}
				// TODO: call Makerbot to execute swipe				
			}			
			
			 
		});
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
            	if (bot != null)
            		bot.disconnect();
                System.exit(0);
            }
        } );

		Thread t = new Thread(){
			public void run(){
				while (true){
					BufferedImage screenshot = screen.getScreenshot(0, 0, 1280, 800);
					setImage(screenshot);	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}
				}
			}			
		};
		t.setName("capture");
		t.start();
	}

	public static void main(String[] args) {		
		final BotRemote frame = new BotRemote();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	protected void setImage(BufferedImage screenshot) {
		label.setIcon(new ImageIcon(screenshot));	
		label.invalidate();
	}

	private JLabel label;
}



