/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands;

/**
 *
 * @author toby
 */
public abstract class Mode {

    protected Mode(RobotDriveCommand driveCommand){
        
    }
    
    
    public double getRotation(){
        return CommandBase.activeRotationCorrectionSubsystem.getPIDOutput();
    }
    public abstract void onCommand(String[] command);
    
    
}
