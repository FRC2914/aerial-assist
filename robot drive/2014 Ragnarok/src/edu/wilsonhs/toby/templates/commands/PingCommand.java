/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.network.PacketListener;

/**
 *
 * @author Dev
 */
public class PingCommand extends CommandBase implements PacketListener{

    protected void initialize() {
        requires(serverSubsystem);
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

    public void onReceivePacket(Packet packet) {
        if(packet.getType() == Packet.TYPE_PING){
            serverSubsystem.sendPacket(packet);
        }
    }
    
}
