/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.general;

import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.Gyro;
/**
 *
 * @author Toby
 */
public class EnhancedGyro extends Gyro{
    private double lastAngle = 0;
    private long timeOfLastMeasurement;
    private static final double alpha = 0.98;
    private Accelerometer acc;
    public EnhancedGyro(int channel, Accelerometer acc){
        super(channel);
        timeOfLastMeasurement = System.currentTimeMillis();
        this.acc = acc;
    }

    public double getAngle() {
        double dt = System.currentTimeMillis() - timeOfLastMeasurement/1000;
        timeOfLastMeasurement = System.currentTimeMillis();
        double angle  = super.getAngle();
        angle = (1-alpha)*(lastAngle + angle * dt) + (alpha)*(acc.getAcceleration());
        lastAngle = angle;
        return angle;
    }
    
    
    
    
    
}
