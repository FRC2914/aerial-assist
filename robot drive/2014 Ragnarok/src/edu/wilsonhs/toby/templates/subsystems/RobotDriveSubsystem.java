/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.general.DriveController;
import edu.wilsonhs.toby.templates.RobotMap;
import edu.wilsonhs.toby.templates.commands.driveCommands.DriveCommand;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.util.Vector;

/**
 *
 * @author Toby
 */
public class RobotDriveSubsystem extends Subsystem {

    private List controllers = new List();

    protected void initDefaultCommand() {
    }

    public void addController(DriveController controller) {
        System.out.println("Attempting to add " + controller.getName() + " to controllers");
        if (!controllers.contains(controller)) {
            controllers.add(controller);
            System.out.println(controller.getName() + " added");
        }
    }

    public void setFront(boolean shootingDirection) {
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, shootingDirection);
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kRearLeft, shootingDirection);
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kRearRight, !shootingDirection);
        RobotMap.CHASSIS.setInvertedMotor(RobotDrive.MotorType.kFrontRight, !shootingDirection);

    }

    public void startDriveLoop() {
        while (true) {
            try {
                drive();
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void drive() {
        double x = 0;
        double y = 0;
        double rotation = 0;
        int xPriority = 0;
        int yPriority = 0;
        int rotPriority = 0;
        for (int i = 0; i < controllers.size(); i++) {
            DriveController controller = (DriveController) controllers.get(i);
            if (controller.isEnabled()) {
                controller.update();
                if (controller.getRotationPriority() >= rotPriority) {
                    if (controller.getRotation() > 0.05 || controller.getRotation() < -0.05) {
                        rotPriority = controller.getRotationPriority();
                        rotation = controller.getRotation();
                    }
                }
                if (controller.getXPriority() >= xPriority) {
                    if (controller.getX() > 0.05 || controller.getX() < -0.05) {
                        xPriority = controller.getXPriority();
                        x = controller.getX();
                    }
                }
                if (controller.getYPriority() >= yPriority) {
                    if (controller.getY() > 0.05 || controller.getY() < -0.05) {
                        yPriority = controller.getYPriority();
                        y = controller.getY();
                    }
                }
            }
        }
        RobotMap.CHASSIS.mecanumDrive_Cartesian(x, y, rotation, 0);
    }

}
