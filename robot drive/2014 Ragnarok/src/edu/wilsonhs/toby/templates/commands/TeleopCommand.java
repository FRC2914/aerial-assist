/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.commands.driveCommands.DriveCommand;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author toby
 */
public class TeleopCommand extends CommandGroup{
    
    public TeleopCommand(){
        addParallel(new DriveCommand());
        addParallel(new UserInputCommand());
    }
    
    
}
