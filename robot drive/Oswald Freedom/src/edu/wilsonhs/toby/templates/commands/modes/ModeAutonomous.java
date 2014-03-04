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

/**
 *
 * @author toby
 */
public class ModeAutonomous extends Mode{
    
    public ModeAutonomous(RobotDriveCommand command){
        super(command);
        command.setFaceForwards(true);
        CommandBase.activeRotationCorrectionSubsystem.setAbsoluteHeading(0);
    }

    public void onCommand(String[] command) {
        if(command[0].equals("hot")){
            System.out.println("hot");
            //release latch
            long start = System.currentTimeMillis();
                System.out.println("started driving");
            while(System.currentTimeMillis() - start < 2000){
                RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 1, 0, 0);
            }
            System.out.println("stopped driving");
            RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 0, 0, 0);
        }
    }
    
}
