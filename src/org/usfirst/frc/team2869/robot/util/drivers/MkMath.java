package org.usfirst.frc.team2869.robot.util.drivers;

import org.usfirst.frc.team2869.robot.Constants;
import org.usfirst.frc.team2869.robot.Constants.ARM;
import org.usfirst.frc.team2869.robot.Constants.DRIVE;

//import org.usfirst.frc.team2869.robot.Constants.ARM;

public class MkMath {

    public static double nativeUnitsPer100MstoInchesPerSec(double vel) {
        return 10 * nativeUnitsToInches(vel);
    }

    public static double nativeUnitsToInches(double units) {
        return (units / Constants.DRIVE.CODES_PER_REV) * (Constants.DRIVE.CIRCUMFERENCE);
    }

    public static double InchesPerSecToUnitsPer100Ms(double vel) {
        return InchesToNativeUnits(vel) / 10;
    }

    public static double InchesToNativeUnits(double in) {
        return (Constants.DRIVE.CODES_PER_REV) * (in / Constants.DRIVE.CIRCUMFERENCE);
    }

    public static double AngleToVel(double angle) {
        return (angle / 360) * DRIVE.TURN_IN_PLACE_CIRCUMFERENCE;
    }

    public static double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }

    public static double angleToNativeUnits(double ang) {
        return ((ang / 360.0) / ARM.GEAR_RATIO) * 4096.0;

    }

}
