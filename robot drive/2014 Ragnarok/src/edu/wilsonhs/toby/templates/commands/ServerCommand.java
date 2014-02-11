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

    public ServerCommand() {
            requires(serverSubsystem);
    }

    
    protected void initialize() {
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
