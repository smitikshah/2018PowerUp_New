package org.usfirst.frc.team2869.robot;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static int leftMotor1 = 5; //FrontLeft
    public static int leftMotor2 = 1; //RearLeft
    public static int rightMotor1 = 2; //FrontRight
    public static int rightMotor2 = 3; //RearRight
    public static int climberSolenoid1 = 1;
    public static int climberSolenoid2 = 0;
    public static int rampSolenoid1 = 2;
    public static int rampSolenoid2 = 3;
    //public static int intake1 = 10;
    //public static int intake2 = 11;


    public static VictorSPX intake1 = new VictorSPX(10);
    public static VictorSPX intake2 = new VictorSPX(11);
    /*
     * CAN Values
     */
    //public static int lift = 2; //TODO: Reset these values because they were lost in the pull
    //public static int shooterWheel1 = 1;
    //public static int shooterWheel2 = 0;
    //public static int climber = 0;
    /*
     * ANALOG Values
     */
    //public static int shooterA = 0, shooterB = 1;
    //public static int leftEncoder1 = 14, leftEncoder2 = 15;
    //public static int rightEncoder1 = 12, rightEncoder2 = 13;

    /*
     * OTHER CONSTANTS
     */
    public static double driveP = 0, driveI = 0, driveD = 0;
    public static double driveForward = 1; //OG value 5300
    public static double driveRotation = 1;
    public static double shooterSpeed = .6;
    //public static double liftSpeed = 1;
    public static double distanceMultiplier = 1;
    public static double driveRotationTolerance = .04;
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
    public static double pressureConstant = 30.0394224802;
    public static double speedMultiplier = .3;
    public static double speedAtFullVoltage = 12.33;
    public static double tiltSpeed = .5;
    public static int cylinderActuation;
    public static int cylinderActuation2;
}
