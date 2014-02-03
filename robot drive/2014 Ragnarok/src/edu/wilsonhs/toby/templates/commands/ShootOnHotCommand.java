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
class ShootOnHotCommand extends CommandBase implements PacketListener{
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
    
}
