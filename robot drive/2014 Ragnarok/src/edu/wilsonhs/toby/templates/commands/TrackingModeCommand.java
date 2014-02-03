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
public class TrackingModeCommand extends CommandBase implements NetworkListener{
    
    public TrackingModeCommand(){
        requires(driveSubsystem);
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

    public void onReceivePacket(Packet packet) {
        if(packet.getType() == Packet.TYPE_TRACKING){
            String[] data = packet.getBody();
            if(data[0].equals("ball")){
                driveSubsystem.setFront(false);
            }else{
                driveSubsystem.setFront(true);
                
            }
        }
    }

    public void onConnectToClient() {
    }
    
}
