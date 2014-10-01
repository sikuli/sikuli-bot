package org.sikuli.bot;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class BotRemote extends JFrame {
	private AndroidMouse mouse;
	private AndroidScreen screen;	
	private Makerbot bot;

	public BotRemote() {
		setTitle("ImageViewer");
		setSize(1280, 800+60);
		label = new JLabel();
		Container contentPane = getContentPane();
		
		
		
		String name = "screen.png";
		label.setIcon(new ImageIcon(name));
		
		
//		JButton button = new JButton("Home");
//		button.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				int x = 1280/2;
//				int y = 800 + 80;
//				ScreenLocation loc = new DefaultScreenLocation(screen, x, y);
//				mouse.click(loc);
//			}
//		});
		
		
		JLabel buttons = new JLabel(new ImageIcon(getClass().getResource("/buttons.png")));
		
//		JButton back = new JButton("Back");
//		back.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				int x = 1280/2 + 150;
//				int y = 800 + 80;
//				ScreenLocation loc = new DefaultScreenLocation(screen, x, y);
//				mouse.click(loc);
//			}
//		});
		
		
		contentPane.add(label, BorderLayout.CENTER);
		contentPane.add(buttons, BorderLayout.PAGE_END);


		bot = new Makerbot();		
		try{
//			bot.disconnect();
			bot.connect();
		}catch(IllegalStateException e){
			System.out.println(e);
			System.out.println("Makerbot is not connected.");
			bot = null;
		}
		
		mouse = new AndroidMouse(bot);
		screen = new AndroidScreen();

		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("click: (" + e.getX() + "," + e.getY() + ")");
				
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
		t.start();
	}

	public static void main(String[] args) {		
		final BotRemote frame = new BotRemote();
		frame.setVisible(true);
	}

	protected void setImage(BufferedImage screenshot) {
		label.setIcon(new ImageIcon(screenshot));	
		label.invalidate();
	}

	private JLabel label;
}



