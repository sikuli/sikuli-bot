package org.sikuli.bot;

import java.awt.Point;
import java.util.Vector;

public class GenTester {

	public GenTester() {

	}

	private void test() throws Exception {
		Point origin = new Point(60, 60);
		int screenWidthmm = 218;
		int screenHeightmm = 136;

		GCodeGeneratorII generator = new GCodeGeneratorII(origin, screenWidthmm, screenHeightmm, 3, ".");
		Vector<String> code;

		Makerbot makerbot = new Makerbot();
		makerbot.connect();

		Point prevPoint = null;
		for (int x = 0; x < 200; x = x + 50){
			Point currPoint = new Point(x,x);		
			code = generator.createClickVector(currPoint, prevPoint, null);
			prevPoint = currPoint;
			makerbot.execute(code);	
		}
//		for (int x = 0; x < 400; x = x + 50){
//			Point currPoint = new Point(x,x);		
//			code = generator.createClickVector(currPoint, prevPoint);
//			prevPoint = currPoint;
//			makerbot.execute(code);	
//		}
		
				
//		makerbot.execute(code);
//		code = generator.createClickVector(new Point(40, 0), new Point(20, 0));		
//		makerbot.execute(code);
//		code = generator.createClickVector(new Point(60, 0), new Point(40, 0));		
//		makerbot.execute(code);
//		code = generator.createClickVector(new Point(60, 0), new Point(40, 0));		
//		makerbot.execute(code);
////		code = generator.createClickVector(new Point(80, 0));
////		makerbot.execute(code);
//		code = generator.createClickVector(new Point(800, 0));
//		makerbot.execute(code);

		//        code = generator.createClickVector(new Point(60, 0));
		//        code = generator.createClickVector(new Point(1280, 0));


		makerbot.disconnect();
	}

	public static void main(String[] args) throws Exception {
		GenTester tester = new GenTester();        
		tester.test();
	}

}