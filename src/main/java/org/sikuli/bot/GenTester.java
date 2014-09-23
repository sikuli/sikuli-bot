package org.sikuli.bot;

import java.awt.Point;
import java.util.Vector;

public class GenTester {

    public GenTester() {
        
    }

    private void test() throws Exception {
        Point origin = new Point(60, 60);
        GCodeGenerator generator = new GCodeGenerator(origin, 140, 80, 3, ".");
		Vector<String> code;

        Makerbot makerbot = new Makerbot();
        makerbot.connect();

        code = generator.createClickVector(new Point(114, 375));
        makerbot.execute(code);

        code = generator.createClickVector(new Point(0, 0));
        makerbot.execute(code);

        makerbot.disconnect();
    }

    public static void main(String[] args) throws Exception {
        GenTester tester = new GenTester();        
        tester.test();
    }

}