/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.network;

/**
 *
 * @author Toby
 */
public class ModePacket extends Packet{
    public static final String AUTONOMOUS = "autonomous";
    public static final String TRACK_BALL = "trackball";
    public static final String TRACK_BUMP = "trackbump";
    public static final String SHOOT      = "shoot";
    public static final String NONE       = "none";
    
    public ModePacket(String mode){
        super("m"+mode);
    }
}
