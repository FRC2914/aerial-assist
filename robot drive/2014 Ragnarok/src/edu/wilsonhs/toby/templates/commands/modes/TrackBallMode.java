/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.network.Packet;

/**
 *
 * @author Toby
 */
public class TrackBallMode extends Mode{
    
    public TrackBallMode(){
        super(true);
    }
    

    protected void initialize() {
        faceForwards = false;
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
            if(packet.getBody()[0].equals("ball")){
                pointTowards(Integer.parseInt(packet.getBody()[1]));
            }
        }
    }

    public void onConnectToClient() {
    }
    
}
