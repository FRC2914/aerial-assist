package edu.wilsonhs.toby.templates;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    private static Talon RIGHT_FRONT = new Talon(1);
    private static Talon RIGHT_BACK = new Talon(2);
    private static Talon LEFT_FRONT = new Talon(3);
    private static Talon LEFT_BACK = new Talon(4);
    public static RobotDrive CHASSIS = new RobotDrive(LEFT_FRONT, LEFT_BACK, RIGHT_FRONT, RIGHT_BACK);
    
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
}
