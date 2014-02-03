/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.commands.driveCommands.DriveCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Toby
 */
public class AutonomousCommand extends CommandGroup{

    public AutonomousCommand() {
        //addParallel(new ShootOnHotCommand());
        addSequential(new DriveCommand());
    }
}
