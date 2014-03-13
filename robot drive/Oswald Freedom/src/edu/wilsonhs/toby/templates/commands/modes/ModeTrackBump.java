/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.commands.modes;

import com.sun.squawk.util.MathUtils;
import edu.wilsonhs.toby.templates.commands.CommandBase;
import edu.wilsonhs.toby.templates.commands.Mode;
import edu.wilsonhs.toby.templates.commands.RobotDriveCommand;

/**
 *
 * @author toby
 */
public class ModeTrackBump extends Mode {

    private double rotation = 0.0;
    private static final double ROTATION_PROPORTION_CONSTANT = 0.6;
    private static final long CUTOFF_TIME = 333;
    private static final int DEADZONE = 10;
    private static final double INITIAL_POWER = 0.1;
    private long timeout;

    public ModeTrackBump(RobotDriveCommand driveCommand) {
        super(driveCommand);
        driveCommand.setFaceForwards(true);
    }

    public void onCommand(String[] command) {
        if (command[0].equals("bump")) {
            int x = Integer.parseInt(command[1]);
            if (x != 0 || Math.abs(x) < DEADZONE) {
                System.out.println(((x - 160) / 160.0) * 21.0);
                rotation = (Math.abs((x - 160) / 160.0)/((x - 160) / 160.0))*MathUtils.pow((x - 160) / 160.0,2) * -ROTATION_PROPORTION_CONSTANT;
                if(rotation < 0){
                    rotation -= INITIAL_POWER;
                }else{
                    rotation += INITIAL_POWER;
                }
                timeout = System.currentTimeMillis();
            }else if( Math.abs(x) < DEADZONE){
                timeout = System.currentTimeMillis();
            }
        }
    }

    public double getRotation() {
        if (System.currentTimeMillis() - timeout < CUTOFF_TIME) {
            return rotation;
        } else {
            return 0;
        }
    }

}
