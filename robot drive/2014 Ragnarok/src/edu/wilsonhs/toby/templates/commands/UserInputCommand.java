/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.general.DriveController;
import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author toby
 */
public class UserInputCommand extends DriveController{
    
    public UserInputCommand(){
        requires(activeRotationCorrectionSubsystem);
        requires(driveSubsystem);
        driveSubsystem.addController(this);
    }

    protected void initialize() {
    }

    public void update() {
        double SwitchX = OI.STICK.getRawAxis(5);
        double switchY = OI.STICK.getRawAxis(6);
        if(switchY == 1.0){
            activeRotationCorrectionSubsystem.unlockRotation();
            System.out.println("UNLOCKED");
        }else if(switchY == -1.0){
            activeRotationCorrectionSubsystem.lockRotation();
            System.out.println("LOCKED");
        }
    }
    
    

    protected boolean isFinished() {
        return !DriverStation.getInstance().isOperatorControl();
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    public double getX() {
        return OI.STICK.getX(); //To change body of generated methods, choose Tools | Templates.
    }

    public double getY() {
        return OI.STICK.getY(); //To change body of generated methods, choose Tools | Templates.
    }

    public double getRotation() {
        if(!activeRotationCorrectionSubsystem.isRotationLocked()){
            return OI.STICK.getTwist();
        }else{
            return 0;
        }
    }

    public int getRotationPriority() {
            return 2;
    }

    protected void execute() {
    }
    
    
    
    
    
}
