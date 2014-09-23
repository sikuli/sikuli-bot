package org.sikuli.bot;

import java.util.Vector;

public class MakerbotExample {

    public MakerbotExample() {
        
    }

    private void test() throws Exception {
        Makerbot makerbot = new Makerbot();
        Vector<String> code = getSourceCode();

        makerbot.connect();
        makerbot.execute(code);
        makerbot.execute(code);
        makerbot.disconnect();
    }
    
    private Vector<String> getSourceCode() {
        Vector<String> code = new Vector<String>();
        code.add("G90");
        code.add("G162 X Y F2500");
        code.add("G92 X0 Y0 Z0");
        code.add("G1 Z-110 F2300");
        code.add("G1 X-125 Y-25 F3000");
        code.add("G1 Z-137");
        code.add("G1 Z-110");
        code.add("G1 X-137");
        code.add("G1 Z-137");
        code.add("G1 Z-110");
        code.add("G1 X-115");
        code.add("G1 Z-137");
        code.add("G1 Z-110");
        code.add("G1 X-115 Y-35");
        code.add("G1 Z-140");
        code.add("G1 Z-110");
        code.add("G1 X-165 Y-20");
        code.add("G1 Z-137");
        code.add("G1 Z-110");
        code.add("G1 X-170");
        code.add("G1 Z-137");
        code.add("G1 X-100");
        code.add("G1 Z-110");
        code.add("G1 X0 Y0 Z0");
        code.add("M72 P1");
        return code;
    }

    public static void main(String[] args) throws Exception {
        MakerbotExample tester = new MakerbotExample();
        tester.test();
    }

}