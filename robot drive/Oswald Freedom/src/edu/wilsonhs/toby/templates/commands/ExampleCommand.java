
package edu.wilsonhs.toby.templates.commands;

import edu.wilsonhs.toby.templates.RobotMap;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author bradmiller
 */
public class ExampleCommand extends CommandBase {
    private boolean hasRun = false;

    public ExampleCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute(){
        if(!hasRun){
            try {
                RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 1, 0, 0);
                Thread.sleep(3000);
                RobotMap.CHASSIS.mecanumDrive_Cartesian(0, 0, 0, 0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            hasRun = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        hasRun = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
