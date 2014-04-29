/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.network;

import edu.wilsonhs.toby.templates.StringUtils;

/**
 *
 * @author Toby
 */
public class Packet {
    public static final char TYPE_PING = 'p';
    public static final char TYPE_TRACKING = 't';
    public static final char TYPE_HOT = 'h';
    public static final char TYPE_MODE = 'm';
    public static final char TYPE_SHOOT = 's';
    private char packetType;
    private String body;
    public Packet(String body){
        this.body = body.substring(1);
        this.packetType = body.charAt(0);
    }
    
    public String[] getBody(){
        return StringUtils.splitAt(body, ",");
    }
    
    public String getUnparsedBody(){
        return body;
    }
    
    public char getType(){
        return packetType;
    }
    
    public boolean equals(Object o){
        if(o instanceof Packet){
            Packet p = (Packet) o;
            return p.getUnparsedBody().equals(getUnparsedBody()) && getType() == p.getType();
        }
        return false;
    }
}
