/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Toby
 */
public class NetworkCommands extends CommandGroup{
    
    public NetworkCommands(){
        addParallel(new ServerCommand());
        addParallel(new PingCommand());
    }
    
}
