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

    public ActiveRotationCorrectionSubsystem(){
        super(.05, 0, 0.13);//0.05,0,0.13 is really good!
        enable();
    }

    protected double returnPIDInput() {
        return RobotMap.GYRO.getAngle()%360 - desiredHeading;
    }

    protected void usePIDOutput(double d) {
        ((ActiveRotationCorrectionCommand)getDefaultCommand()).correctAngle(d);
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
    

    
    
}
