package org.sikuli.bot;


import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.slides.api.Context;

public class AndroidContext extends Context {

	public AndroidContext(Makerbot makerbot){
		super(new DefaultScreenRegion(new AndroidScreen()));		
		setMouse(new AndroidMouse(makerbot));
		setExecutionListener(null);
		setMinScore(0.85f);
		try{
			makerbot.connect();
		}catch(IllegalStateException e){
			System.out.println(e);
			System.out.println("Makerbot is not connected.");
			makerbot = null;
		}
	}

}
