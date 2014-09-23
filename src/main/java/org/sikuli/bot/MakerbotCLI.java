package org.sikuli.bot;

import java.io.File;

public class MakerbotCLI {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("missing gcode filename argument");
            System.exit(0);
        }

        File file = new File(args[0]);

        Makerbot makerbot = new Makerbot();
        makerbot.connect();
        makerbot.execute(file);
        makerbot.disconnect();
    }

}