/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.network.ModePacket;
import edu.wilsonhs.toby.network.NetworkListener;
import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.templates.commands.modes.Mode;
import edu.wilsonhs.toby.templates.commands.modes.TrackBallMode;
import edu.wilsonhs.toby.templates.commands.modes.TrackBumpMode;

/**
 *
 * @author toby
 */
public class ModeController implements NetworkListener{
    private Mode currMode = null;
    public ModeController(){
        CommandBase.serverSubsystem.addListener(this);
    }

    public void onReceivePacket(Packet packet) {
        if(packet.getType() == Packet.TYPE_MODE){
            String mode = packet.getBody()[0].substring(1);
            if(!currMode.getPacket().equals(packet)){
            if(mode.equals(ModePacket.TRACK_BALL)){
                currMode = new TrackBallMode();
            }else if(mode.equals(ModePacket.TRACK_BUMP)){
                currMode = new TrackBumpMode();
            }
            }
        }
    }

    public void onConnectToClient() {
    }

    public void update() {
    }
    
}
