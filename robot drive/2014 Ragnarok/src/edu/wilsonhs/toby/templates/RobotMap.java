package edu.wilsonhs.toby.templates;

import edu.wilsonhs.toby.general.EnhancedGyro;
import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
//Motors
    private static final Jaguar RIGHT_FRONT = new Jaguar(2);
    private static final Jaguar RIGHT_REAR = new Jaguar(3);
    private static final Jaguar LEFT_FRONT = new Jaguar(4);
    private static final Jaguar LEFT_REAR = new Jaguar(5);
//Robot Drive
    public static final RobotDrive CHASSIS = new RobotDrive(LEFT_FRONT, LEFT_REAR, RIGHT_FRONT, RIGHT_REAR);
//Sensors
//    public static final Accelerometer ACC = new Accelerometer(1);
//    public static final EnhancedGyro GYRO = new EnhancedGyro(2, ACC);
    public static final Gyro GYRO = new Gyro(2);
//Constants
    public static final boolean IS_GYRO_UPSIDE_DOWN = true;
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
}
