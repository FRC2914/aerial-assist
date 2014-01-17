/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    private final Joystick LEFT_STICK = new Joystick(1);
    private final Joystick RIGHT_STICK = new Joystick(2);
    private final Talon FRONT_LEFT_MOTOR = new Talon(4);//Drive
    private final Talon REAR_LEFT_MOTOR = new Talon(3);//Drive
    private final Talon FRONT_RIGHT_MOTOR = new Talon(2);//Drive
    private final Talon REAR_RIGHT_MOTOR = new Talon(1);//Drive
    private final RobotDrive CHASSIS = new RobotDrive(FRONT_LEFT_MOTOR, REAR_LEFT_MOTOR, FRONT_RIGHT_MOTOR, REAR_RIGHT_MOTOR);
    public void operatorControl() {
    while(isOperatorControl()) {
    CHASSIS.tankDrive(RIGHT_STICK.getAxis(Joystick.AxisType.kY), LEFT_STICK.getAxis(Joystick.AxisType.kY));
    }    
    }
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    
}
