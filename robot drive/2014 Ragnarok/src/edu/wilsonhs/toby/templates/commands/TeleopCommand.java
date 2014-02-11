/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.commands.driveCommands.DriveCommand;
import edu.wilsonhs.toby.templates.commands.modes.TrackBallMode;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author toby
 */
public class TeleopCommand extends CommandGroup{
    
    public TeleopCommand(){
        addParallel(new UserInputCommand());
        addParallel(new TrackBallMode());
        addParallel(new DriveCommand());
    }
    
    
}
