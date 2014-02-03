/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

/**
 *
 * @author Toby
 */
public class ServerCommand extends CommandBase{

    protected void initialize() {
        requires(serverSubsystem);
        serverSubsystem.instantiateServer();
        serverSubsystem.openServer();
    }

    protected void execute() {
        serverSubsystem.recieveData();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
