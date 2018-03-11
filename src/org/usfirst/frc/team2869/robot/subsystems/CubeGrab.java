package org.usfirst.frc.team2869.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import org.usfirst.frc.team2869.robot.util.other.Looper;
import org.usfirst.frc.team2869.robot.util.other.Subsystem;

/**
 *
 */
public class CubeGrab extends Subsystem {
    public static DoubleSolenoid armCylinder = new DoubleSolenoid(5, 6);
    private static Value isActivated = armCylinder.get();

    public static void toggleCylinders() {
        if (isActivated == DoubleSolenoid.Value.kForward) {
            armCylinder.set(DoubleSolenoid.Value.kForward);

        } else {
            armCylinder.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void initDefaultCommand() {
    }

    @Override
    public void outputToSmartDashboard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroSensors() {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkSystem() {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        // TODO Auto-generated method stub

    }
}

