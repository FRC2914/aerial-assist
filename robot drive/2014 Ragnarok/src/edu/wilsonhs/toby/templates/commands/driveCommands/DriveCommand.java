/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.driveCommands;

import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author Dev
 */
public class DriveCommand extends CommandBase{
    
    public DriveCommand(){
        
        requires(driveSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        driveSubsystem.drive();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
