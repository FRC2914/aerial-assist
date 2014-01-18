package edu.wilsonhs.demo;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;

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
    private final Gyro GYRO = new Gyro(2);
//Misc. Variables
    double lastGyroPosition = 0;

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        GYRO.setSensitivity(0.007);
        GYRO.reset();
        lastGyroPosition = 0;
        while (DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled()) {
            //get positions
            double currentPosition = GYRO.getAngle() % 360;//0 to 360 . (360 - gyro.getAngle())%360;
            double desiredPosition = 0;
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
        System.out.println("best difference" + bestDiff);
        double speed = (160.0/(1+1*MathUtils.pow(Math.E,-0.29 * bestDiff))-80.0)/100;//Our best fit curve
        System.out.println("calculated speed"+speed);
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
    }

    public void test() {
    }
}