/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Toby
 */
public class PregameCommand extends CommandGroup {

    public PregameCommand() {
//        Command netCommand = new NetworkCommands();
//        netCommand.setRunWhenDisabled(true);
//        addParallel(netCommand);
    }

    public synchronized void start() {
        super.start();
    }

    protected void initialize() {
        RobotMap.GYRO.reset();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
