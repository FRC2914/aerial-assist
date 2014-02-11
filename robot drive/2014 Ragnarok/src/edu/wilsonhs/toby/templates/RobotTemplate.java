/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wilsonhs.toby.templates;

import edu.wilsonhs.toby.templates.commands.AutonomousCommand;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.NetworkCommands;
import edu.wilsonhs.toby.templates.commands.PregameCommand;
import edu.wilsonhs.toby.templates.commands.TeleopCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    Command autonomousCommand;
    Command disabledCommand;
    Command networkCommands;
    Command teleopCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        // instantiate the command used for the autonomous period
        autonomousCommand = new AutonomousCommand();
        disabledCommand   = new PregameCommand();
//        networkCommands   = new NetworkCommands();
        teleopCommand     = new TeleopCommand();
        RobotMap.GYRO.setSensitivity(0.007);
        disabledCommand.setRunWhenDisabled(true);
        new Thread(){
            public void run(){
                CommandBase.driveSubsystem.startDriveLoop();
            }
        }.start();



        // Initialize all subsystems
        CommandBase.init();


    }

    public void disabledInit() {
        disabledCommand.start();
    }

    public void disabledPeriodic() {
            Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
//        autonomousCommand.start();
        disabledCommand.cancel();
        disabledCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        System.out.println("TELEOP");
        teleopCommand.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
