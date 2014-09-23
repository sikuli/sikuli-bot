
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
	private final int ZBOTTOM = 140;
	private final int STYLUS_Y_OFFSET = -60;
	private final int STYLUS_X_OFFSET = 0;
	
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
		this.origin = origin;
		this.length = length;
		this.height = height;
		this.width = width;
		this.filePath = filePath;
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
		int x = (int)((coord.getX()/800)*length);
		int y = (int)((coord.getY()/600)*height);
		
		if(x >= origin.getX() + 280)
			x = (int)(280 - origin.getX());
		if(x < 0)
			x = 0;
		if(y >= origin.getY() + 150)
			y = (int)(150 - origin.getY());
		if(y < 0)
			y = 0;
		
		return new Point(x, y);
	}

	/**
	 * Given the coordinate on the screen where the device should click, gcode is generated 
	 * 
	 * @param clickPt The point on the device where the maker bot should click. 
	 */
	public List<String> createClickVector(Point clickPt){
		//Calculate the new coordinate
		clickPt = findCoord(clickPt);
		List<String> vect = new ArrayList<String>();
		//Write to vector
			
		//Set Up stuff
		vect.add("M73 P2 \n" + "M103 \n" + "M73 P5 \n" + "M73 P0 \n");
		vect.add("G21 \n" + "G90 \n");
		vect.add("G162 X Y F2500 \n");
		vect.add("G92 X" + ((int)origin.getX() + STYLUS_X_OFFSET) + " Y" + ((int)origin.getY() + STYLUS_Y_OFFSET) + " Z0 \n");
		
		//Moving
		vect.add("G1 Z-" + (ZBOTTOM - 30 - width) + " F2300 \n"); //adjust to right height
		vect.add("G1 X-" + ((int) clickPt.getX()) + " Y-" + ((int) clickPt.getY()) + " F2300 \n"); //Move to pt
		vect.add("G1 Z-" + (ZBOTTOM - width - 1) + " \n"); //Click
		vect.add("G1 Z-" + (ZBOTTOM - 30 - width) + " \n");
		vect.add("G1 X0 Y0 Z0 \n"); //Return home
		//vect.add("M72 P1"); //Done music 
		
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
	
	public static void main(String[] args){
		//Piece of paper is 205x268 mm
		GCodeGenerator test = new GCodeGenerator(new Point(60,60), 140, 80, 3, "/Users/Ian Char/Documents/School/DLA/makerBotRobot/test/");
		test.createClickCode(new Point (114,375));
		test.createClickCode(new Point(0,0));
		test.createClickCode(new Point(571, 150));
	}
}