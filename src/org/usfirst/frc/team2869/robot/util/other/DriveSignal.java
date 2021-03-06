package org.usfirst.frc.team2869.robot.util.other;

import org.usfirst.frc.team2869.robot.Constants;

/**
 * A drivetrain command consisting of the left, right motor settings and whether the brake mode is enabled.
 */
public class DriveSignal {

    public static DriveSignal BRAKE = new DriveSignal(0, 0, true);
    protected double mLeftMotor;
    protected double mRightMotor;
    protected boolean mBrakeMode;

    public DriveSignal(double left, double right) {
        this(left, right, true);
    }

    public DriveSignal(double left, double right, boolean brakeMode) {
        mLeftMotor = left;
        mRightMotor = right;
        mBrakeMode = brakeMode;
    }

    public double getLeft() {
        return mLeftMotor;
    }

    public double getRight() {
        return mRightMotor;
    }

    public boolean getBrakeMode() {
        return mBrakeMode;
    }

    @Override
    public String toString() {
        return "L: " + mLeftMotor + ", R: " + mRightMotor + (mBrakeMode ? ", BRAKE" : "");
    }

    public double getLeftNativeVel() {
        return MkMath.InchesPerSecToUnitsPer100Ms(mLeftMotor * Constants.DRIVE.MAX_VEL);
    }

    public double getLeftNativeVelTraj() {
        return MkMath.InchesPerSecToUnitsPer100Ms(mLeftMotor);
    }

    public double getRightNativeVelTraj() {
        return MkMath.InchesPerSecToUnitsPer100Ms(mRightMotor);
    }

    public double getRightNativeVel() {
        return MkMath.InchesPerSecToUnitsPer100Ms(mRightMotor * Constants.DRIVE.MAX_VEL);
    }

}
