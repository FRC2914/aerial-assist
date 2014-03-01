/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;

/**
 *
 * @author toby
 */
public class ModeAutonomous extends Mode{
    
    public ModeAutonomous(RobotDriveCommand command){
        super(command);
    }

    public void onCommand(String[] command) {
        if(command[0].equals("hhot")){
            //releaselatch
        }
    }
    
}
