/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author toby
 */
public class PneumaticFillCommand extends CommandBase{
    private boolean switchBroken = false;
    private boolean filling = false;
    protected void initialize() {
        
    }

    protected void execute() {
        if(RobotMap.PRESSURE_SWITCH.get() && !switchBroken && !filling){
            new Thread(){
                public void run() {
                    filling = true;
                    long start = System.currentTimeMillis();
                    while(!RobotMap.PRESSURE_SWITCH.get() && System.currentTimeMillis() - start < 60000){
                        RobotMap.COMPRESSOR.set(Relay.Value.kOn);
                        RobotMap.COMPRESSOR.setDirection(Relay.Direction.kForward);
                    }
                    if(System.currentTimeMillis() - start > 60000){
                        switchBroken = true;
                    }
                    RobotMap.COMPRESSOR.set(Relay.Value.kOff);
                    filling = false;  
                }
                
            }.start();
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        RobotMap.COMPRESSOR.set(Relay.Value.kOff);
    }

    protected void interrupted() {
        RobotMap.COMPRESSOR.set(Relay.Value.kOff);
    }
    
}
