/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.general;

import edu.wpi.first.wpilibj.Gyro;
import java.util.Vector;

/**
 *
 * @author Dev
 */
public class EnhancedGyro extends Gyro{
    private boolean recording;
    private long lastRecordTime;
    private Vector pastData = new Vector(15, 0);
    private long timeOfLastReset;
    private double driftPerSecond = 0d;
    public EnhancedGyro(int channel){
        super(channel);
        lastRecordTime = System.currentTimeMillis();
        timeOfLastReset = System.currentTimeMillis();
    }

    public double getAngle() {
        if(recording && System.currentTimeMillis() - lastRecordTime >= 1000){
            double angle = super.getAngle();
            System.out.println(angle);
            pastData.insertElementAt(new Double(angle), 0);
            lastRecordTime = System.currentTimeMillis();
        }
            return super.getAngle()/* + (driftPerSecond * ((System.currentTimeMillis() - timeOfLastReset)/1000d))*/;
    }
    
    public void startRecording(){
        recording = true;
        reset();
    }
    
    public void stopRecording(){
        recording = false;
        double firstRecord, lastRecord, totalDrift;
        firstRecord = ((Double)pastData.elementAt(14)).doubleValue();
        lastRecord = ((Double)pastData.elementAt(0)).doubleValue();
        totalDrift = lastRecord - firstRecord;
        System.out.println(firstRecord + " - " + lastRecord + "\ndrifted " + totalDrift + " degrees in 15 seconds");
        if(totalDrift < 15d){
            driftPerSecond = totalDrift/15d;
        }
    }

    public void reset() {
        super.reset();
        timeOfLastReset = System.currentTimeMillis();
    }
    
    
    
    
    
}
