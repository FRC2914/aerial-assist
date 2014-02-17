/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.network.ModePacket;
import edu.wilsonhs.toby.network.Packet;

/**
 *
 * @author Toby
 */
public class TrackBallMode extends Mode{
    
    public TrackBallMode(){
        super(false);
    }

    public void onReceivePacket(Packet packet) {
        if(packet.getType() == Packet.TYPE_TRACKING){
            if(packet.getBody()[0].equals("ball")){
                if(Integer.parseInt(packet.getBody()[3]) > 0){
                    pointTowards(Integer.parseInt(packet.getBody()[1]));
                }
            }
        }
    }

    public void onConnectToClient() {
    }

    public void update() {
        
    }
    public Packet getPacket() {
       return new ModePacket(ModePacket.TRACK_BALL);
    }
    
}
