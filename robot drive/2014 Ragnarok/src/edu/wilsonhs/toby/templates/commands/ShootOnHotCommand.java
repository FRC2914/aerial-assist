/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.network.NetworkListener;
import edu.wilsonhs.toby.network.Packet;
/**
 *
 * @author Toby
 */
class ShootOnHotCommand extends CommandBase implements NetworkListener{
    private boolean hasShot;

    public ShootOnHotCommand() {
        requires(serverSubsystem);
        requires(launcherSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return hasShot;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
    public void onReceivePacket(Packet packet) {
        if(packet.getType() == Packet.TYPE_HOT){
            if(packet.getBody().equals("hot")){
                launcherSubsystem.shoot();
            }
        }
    }

    public void onConnectToClient() {
    }
    
}
