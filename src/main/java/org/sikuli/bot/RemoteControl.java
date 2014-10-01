package org.sikuli.bot;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;

import com.sun.codemodel.internal.JLabel;

public class RemoteControl {
	
	
	public static void main(String[] args) throws Exception {
		
			AndroidScreen screen = new AndroidScreen();
			BufferedImage screenshot = screen.getScreenshot(0, 0, 1280, 800);
			
			
			Canvas c = new DesktopCanvas();
			
			c.add().image(screenshot).centeredAt(new DesktopScreenRegion());
			c.show();
			
		
	}
	
	

}
