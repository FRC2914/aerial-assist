/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Dev
 */
public class PregameCommand extends CommandGroup {

    public PregameCommand() {
        RobotMap.GYRO.startRecording();
    }

    public synchronized void start() {
        super.start();
    }

    protected void initialize() {
    }

    protected void execute() {
        RobotMap.GYRO.getAngle();
    }

    protected boolean isFinished() {
        return !DriverStation.getInstance().isDisabled();
    }

    protected void end() {
        RobotMap.GYRO.stopRecording();
    }

    protected void interrupted() {
        RobotMap.GYRO.stopRecording();
    }
}
