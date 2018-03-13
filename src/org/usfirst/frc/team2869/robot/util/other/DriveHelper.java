package org.usfirst.frc.team2869.robot.util.other;

import org.usfirst.frc.team2869.robot.Constants.INPUT;

public class DriveHelper {

    private static final double kThrottleDeadband = INPUT.kThrottleDeadband;
    private static final double kWheelDeadband = INPUT.kWheelDeadband;

    public static DriveSignal cheesyDrive(double throttle, double wheel, boolean cubeInputs) {

        double leftMotorSpeed;
        double rightMotorSpeed;

        double moveValue = limit(throttle);
        double rotateValue = limit(wheel);

        moveValue = handleDeadband(moveValue, kThrottleDeadband);
        rotateValue = handleDeadband(rotateValue, kWheelDeadband);

        if (cubeInputs) {
            rotateValue = rotateValue * rotateValue * rotateValue;
        }
        rotateValue = rotateValue / 2;

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        return new DriveSignal(leftMotorSpeed, rightMotorSpeed);
    }


    public static DriveSignal SmitiDrive(double forward, double reverse, double turn, boolean cubeInputs) {
        double deadband = .08;
        double power = 0.0;

        if (turn < deadband && turn > -deadband) turn = 0.0;
        if (forward < deadband && reverse > -deadband) forward = 0.0;
        if (reverse < deadband && reverse > -deadband) reverse = 0.0;

        if (forward > 0.0) power = forward;
        else power = reverse;

        double left;
        double right;

        left = power + turn;
        right = power - turn;

        return new DriveSignal(left, right);
    }


    protected static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }

    public static double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }

}
