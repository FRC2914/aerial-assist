/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.driveCommands.DriveCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Dev
 */
public class RobotDriveSubsystem extends Subsystem{
    private double x = 0, y = 0, rotation = 0;
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }
    
    public void drive(double x, double y, double rotation){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }
    
    public void drive(){
        RobotMap.CHASSIS.mecanumDrive_Cartesian(x, y, rotation, 0);
        
    }
    
    
    
}
