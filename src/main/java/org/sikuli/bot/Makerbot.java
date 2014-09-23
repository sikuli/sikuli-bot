package org.sikuli.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;
import replicatorg.app.util.serial.Name;
import replicatorg.app.util.serial.Serial;
import replicatorg.machine.Machine;
import replicatorg.machine.MachineCallbackHandler;
import replicatorg.machine.MachineFactory;
import replicatorg.machine.MachineListener;
import replicatorg.machine.MachineProgressEvent;
import replicatorg.machine.MachineState.State;
import replicatorg.machine.MachineStateChangeEvent;
import replicatorg.machine.MachineToolStatusEvent;
import replicatorg.model.StringListSource;

public class Makerbot {

    private static final String MACHINE_NAME = "Replicator 2";

    private Machine machine;
    private MachineCallbackHandler handler;
    private boolean isBusy;

    /**
     * Creates a connection to a Makerbot 3D printer for executing gcode. This
     * implementation assumes the machine type is a 'Replicator 2'.
     */
    public Makerbot() {
        isBusy = false;
    }

    /** 
     * Connect to Makerbot 3D printer. Assumes the connection will be with a
     * 'Replicator 2' over USB port. The USB port is automatically detected.
     * This function must be called before executing commands.
     */
    public void connect() {
        createMachine();
        String port = getUsbPort();
        machine.connect(port);
    }

    /**
     * Stops all work and disconnects from Makerbot 3D printer. 
     */
    public void disconnect() {
        machine.stopAll();
        machine.disconnect();
        machine.dispose();
        handler.interrupt();
    }

    /**
     * Executes gcode formated string. The string should be newline delimited,
     * with one line of gcode per line. This function blocks until the job has
     * completed.
     *
     * @param code the gcode to be executed
     * @throws MakerbotException 
     */
    public void execute(String code) throws MakerbotException {
        Scanner scanner = new Scanner(code);
        Vector<String> list = parse(scanner);
        execute(list);
    }

    /**
     * Executes gcode file. The file should be newline delimited, with one line
     * of gcode per line. This function blocks until the job has completed.
     *
     * @param code the gcode to be executed
     * @throws FileNotFoundException if given file cannot be located
     * @throws MakerbotException if problems occur while executing code
     */
    public void execute(File code) throws FileNotFoundException,
            MakerbotException {

        Scanner scanner = new Scanner(code);
        Vector<String> list = parse(scanner);
        execute(list);
    }

    /**
     * Executes list of gcode commands. Each entry in the <code>Vector</code>
     * should contain a single gcode command. This function blocks until the job
     * has completed. This function blocks until the job has completed.
     *
     * @param code the gcode to be executed
     * @throws MakerbotException if problems occur while executing code
     */
    public void execute(Vector<String> code) throws MakerbotException {
        StringListSource source = new StringListSource(code);
        machine.buildDirect(source);
        waitForCompletion();
    }

    // Creates a Vector of gcode from a scanner object
    // The scanner is closed upon completion
    private Vector<String> parse(Scanner scanner) {
        Vector<String> list = new Stack<String>();
        
        // add each line of code to list
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }

        scanner.close();
        return list;
    }

    // Creates connection to Makerbot and starts a handler thread
    private void createMachine() {
        handler = new MachineCallbackHandler();
        handler.addMachineListener(new MakerbotListener());
        machine = MachineFactory.load(MACHINE_NAME, handler);
        handler.start();
    }

    // Searches and returns a valid USB port
    // WARNING: uncertain what happens if multiple USB devices attached
    private String getUsbPort() {
        Vector<Name> ports = Serial.scanSerialNames();

        // search each port
        for (Name port : ports) {
            // usb port found
            if (port.getName().contains("tty.usb")) {
                return port.getName();
            }
        }

        // no usb ports found
        throw new IllegalStateException("no usb ports available");
    }

    // blocks until print job completes
    private void waitForCompletion() {
        isBusy = true;

        // wait until completion
        while (isBusy) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    /**
     * An implementation of <code>MachineListener</code> used for monitoring the
     * status of gcode execution.
     */
    private class MakerbotListener implements MachineListener {

        @Override
        public void machineStateChanged(MachineStateChangeEvent event) {
            State state = event.getState().getState();

            // check if build has finished
            if (state == State.FINISHED) {
                isBusy = false;
            }
        }

        @Override
        public void machineProgress(MachineProgressEvent event) {
            // do nothing
        }

        @Override
        public void toolStatusChanged(MachineToolStatusEvent event) {
            // do nothing
        }
        
    }

}