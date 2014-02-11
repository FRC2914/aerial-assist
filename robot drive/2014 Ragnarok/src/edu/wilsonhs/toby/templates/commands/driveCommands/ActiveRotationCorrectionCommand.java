/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.driveCommands;

import edu.wilsonhs.toby.general.DriveController;

/**
 *
 * @author Toby
 */
public class ActiveRotationCorrectionCommand extends DriveController{

    private double rotation = 0.0;
    public ActiveRotationCorrectionCommand(){
        requires(driveSubsystem);
        requires(activeRotationCorrectionSubsystem);
        driveSubsystem.addController(this);
   }
    protected void initialize() {
    }

    protected void execute() {
    }
    
    public void correctAngle(double amount){
        rotation = -amount;
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    public double getRotation() {
        return rotation;
    }

    public int getRotationPriority() {
        return 1;
    }
    
    
    
}
