/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.driveCommands.DriveCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Dev
 */
public class PregameCommand extends CommandGroup {

    public PregameCommand() {
    }

    public synchronized void start() {
        super.start();
    }

    protected void initialize() {
        RobotMap.GYRO.reset();
    }

    protected void execute() {
        System.out.println(RobotMap.GYRO.getAngle());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
