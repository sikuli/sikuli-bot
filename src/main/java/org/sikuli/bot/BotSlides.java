package org.sikuli.bot;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.slideshow.SlideShowViewer;

public class BotSlides {

    public BotSlides() {
        
    }

    public static void main(String[] args) throws Exception {
    	    	
    	final Makerbot bot = new Makerbot();
    	final Context context = new AndroidContext(bot);    
    	final SlideShowViewer viewer = new SlideShowViewer();
    	viewer.setContext(context);
    	viewer.setLocationRelativeTo(null);
		viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer.setVisible(true);	
		viewer.setTitle("Sikuli Bot + Slides");
		
		String input;
		if (args.length >= 1){
			input = args[0];
			viewer.invokeOpen(new File(input));
		}			
		
		viewer.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                bot.disconnect();
                System.exit(0);
            }
        } );

		
    }

}