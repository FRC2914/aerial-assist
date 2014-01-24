package edu.wilsonhs.demo;

import com.sun.squawk.util.MathUtils;
import com.sun.squawk.util.StringTokenizer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author Seb
 */
public class GyroTest extends SimpleRobot {

//Parameters
    private final double PROPORTION = 1.0 / 50;
//Motors
    private final Jaguar RIGHT_FRONT = new Jaguar(2);
    private final Jaguar RIGHT_REAR = new Jaguar(3);
    private final Jaguar LEFT_FRONT = new Jaguar(4);
    private final Jaguar LEFT_REAR = new Jaguar(5);
//Robot Drive
    private final RobotDrive CHASSIS = new RobotDrive(LEFT_FRONT, LEFT_REAR, RIGHT_FRONT, RIGHT_REAR);
//Sensors
    private final Joystick JOYSTICK = new Joystick(1);
    private final Gyro GYRO = new Gyro(2);
//Misc. Variables
    double lastGyroPosition = 0;
    private boolean IS_GYRO_UPSIDE_DOWN = true;//R.I.P. Proton M
    double lastrun = System.currentTimeMillis();
    double desiredPosition = 0;

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        CHASSIS.setSafetyEnabled(false);
        GYRO.setSensitivity(0.007);
        GYRO.reset();
        lastGyroPosition = 0;
        try {
            ServerSocketConnection ssc = (ServerSocketConnection) Connector.open("socket://:2914");
            System.out.println("Ready for connect");
            StreamConnection sc = null;
            InputStream is = null;
            try {
                sc = ssc.acceptAndOpen();
                System.out.println("connected");
                is = sc.openInputStream();
                int ch;
                String msg = "";
                while (DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled()) {
                    long timeoutStart = System.currentTimeMillis();
                    while ((ch = is.read()) != -1 && System.currentTimeMillis() - timeoutStart < 500) {
                        //System.out.print((char)ch);
                        if (((char) ch) == ';') {
                            System.out.println(msg);
                            parseObject(msg);
                            msg = "";
                        } else if (((char) ch) == '\n') {
                            break;
                        }else{
                            msg += (char) ch;
                        }
                        //System.out.write(ch);
                    }
                    //get positions
                    double currentPosition = (360 - GYRO.getAngle()) % 360;//0 to 360 . (360 - gyro.getAngle())%360;
                    //Gyro Deadband: if gyro hasn't changed enough, ignore it
                    if (Math.abs(currentPosition - lastGyroPosition) < 0.0001) {//change back to 0.01
                        currentPosition = lastGyroPosition;
                    }

                    //find the shortest path and calulate speed
                    double speed = calculateShortestPath(currentPosition, desiredPosition, PROPORTION);
                    //move at the given speed
                    //CHASSIS.mecanumDrive_Cartesian(0, 0, speed, 0);
                    CHASSIS.arcadeDrive(0, speed);
                    //System.out.println(speed);
                    lastGyroPosition = currentPosition;
                }
            } finally {
                ssc.close();
                sc.close();
                is.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * finds the shortest path from current to desired and returns a
     * speed(between -1 and +1) at which to turn to get there most efficiently
     *
     * @param current The Current Position/Rotation
     * @param desired The Desired Position/Rotation
     * @param proportion the P part of the PID loop
     * @return
     */
    public double calculateShortestPath(double current, double desired, double proportion) {
        double[] possiblePositions = new double[]{desired, desired + 360, desired - 360};
        double[] differences = new double[]{possiblePositions[0] - current, possiblePositions[1] - current, possiblePositions[2] - current};
        double[] absValueDifferences = new double[3];
        for (int i = 0; i < 3; i++) {
            absValueDifferences[i] = Math.abs(differences[i]);
        }
        int bestIndex = findIndexOfMin(absValueDifferences);
        double bestDiff = differences[bestIndex];
//        System.out.println("best difference" + bestDiff);
        double speed = (160.0 / (1 + 1 * MathUtils.pow(Math.E, -0.29 * bestDiff)) - 80.0) / 100;//Our best fit curve
//        System.out.println("calculated speed"+speed);
        if(speed > 0.5 || speed < -0.5){
            speed = 0.5* (speed/Math.abs(speed));
        }
        return speed;
    }

    /**
     *
     * @param numbers the numbers to be searched
     * @return the value of the smallest number in the given set
     */
    public double findMin(double[] numbers) {
        int smallestIndex = 0;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < numbers[smallestIndex]) {
                smallestIndex = i;
            }
        }
        return numbers[smallestIndex];
    }

    /**
     *
     * @param numbers the numbers to be searched
     * @return the index of the smallest number in the given set
     */
    public int findIndexOfMin(double[] numbers) {
        int smallestIndex = 0;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < numbers[smallestIndex]) {
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    /**
     * will keep the given double within bounds of 0 to +360
     *
     * @param x
     * @return
     */
    public double limitTo360(double angle) {
        if (angle > 360) {
            angle = 360;
        }
        if (angle < 0) {
            angle = 0;
        }
        return angle;
    }

    public void operatorControl() {
        GYRO.setSensitivity(0.007);
        GYRO.reset();
        lastGyroPosition = 0;
        while (DriverStation.getInstance().isOperatorControl() && DriverStation.getInstance().isEnabled()) {
            //get positions
            double currentPosition = GYRO.getAngle() % 360;//0 to 360 . (360 - gyro.getAngle())%360;
            double desiredPosition = 0;
            //Gyro Deadband: if gyro hasn't changed enough, ignore it
            if (Math.abs(currentPosition - lastGyroPosition) < 0.0001) {//change back to 0.01
                currentPosition = lastGyroPosition;
            }

            //find the shortest path and calulate speed
            double speed = calculateShortestPath(currentPosition, desiredPosition, PROPORTION);
            CHASSIS.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
            CHASSIS.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            //move at the given speed
            CHASSIS.mecanumDrive_Cartesian(JOYSTICK.getX(), JOYSTICK.getY(), speed, 0);
            //CHASSIS.arcadeDrive(0, speed);
            //System.out.println(speed);
            lastGyroPosition = currentPosition;
        }
    }

    public void test() {
    }

    public void parseObject(String incomingObject) {
        StringTokenizer params = new StringTokenizer(incomingObject, ",");
        String type = params.nextToken();
        int x = Integer.parseInt(params.nextToken());
        int y = Integer.parseInt(params.nextToken());
        double area = Double.parseDouble(params.nextToken());
        if((x > 165 || x < 155) && type.equalsIgnoreCase("ball")){
        desiredPosition = lastGyroPosition + (1.0/77777.0)*-MathUtils.pow(x - 160, 3);
    }else{
            desiredPosition = lastGyroPosition;
        }
    }
}
