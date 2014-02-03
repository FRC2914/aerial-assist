/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.driveCommands;

import edu.wilsonhs.toby.templates.commands.CommandBase;

/**
 *
 * @author Dev
 */
public class ActiveRotationCorrectionCommand extends CommandBase{

   public ActiveRotationCorrectionCommand(){
        requires(driveSubsystem);
        requires(activeRotationCorrectionSubsystem);
   }
    protected void initialize() {
    }

    protected void execute() {
    }
    
    public void correctAngle(double amount){
        driveSubsystem.drive(0, 0, -amount);
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
