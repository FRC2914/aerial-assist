/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands;

import com.sun.squawk.util.MathUtils;
import edu.wilsonhs.toby.network.ModePacket;
import edu.wilsonhs.toby.network.NetworkListener;
import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.templates.OI;
import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.modes.ModeAutonomous;
import edu.wilsonhs.toby.templates.commands.modes.ModeNone;
import edu.wilsonhs.toby.templates.commands.modes.ModeShooting;
import edu.wilsonhs.toby.templates.commands.modes.ModeTrackBall;
import edu.wilsonhs.toby.templates.commands.modes.ModeTrackBump;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.buttons.NetworkButton;

/**
 *
 * @author toby
 */
public class RobotDriveCommand extends CommandBase implements NetworkListener {

    private Mode mode = new ModeNone(this);
//    private long pingTimeout;
    private boolean locked = true;
    private double forwardModifier;
    private Packet lastSent;
    public boolean fireButtonDownLastLoop = false;
    private boolean ballSwitchLastPos = false;

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
        if (packet.getType() == Packet.TYPE_MODE) {
            if (packet.getUnparsedBody().equals("trackball")) {
                mode = new ModeTrackBall(this);
            } else if (packet.getUnparsedBody().equals("trackbump")) {
                mode = new ModeTrackBump(this);
            } else if (packet.getUnparsedBody().equals("shooting")) {
                mode = new ModeShooting(this);
            } else if (packet.getUnparsedBody().equals("autonomous")) {
                mode = new ModeAutonomous(this);
            }
        } else {
            mode.onCommand(packet.getBody());
        }
        
    }

    public void onConnectToClient() {
//        serverSubsystem.sendPacket(new Packet("p"));
//        pingTimeout = System.currentTimeMillis();
        serverSubsystem.sendPacket(new Packet("mnone"));
        lastSent = null;
    }

    public void update() {
        Packet toSend = null;
        double switchX = OI.STICK.getRawAxis(5);

        if (switchX == 1.0) {
            toSend = new Packet("mtrackball");
        } else if (switchX == -1.0) {
            toSend = new Packet("mtrackbump");
        }

        if (OI.STICK.getRawButton(2)) {
            toSend = new Packet("mshooting");
        }
        if (!(mode instanceof ModeAutonomous) && DriverStation.getInstance().isAutonomous()) {
//            toSend = new Packet("mautonomous");
            armSubsystem.retractArm();
        }
        if (toSend != null) {
            if (!toSend.equals(lastSent)) {
                lastSent = toSend;
                serverSubsystem.sendPacket(toSend);
            }
        }
    }

    protected void initialize() {
        RobotMap.GYRO.reset();
        System.out.println("motors inverted");
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }

    protected void execute() {
        System.out.println(RobotMap.BALL_SWITCH.get());
        double switchY = OI.STICK.getRawAxis(6);
        if (switchY == 1.0) {
            locked = false;
        } else if (switchY == -1.0) {
            locked = true;
        }
        if(OI.STICK.getRawButton(1)){
            catapultSubsystem.startTension();
        }else if(fireButtonDownLastLoop){
            catapultSubsystem.fire();
        }else if(OI.STICK.getRawButton(7)){
            catapultSubsystem.startDetension();
        }else{
            catapultSubsystem.stopTension();
        }
        if(OI.STICK.getRawButton(3)){
            armSubsystem.retractArm();
        }else if(OI.STICK.getRawButton(5)){
            armSubsystem.deployArm();
        }
        
        if(OI.STICK.getRawButton(11) && OI.STICK.getRawButton(12)){
            RobotMap.GYRO.reset();
        }
        
        if(OI.STICK.getRawButton(6)){
            RobotMap.INTAKE_MOTOR.set(OI.STICK.getThrottle()*2.0 - 1.0);
        }else{
            RobotMap.INTAKE_MOTOR.set(0);
        }

        if (!DriverStation.getInstance().isAutonomous()) {
            if (locked) {
                System.out.println("turn: " + mode.getRotation());
                double move = OI.STICK.getY();
                if(mode instanceof ModeTrackBall){
                    if(((ModeTrackBall)mode).getPower() != 0.0){
                        move = ((ModeTrackBall)mode).getPower();
                    }
                }
                RobotMap.CHASSIS.mecanumDrive_Cartesian(OI.STICK.getX(), move * forwardModifier, mode.getRotation(), 0);
            } else {
                RobotMap.CHASSIS.mecanumDrive_Cartesian(-forwardModifier * OI.STICK.getX(), OI.STICK.getY() * -forwardModifier, -deadzone(OI.STICK.getTwist()), 0);
//                RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 0, 0, 0);
            }
            
            if(!RobotMap.BALL_SWITCH.get() && ballSwitchLastPos){
                serverSubsystem.sendPacket(new Packet("mshooting"));
                mode = new ModeShooting(this);
                locked = true;
            }
            ballSwitchLastPos = RobotMap.BALL_SWITCH.get();
        }
        fireButtonDownLastLoop = OI.STICK.getRawButton(1);
    }

    public void setFaceForwards(boolean faceForwards) {
        if (faceForwards) {
            forwardModifier = 1.0;
        } else {
            forwardModifier = -1.0;
        }
    }
    
    private double deadzone(double twist){
        if(Math.abs(twist) < 0.3){
            return 0;
        }
        System.out.println(twist);
        return (Math.abs(twist)/twist)*MathUtils.pow(twist, 2);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }

}
