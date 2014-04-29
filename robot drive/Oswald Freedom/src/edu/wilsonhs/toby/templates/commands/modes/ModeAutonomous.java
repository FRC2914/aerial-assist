/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author toby
 */
public class ModeAutonomous extends Mode{
    
    private boolean hasDriven = false;
    
    public ModeAutonomous(RobotDriveCommand command){
        super(command);
        command.setFaceForwards(true);
        CommandBase.activeRotationCorrectionSubsystem.setAbsoluteHeading(0);
    }

    public void onCommand(String[] command) {
        if(command[0].equals("hot")){
            System.out.println("hot");
            CommandBase.armSubsystem.deployArm();
            driveForwards(2000);
            //release latch
        }else if(command[0].equalsIgnoreCase("not") && DriverStation.getInstance().getMatchTime() > 6 && !hasDriven){
            CommandBase.armSubsystem.deployArm();
            driveForwards(2000);
            hasDriven = true;
        }
    }
    
    private void driveForwards(long time){
            long start = System.currentTimeMillis();
                System.out.println("started driving");
            while(System.currentTimeMillis() - start < time){
                RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 1, 0, 0);
            }
            
            RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 0, 0, 0);
            System.out.println("stopped driving");
    }
    
}
