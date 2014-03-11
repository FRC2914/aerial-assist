package edu.wilsonhs.toby.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class RobotMap {
//Motors
    private static final Talon RIGHT_FRONT = new Talon(2);
    private static final Talon RIGHT_REAR = new Talon(1);
    private static final Talon LEFT_FRONT = new Talon(3);
    private static final Talon LEFT_REAR = new Talon(4);
    public static final Talon INTAKE_MOTOR = new Talon(5);
    
//Robot Drive
    public static final RobotDrive CHASSIS = new RobotDrive(LEFT_FRONT, LEFT_REAR, RIGHT_FRONT, RIGHT_REAR);
//Pneumatics
    public static final Relay COMPRESSOR = new Relay(1);
    public static final Solenoid INTAKE_OUT = new Solenoid(1);
    public static final Solenoid INTAKE_IN = new Solenoid(2);
    public static final Solenoid CATAPULT_OUT = new Solenoid(3);
    public static final Solenoid CATAPULT_IN = new Solenoid(4);
//Sensors
    public static final Gyro GYRO = new Gyro(1);
    public static final DigitalInput PRESSURE_SWITCH = new DigitalInput(1);
}
