/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;

/**
 *
 * @author toby
 */
public class ModeNone extends Mode{

    public ModeNone(RobotDriveCommand driveCommand) {
        super(driveCommand);
        driveCommand.setFaceForwards(true);
        CommandBase.activeRotationCorrectionSubsystem.setAbsoluteHeading(0);
    }

    public void onCommand(String[] command) {
        
    }
    
}
