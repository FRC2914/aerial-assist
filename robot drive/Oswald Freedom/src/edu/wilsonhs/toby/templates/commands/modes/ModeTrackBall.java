/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.modes;

import com.sun.squawk.util.MathUtils;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;

/**
 *
 * @author toby
 */
public class ModeTrackBall extends Mode {

    private double rotation = 0.0;
    private static final double ROTATION_PROPORTION_CONSTANT = 0.15;
    private static final long ROTATION_TIMEOUT = 333;
    private static final long DRIVE_TIMEOUT = 333;
    private static final int DEADZONE = 10;
    private static final double INITIAL_POWER = 0.21;
    private static final long INTAKE_MOTOR_TIMEOUT = 2000;
    private long intakeTimeout;
    private double drivePower = 0.0;
    private long timeout;
    private long driveTimeout;

    public ModeTrackBall(RobotDriveCommand driveCommand) {
        super(driveCommand);
        driveCommand.setFaceForwards(false);
    }

    public void onCommand(String[] command) {
        if (command[0].equals("ball")) {
            int x = Integer.parseInt(command[1]);
            int weight = Integer.parseInt(command[3]);
            if(weight == 99999){
                intakeTimeout = System.currentTimeMillis();
            }
            if (weight != 0 && Math.abs(x-160) > DEADZONE) {
                drivePower = 0;
//                rotation = (Math.abs((x - 160) / 160.0) / ((x - 160) / 160.0)) * MathUtils.pow((x - 160) / 160.0, 2) * -ROTATION_PROPORTION_CONSTANT;
                rotation = ((x - 160) / 160.0) * -ROTATION_PROPORTION_CONSTANT;
                if (rotation < 0) {
                    rotation -= INITIAL_POWER;
                } else {
                    rotation += INITIAL_POWER;
                }
                System.out.println(rotation);
                timeout = System.currentTimeMillis();
            } else if (Math.abs(x-160) < DEADZONE && weight != 0) {
                drivePower = 0.3;
                driveTimeout = System.currentTimeMillis();
                timeout = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - intakeTimeout < INTAKE_MOTOR_TIMEOUT){
                CommandBase.armSubsystem.setIntakeMotor(-1);
                System.out.println("intake motor on");
            }else{
                CommandBase.armSubsystem.setIntakeMotor(0);
                System.out.println("intake motor off");
            }
//            int x = Integer.parseInt(command[1]);
//            int weight = Integer.parseInt(command[3]);
//            if (weight != 0 && Math.abs(x) > DEADZONE) {
//                drivePower = 0;
//                System.out.println(((x - 160) / 160.0) * 21.0);
//                rotation = ((x - 160) / 160.0) * -ROTATION_PROPORTION_CONSTANT;
//                if (rotation < 0) {
//                    rotation -= INITIAL_POWER;
//                } else {
//                    rotation += INITIAL_POWER;
//                }
//                timeout = System.currentTimeMillis();
//            } else if (Math.abs(x) < DEADZONE && weight != 0) {
//                drivePower = 0.5;
//                driveTimeout = System.currentTimeMillis();
//                timeout = System.currentTimeMillis();
//            }
        }
    }

    public double getPower() {
        if (System.currentTimeMillis() - driveTimeout < DRIVE_TIMEOUT) {
            return drivePower;
        } else {
            drivePower = 0;
            return 0;
        }
    }

    public double getRotation() {
        if (System.currentTimeMillis() - timeout < ROTATION_TIMEOUT && drivePower == 0.0) {
            return rotation;
        } else {
            return 0;
        }
    }

}
