/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.network.NetworkListener;

/**
 *
 * @author Toby
 */
public class PingCommand extends CommandBase implements NetworkListener{
    
    private long timePingSent;

    protected void initialize() {
        requires(serverSubsystem);
        serverSubsystem.addListener(this);
    }

    protected void execute() {
        if(serverSubsystem.isConnectedToClient() && System.currentTimeMillis() - timePingSent > 500){
            serverSubsystem.resetConnetion();
        }
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
            timePingSent = System.currentTimeMillis();
        }
    }

    public void onConnectToClient() {
        serverSubsystem.sendPacket(new Packet("p"));
        timePingSent = System.currentTimeMillis();
    }
    
}
