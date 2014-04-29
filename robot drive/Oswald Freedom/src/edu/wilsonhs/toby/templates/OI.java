
package edu.wilsonhs.toby.templates;

import edu.wpi.first.wpilibj.Joystick;
/**
 * 
 * @author toby
 */
public class OI {
    
    public static Joystick STICK;
    
    public OI() {
        STICK = new Joystick(1);
    }  
}

