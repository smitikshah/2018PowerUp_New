package org.usfirst.frc.team2869.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Cylinder extends Subsystem {
    private static Solenoid leftCylinder = new Solenoid(0);
    private static Solenoid rightCylinder = new Solenoid(1);
    private static boolean isActivated = leftCylinder.get();


    public static void toggleCylinders() {
        if (isActivated) {
            leftCylinder.set(true);
            rightCylinder.set(true);
        } else {
            leftCylinder.set(false);
            rightCylinder.set(false);
        }
    }

    public void extendCylinder() {

    }

    public void retractCylinder() {

    }

    protected void initDefaultCommand() {

    }
}

