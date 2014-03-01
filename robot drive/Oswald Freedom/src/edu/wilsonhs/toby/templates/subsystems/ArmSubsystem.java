/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author toby
 */
public class ArmSubsystem extends Subsystem {

    protected void initDefaultCommand() {

    }

    public void deployArm() {
        RobotMap.INTAKE_OUT.set(true);
        RobotMap.INTAKE_IN.set(false);
    }

    public void retractArm() {
        RobotMap.INTAKE_OUT.set(false);
        RobotMap.INTAKE_IN.set(true);
    }

    public void setIntakeMotor(double speed) {
        RobotMap.INTAKE_MOTOR.set(speed);
    }

}
