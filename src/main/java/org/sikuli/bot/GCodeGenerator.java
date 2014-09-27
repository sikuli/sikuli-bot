package org.sikuli.bot;


import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GCodeGenerator {
	
	private int codeCount = 1; //Tells which gCode number is being generated is for naming purposes
	private Point origin;
	private int length;
	private int height;
	private int width;
	private String filePath;
	private Point lastClick;
	private final int ZBOTTOM = 100;
	private final int STYLUS_Y_OFFSET = 0;
	private final int STYLUS_X_OFFSET = -55;
	
	/**
	 * Constructor
	 * 
	 * @param origin Origin Point of the device (will probably be some corner of the screen)
	 * @param length Length of the screen in mm. Represents the x-axis.
	 * @param height Height of the screen in mm. Represents the y-axis.
	 * @param width Width of the device in mm. Represents the z-axis.
	 * @param filePath The file path where the generated gCode will be saved.
	 */
	public GCodeGenerator(Point origin, int length, int height, int width, String filePath){
		this.origin = new Point((int)(origin.getX() * -1), (int)(origin.getY() * -1));
		this.length = length;
		this.height = height;
		this.width = width;
		this.filePath = filePath;
		this.lastClick = new Point(0,0);;
	}
	
	private Point invertCoords(Point coord){
		return new Point(1280-(int)coord.getX() ,800 - (int)coord.getY());
	}
	
	/**
	 * Returns the physical coordinates of where the maker bot should click
	 * given the coordinates on the device screen.
	 * 
	 * @param coord A coordinate on device
	 * @return 		The physical coordinate on the maker bot
	 */
	private Point findCoord(Point coord){
		//Assume that screen is 600x800 pixels 
		//double made to int (possible loss in precision)
		//coordinates switched since screen is on its side
		coord = invertCoords(coord);
		int x = (int)((coord.getX()/1250)*length) * -1;
		int y = (int)((coord.getY()/800)*height) * -1;
		
		//Account for origin offset (absolute position)
		x = (int)(x + origin.getX() - STYLUS_X_OFFSET);
		y = (int)(y + origin.getY() - STYLUS_Y_OFFSET);
		
		//Ensure X and Y are in the bound of the MakerBot
		if(x <= origin.getX() - 275)
			x = (int)(-280 - origin.getX())*-1;
		if(x > 0)
			x = 0;
		if(y <= origin.getY() - 145)
			y = (int)(-150 - origin.getY())*-1;
		if(y > 0)
			y = 0;
		
		Point tempClick = new Point(x,y);
		
		//Calculate relative to last click point (relative position)
		x = (int)(x - lastClick.getX());
		y = (int)(y - lastClick.getY());

		lastClick = tempClick;
		//For Debugging
		System.out.println("X absolute: " + lastClick.getX());
		System.out.println("Y absolute: " + lastClick.getY());
		System.out.println("X movement: " + x);
		System.out.println("Y movement: " + y);
		return lastClick;
		//return new Point(x, y);
	}

	/**
	 * Given the coordinate on the screen where the device should click, gcode is generated 
	 * 
	 * @param clickPt The point on the device where the maker bot should click. 
	 */
	public List<String> createClickVector(Point clickPt){
		List<String> vect = new ArrayList<String>();
		//Write to vector
			
		//Set Up stuff
		vect.add("M73 P2 \n" + "M103 \n" + "M73 P5 \n" + "M73 P0 \n");
		vect.add("G21 \n" + "G90 \n");
		//vect.add("G162 X Y F2500 \n");
		if(codeCount == 1)
			vect.add("G92 X" + ((int)lastClick.getX()) + " Y" + ((int)lastClick.getY()) + " Z0 \n");
		else
			vect.add("G92 X" + ((int)lastClick.getX()) + " Y" + ((int)lastClick.getY()) + " Z-" + (ZBOTTOM - 30 -  width) + "\n");
		//Calculate the new coordinate
		clickPt = findCoord(clickPt);
		
		//Moving
		if(codeCount == 1)
			vect.add("G1 Z-" + (ZBOTTOM - 30 - width) + " F2300 \n"); //adjust to right height
		vect.add("G1 X" + ((int) clickPt.getX()) + " Y" + ((int) clickPt.getY()) + " F2300 \n"); //Move to pt
		vect.add("G1 Z-" + (ZBOTTOM - width - 1) + " \n"); //Click
		vect.add("G1 Z-" + (ZBOTTOM - 30 -  width) + " F2300 \n");
		//vect.add("G1 X0 Y0 Z0 \n"); //Return home
		//vect.add("M72 P1"); //Done music 
		
		return vect;
	}
	
	/**
	 * Given two coordinates on the screen where the device start and end swipe 
	 * 
	 * @param clickPt1 The point on the device where the maker bot should click.
	 * @param clickPt2 The point the device should drag to
	 */
	public List<String> createSwipeVector(Point clickPt1, Point clickPt2){
		List<String> vect = new ArrayList<String>();
		//Write to vector
			
		//Set Up stuff
		vect.add("M73 P2 \n" + "M103 \n" + "M73 P5 \n" + "M73 P0 \n");
		vect.add("G21 \n" + "G90 \n");
		//vect.add("G162 X Y F2500 \n");
		if(codeCount == 1)
			vect.add("G92 X" + ((int)lastClick.getX()) + " Y" + ((int)lastClick.getY()) + " Z0 \n");
		else
			vect.add("G92 X" + ((int)lastClick.getX()) + " Y" + ((int)lastClick.getY()) + " Z-" + (ZBOTTOM - 30 -  width) + "\n");
		//Calculate the new coordinate
		clickPt1 = findCoord(clickPt1);
		clickPt2 = findCoord(clickPt2);
		
		//Moving
		if(codeCount == 1)
			vect.add("G1 Z-" + (ZBOTTOM - 30 - width) + " F2300 \n"); //adjust to right height
		vect.add("G1 X" + ((int) clickPt1.getX()) + " Y" + ((int) clickPt1.getY()) + " F2300 \n"); //Move to pt
		vect.add("G1 Z-" + (ZBOTTOM - width - 1) + " \n"); //Click
		vect.add("G1 X" + ((int) clickPt2.getX()) + " Y" + ((int) clickPt2.getY()) + " F2300 \n"); //Drag
		vect.add("G1 Z-" + (ZBOTTOM - 30 -  width) + " F2300 \n");
		//vect.add("G1 X0 Y0 Z0 \n"); //Return home
		//vect.add("M72 P1"); //Done music 
		
		return vect;
	}
	
	/**
	 * Creates code to send back to the origin
	 * 
	 * @param clickPt The point on the device where the maker bot should click. 
	 */
	public List<String> createFinishVector(){
		List<String> vect = new ArrayList<String>();
		//Write to vector
			
		//Set Up stuff
		vect.add("M73 P2 \n" + "M103 \n" + "M73 P5 \n" + "M73 P0 \n");
		vect.add("G21 \n" + "G90 \n");
		vect.add("G162 X Y F2500 \n");
		vect.add("G92 X" + 0 + " Y" + 0 + " Z0 \n");
		
		//Moving
		vect.add("G1 X0 Y0 Z60 \n"); //Return home
		vect.add("M72 P1"); //Done music 
		
		return vect;
	}
	
	/**
	 * Given the coordinate on the screen where the device should click, gcode is generated 
	 * 
	 * @param clickPt The point on the device where the maker bot should click. 
	 */
	public void createClickCode(Point clickPt){
		//Calculate the new coordinate
		List<String> vect = createClickVector(clickPt);
		//Write to file
		try{
			File newCode = new File(filePath + "gCode" + codeCount +".gcode");
			newCode.createNewFile();
			
			FileWriter fw = new FileWriter(newCode.getAbsoluteFile());
			BufferedWriter writer = new BufferedWriter(fw);
			
			for(String line: vect)
			{
				writer.write(line);
			}
			
			writer.close();
			
			System.out.println("Done with instruction set" + codeCount);
			codeCount++;
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Given the coordinate on the screen where the device should click, gcode is generated 
	 * 
	 * @param clickPt The point on the device where the maker bot should click. 
	 */
	public void createSwipeCode(Point clickPt1, Point clickPt2){
		//Calculate the new coordinate
		List<String> vect = createSwipeVector(clickPt1, clickPt2);
		//Write to file
		try{
			File newCode = new File(filePath + "gCode" + codeCount +".gcode");
			newCode.createNewFile();
			
			FileWriter fw = new FileWriter(newCode.getAbsoluteFile());
			BufferedWriter writer = new BufferedWriter(fw);
			
			for(String line: vect)
			{
				writer.write(line);
			}
			
			writer.close();
			
			System.out.println("Done with instruction set" + codeCount);
			codeCount++;
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Given the coordinate on the screen where the device should click, gcode is generated 
	 * 
	 * @param clickPt The point on the device where the maker bot should click. 
	 */
	public void createFinishCode(){
		//Calculate the new coordinate
		List<String> vect = createFinishVector();
		//Write to file
		try{
			File newCode = new File(filePath + "finish" + ".gcode");
			newCode.createNewFile();
			
			FileWriter fw = new FileWriter(newCode.getAbsoluteFile());
			BufferedWriter writer = new BufferedWriter(fw);
			
			for(String line: vect)
			{
				writer.write(line);
			}
			
			writer.close();
			
			System.out.println("Done with instruction set" + codeCount);
			codeCount++;
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//Piece of paper is 205x268 mm
		GCodeGenerator test = new GCodeGenerator(new Point(40,20), 218, 137, 3, "/Users/Ian Char/Documents/School/DLA/makerBotRobot/test/");
		test.createClickCode(new Point (0,0));
		test.createClickCode(new Point(1280,0));
		test.createClickCode(new Point(1280, 800));
		test.createClickCode(new Point(0, 800));
		test.createSwipeCode(new Point(0,800), new Point(0,0));
		test.createFinishCode();
	}
}
