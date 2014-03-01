/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.network.ModePacket;
import edu.wilsonhs.toby.network.NetworkListener;
import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.modes.ModeNone;
import edu.wilsonhs.toby.templates.commands.modes.ModeShooting;
import edu.wilsonhs.toby.templates.commands.modes.ModeTrackBall;
import edu.wilsonhs.toby.templates.commands.modes.ModeTrackBump;
import edu.wpi.first.wpilibj.buttons.NetworkButton;

/**
 *
 * @author toby
 */
public class RobotDriveCommand extends CommandBase implements NetworkListener{
    private Mode mode = new ModeNone(this);
//    private long pingTimeout;
    private boolean locked = true;
    private double forwardModifier;
    private Packet lastSent;

    public RobotDriveCommand() {
        requires(serverSubsystem);
        requires(armSubsystem);
        serverSubsystem.addListener(this);
        RobotMap.CHASSIS.setSafetyEnabled(false);
    }
    
    
    

    public void onReceivePacket(Packet packet) {
//        if(packet.getType() == Packet.TYPE_PING){
//            new Thread(){
//
//                public void run() {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//            serverSubsystem.sendPacket(new Packet("p"));
//                }
//                
//            }.start();
//            pingTimeout = System.currentTimeMillis();
//        }
        if(packet.getType() == Packet.TYPE_MODE){
            if(packet.getUnparsedBody().equals("trackball")){
                mode = new ModeTrackBall(this);
            }else if(packet.getUnparsedBody().equals("trackbump")){
                mode = new ModeTrackBump(this);
            }else if(packet.getUnparsedBody().equals("shooting")){
                mode = new ModeShooting(this);
            }
        }else{
            mode.onCommand(packet.getBody());
        }
   }

    public void onConnectToClient() {
//        serverSubsystem.sendPacket(new Packet("p"));
//        pingTimeout = System.currentTimeMillis();
        lastSent = null;
    }

    public void update() {
        Packet toSend = null;
        double switchY = OI.STICK.getRawAxis(6);
        double switchX = OI.STICK.getRawAxis(5);
        if(switchY == 1.0){
            locked = false;
        }else if(switchY == -1.0){
            locked = true;
        }
        
        if(switchX == 1.0){
            toSend = new Packet("mtrackball");
        }else if(switchX == -1.0){
            toSend = new Packet("mtrackbump");
        }
        
        if(OI.STICK.getRawButton(2)){
            toSend = new Packet("mshooting");
        }
        if(toSend != null){
            if(!toSend.equals(lastSent)){
                lastSent = toSend;
                serverSubsystem.sendPacket(toSend);
            }
        }
    }

    protected void initialize() {
    }

    protected void execute() {
        
        
        if(locked){
//            RobotMap.CHASSIS.mecanumDrive_Cartesian(OI.STICK.getX(), OI.STICK.getY() * forwardModifier, mode.getRotation(), 0);
        }else{
//            RobotMap.CHASSIS.mecanumDrive_Cartesian(OI.STICK.getX(), OI.STICK.getY() * forwardModifier, OI.STICK.getTwist(), 0);
        }
    }
    
    public void setFaceForwards(boolean faceForwards){
        if(faceForwards){
            forwardModifier = 1.0;
        }else{
            forwardModifier = -1.0;
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
