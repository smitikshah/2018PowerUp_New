package org.usfirst.frc.team2869.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CubeLift extends Subsystem {
    private static final double liftSpeed = 1;
    public static TalonSRX armTalon = Arm.getInstance().armTalon;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public static void liftOrLower(boolean lift) {
        if (lift) {
            armTalon.set(ControlMode.PercentOutput, -liftSpeed);
        } else {
            armTalon.set(ControlMode.PercentOutput, liftSpeed);
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

