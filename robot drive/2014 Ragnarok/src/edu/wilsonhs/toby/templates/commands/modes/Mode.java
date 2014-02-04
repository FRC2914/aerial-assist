/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.modes;

import edu.wilsonhs.toby.network.NetworkListener;
import edu.wilsonhs.toby.templates.commands.CommandBase;

/**
 *
 * @author Toby
 */
public abstract class Mode extends CommandBase implements NetworkListener{
    protected boolean faceForwards;
    
    public Mode(boolean faceForwards){
        requires(serverSubsystem);
        requires(activeRotationCorrectionSubsystem);
        serverSubsystem.addListener(this);
        this.faceForwards = faceForwards;
    }
    
    private double getAngle(int x){
        return x-240 * 21.0;
    }
    
    protected void pointTowards(int direction){
        double angle = getAngle(direction);
        activeRotationCorrectionSubsystem.setRelativeHeading(angle);
    }
    
    public boolean isForwardFacing(){
        return faceForwards;
    }
    
    
}
