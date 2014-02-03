/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.network;

/**
 *
 * @author Toby
 */
public interface NetworkListener {
    public void onReceivePacket(Packet packet);
    public void onConnectToClient();
}
