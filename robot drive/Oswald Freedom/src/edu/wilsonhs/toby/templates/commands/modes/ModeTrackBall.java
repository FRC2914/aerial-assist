/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;

/**
 *
 * @author toby
 */
public class ModeTrackBall extends Mode{

    public ModeTrackBall(RobotDriveCommand driveCommand) {
        super(driveCommand);
        driveCommand.setFaceForwards(false);
    }

    public void onCommand(String[] command) {
        if(command[0].equals("ball")){
            int x = Integer.parseInt(command[1]);
            CommandBase.activeRotationCorrectionSubsystem.setRelativeHeading(-(((x-160)/160.0) * 21.0));
        }
    }
    
}
