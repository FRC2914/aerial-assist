/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.driveCommands.ActiveRotationCorrectionCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Dev
 */
public class ActiveRotationCorrectionSubsystem extends PIDSubsystem{

    private double desiredHeading = 0;

    public ActiveRotationCorrectionSubsystem(){
        super(.05, 0, 0.13);//0.05,0,0.13 is really good!
        enable();
    }

    protected double returnPIDInput() {
        System.out.println("giving info");
        return RobotMap.GYRO.getAngle() - desiredHeading;
    }

    protected void usePIDOutput(double d) {
        System.out.println("sending info");
        ((ActiveRotationCorrectionCommand)getDefaultCommand()).correctAngle(d);
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new ActiveRotationCorrectionCommand());
    }
    

    
    
}
