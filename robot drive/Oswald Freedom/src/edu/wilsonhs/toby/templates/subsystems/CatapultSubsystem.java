/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.templates.RobotMap;

/**
 *
 * @author Toby
 */
public class CatapultSubsystem {
    public void fire(){
        try {
            RobotMap.CATAPULT_IN.set(false);
            RobotMap.CATAPULT_OUT.set(true);
            Thread.sleep(500);
            RobotMap.CATAPULT_IN.set(true);
            RobotMap.CATAPULT_OUT.set(false);
        } catch (InterruptedException ex) {
            RobotMap.CATAPULT_IN.set(true);
            RobotMap.CATAPULT_OUT.set(false);
            ex.printStackTrace();
        }
    }
}
