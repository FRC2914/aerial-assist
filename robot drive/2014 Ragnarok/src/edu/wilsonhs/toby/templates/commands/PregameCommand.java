/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Toby
 */
public class PregameCommand extends CommandGroup {

    public PregameCommand() {
        requires(CommandBase.serverSubsystem);
        Command server = new ServerCommand();
        Command ping = new PingCommand();
        server.setRunWhenDisabled(true);
        ping.setRunWhenDisabled(true);
        addParallel(server);
        addParallel(ping);
    }

    protected void initialize() {
        CommandBase.serverSubsystem.openServer();
        CommandBase.serverSubsystem.instantiateServer();
        super.initialize();
        RobotMap.GYRO.reset();
        CommandBase.serverSubsystem.startServerLoop();
        System.out.println("continue");
    }
}
