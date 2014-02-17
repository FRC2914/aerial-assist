/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.driveCommands.ActiveRotationCorrectionCommand;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 * @author Toby
 */
public class ActiveRotationCorrectionSubsystem extends PIDSubsystem{

    private double desiredHeading = 0;
    boolean lockedRotation = true;

    public ActiveRotationCorrectionSubsystem(){
        super(.02, 0, 0.02);//0.05,0,0.13 is really good!
        enable();
    }
    
    public double getAngle(){
        double gyroAngle = -RobotMap.GYRO.getAngle()%360;
        if(gyroAngle < -180){
            gyroAngle = (360 + gyroAngle);
        }else if(gyroAngle > 180){
            gyroAngle = -(360 - gyroAngle);
        }
        return gyroAngle;
    }

    protected double returnPIDInput() {
        System.out.println(getAngle());
        return getAngle();
    }

    protected void usePIDOutput(double d) {
        if(lockedRotation){
            ((ActiveRotationCorrectionCommand)getDefaultCommand()).correctAngle(d);
        }else{
            ((ActiveRotationCorrectionCommand)getDefaultCommand()).correctAngle(0);
        }
    }

    public void setRelativeHeading(double heading){
        desiredHeading = RobotMap.GYRO.getAngle()%360 + heading;
    }

    public void setAbsoluteHeading(double heading){
        desiredHeading = heading;
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new ActiveRotationCorrectionCommand());
    }

    public void unlockRotation(){
        lockedRotation = false;
    }

    public void lockRotation(){
        lockedRotation = true;
    }

    public boolean isRotationLocked(){
        return lockedRotation;
    }




}
