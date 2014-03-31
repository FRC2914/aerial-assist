/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 * @author Toby
 */
public class ActiveRotationCorrectionSubsystem extends PIDSubsystem{

    private double desiredHeading = 0;
    boolean lockedRotation = true;
    private double PIDoutput;

    public ActiveRotationCorrectionSubsystem(){
        super(.02, 0, 0.02);//0.05,0,0.13 is really good!
        //oswald setting: 0.02,0,0.02
        enable();
    }
    
    public double getAngle(){
        double gyroAngle = RobotMap.GYRO.getAngle()%360;
        if(gyroAngle < -180){
            gyroAngle = (360 + gyroAngle);
        }else if(gyroAngle > 180){
            gyroAngle = -(360 - gyroAngle);
        }
        return gyroAngle;
    }

    protected double returnPIDInput() {
        return getAngle();
    }

    protected void usePIDOutput(double d) {
        PIDoutput = d;
    }

    public void setRelativeHeading(double heading){
        desiredHeading = getAngle()%360 + heading;
    }

    public void setAbsoluteHeading(double heading){
        desiredHeading = heading;
    }

    protected void initDefaultCommand() {
    }

    public void unlockRotation(){
        lockedRotation = false;
    }
    
    public double getPIDOutput(){
        return PIDoutput;
    }

    public void lockRotation(){
        lockedRotation = true;
    }

    public boolean isRotationLocked(){
        return lockedRotation;
    }




}
