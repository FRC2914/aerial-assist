/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 *
 * @author toby
 */
public class ModeShooting extends Mode{
    
    public ModeShooting(RobotDriveCommand driveCommand){
        super(driveCommand);
        driveCommand.setFaceForwards(true);
        CommandBase.armSubsystem.deployArm();
        CommandBase.activeRotationCorrectionSubsystem.setAbsoluteHeading(0);
    }

    public void onCommand(String[] command) {
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, command[0]);
        DriverStationLCD.getInstance().updateLCD();
    }
    
}
