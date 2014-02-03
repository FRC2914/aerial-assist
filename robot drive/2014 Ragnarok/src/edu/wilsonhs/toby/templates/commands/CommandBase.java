package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.subsystems.ActiveRotationCorrectionSubsystem;
import edu.wilsonhs.toby.templates.subsystems.LauncherSubsystem;
import edu.wilsonhs.toby.templates.subsystems.RobotDriveSubsystem;
import edu.wilsonhs.toby.templates.subsystems.ServerSubsystem;
import edu.wpi.first.wpilibj.command.Command;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static ServerSubsystem serverSubsystem = new ServerSubsystem();
    public static RobotDriveSubsystem driveSubsystem = new RobotDriveSubsystem();
    public static ActiveRotationCorrectionSubsystem activeRotationCorrectionSubsystem = new ActiveRotationCorrectionSubsystem();
    public static LauncherSubsystem launcherSubsystem = new LauncherSubsystem();
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();
        
        // Show what command your subsystem is running on the SmartDashboard
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
