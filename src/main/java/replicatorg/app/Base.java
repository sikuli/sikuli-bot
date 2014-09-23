package replicatorg.app;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import replicatorg.machine.MachineLoader;

public class Base {

    public static final int VERSION = 40;
    public static final String VERSION_NAME = "1";

    public static final Logger logger = Logger.getAnonymousLogger();

    public static final Preferences preferences = null;

    public static DataCapture capture = null;

	static public NumberFormat getGcodeFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
	}

    public static void copyDir(File srcDir, File dstDir) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String loadFile(File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String saveFile(String program, File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static MachineLoader getMachineLoader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static File getApplicationFile(String path) {
        return new File("replicatorg", path);
    }

    public static File getUserFile(String machines, boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static boolean isLinux() {
        return true;
    }

    public static boolean isWindows() {
        return false;
    }

    public static void openURL(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}