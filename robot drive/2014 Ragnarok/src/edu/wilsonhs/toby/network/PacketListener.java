/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.network;

/**
 *
 * @author Dev
 */
public interface PacketListener {
    public void onReceivePacket(Packet packet);
    
}
