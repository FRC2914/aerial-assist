/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author toby
 */
public class UserInputCommand extends CommandBase{
    
    public UserInputCommand(){
        requires(activeRotationCorrectionSubsystem);
        requires(driveSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        double x = OI.STICK.getRawAxis(5);
        double y = OI.STICK.getRawAxis(6);
        if(y == 1.0){
            activeRotationCorrectionSubsystem.unlockRotation();
            System.out.println("UNLOCKED");
        }else if(y == -1.0){
            activeRotationCorrectionSubsystem.lockRotation();
            System.out.println("LOCKED");
        } 
        
        if(!activeRotationCorrectionSubsystem.isRotationLocked()){
            driveSubsystem.drive(0, 0, OI.STICK.getTwist());
        }
    }

    protected boolean isFinished() {
        return !DriverStation.getInstance().isOperatorControl();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
