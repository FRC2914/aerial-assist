/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.network.ModePacket;
import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.templates.commands.modes.Mode;

/**
 *
 * @author toby
 */
public class TrackBumpMode extends Mode {

    public TrackBumpMode() {
        super(true);
    }

    public void onReceivePacket(Packet packet) {
        if(packet.getType() == Packet.TYPE_TRACKING){
            if(packet.getBody()[0].equals("bump")){
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
       return new ModePacket(ModePacket.TRACK_BUMP);
    }
    
}
