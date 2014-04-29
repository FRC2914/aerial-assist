package edu.wilsonhs.toby.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.subsystems.ActiveRotationCorrectionSubsystem;
import edu.wilsonhs.toby.templates.subsystems.ArmSubsystem;
import edu.wilsonhs.toby.templates.subsystems.CatapultSubsystem;
import edu.wilsonhs.toby.templates.subsystems.ExampleSubsystem;
import edu.wilsonhs.toby.templates.subsystems.ServerSubsystem;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use
 * CommandBase.exampleSubsystem
 *
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static ServerSubsystem serverSubsystem = new ServerSubsystem();
    public static ActiveRotationCorrectionSubsystem activeRotationCorrectionSubsystem = new ActiveRotationCorrectionSubsystem();
    public static ArmSubsystem armSubsystem = new ArmSubsystem();
    public static CatapultSubsystem catapultSubsystem = new CatapultSubsystem();
    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static ExampleSubsystem exampleSubsystem = new ExampleSubsystem();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();
        new Thread() {
            public void run() {
                serverSubsystem.openServer();
                serverSubsystem.instantiateServer();
                serverSubsystem.startServerLoop();
            }
        }.start();
        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(exampleSubsystem);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
