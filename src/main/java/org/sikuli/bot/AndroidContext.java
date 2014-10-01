package org.sikuli.bot;


import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.slides.api.Context;

public class AndroidContext extends Context {

	private Makerbot makerbot;
		

	public AndroidContext(Makerbot makerbot){
		super(new DefaultScreenRegion(new AndroidScreen()));
		this.makerbot = makerbot;
		setMouse(new AndroidMouse(makerbot));
		setExecutionListener(null);
		try{
			makerbot.connect();
		}catch(IllegalStateException e){
			System.out.println(e);
			System.out.println("Makerbot is not connected.");
			makerbot = null;
		}
	}

}
