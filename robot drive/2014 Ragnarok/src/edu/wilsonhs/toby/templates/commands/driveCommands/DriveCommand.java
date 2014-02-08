/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.driveCommands;

import edu.wilsonhs.toby.templates.commands.CommandBase;
/**
 *
 * @author Toby
 */
public class DriveCommand extends CommandBase{
    
    public DriveCommand(){
        requires(driveSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        System.out.println("PRINTING");
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
