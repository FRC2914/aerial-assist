/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wilsonhs.toby.general;

import edu.wilsonhs.toby.templates.commands.CommandBase;

/**
 *
 * @author toby
 */
public abstract class DriveController extends CommandBase{
    public int getRotationPriority(){
        return 0;
    }
    
    public int getXPriority(){
        return 0;
    }
    
    public int getYPriority(){
        return 0;
    }
    
    public double getX(){
        return 0;
    }
    
    public double getY(){
        return 0;
    }
    
    public double getRotation(){
        return 0;
    }
    
    public abstract boolean isEnabled();
    
    public void update(){};

}
